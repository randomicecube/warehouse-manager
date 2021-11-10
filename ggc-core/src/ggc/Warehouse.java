package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import java.text.Collator;
import java.util.Locale;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Warehouse's current date */
  private int _date = 0;

  private int _nextTransactionKey = 0;

  /** Warehouse's current available balance */
  private double _availableBalance = 0;

  /** All products associated with the warehouse */
  private Map<String, Product> _products = new HashMap<String, Product>();

  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new HashMap<String, Partner>();

  private Map<Integer, Transaction> _transactions = new LinkedHashMap<Integer, Transaction>();

  private Map<Product, Double> _biggestKnownPrices = new HashMap<Product, Double>();

  private Map<Product, Double> _smallestWarehousePrices = new HashMap<Product, Double>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile)
      throws IOException, BadEntryException, NoSuchPartnerKeyException, NoSuchProductKeyException {
    try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
      String s;
      while ((s = in.readLine()) != null) {
        String line = new String(s.getBytes(), "UTF-8");

        String[] fields = line.split("\\|");

        switch (fields[0]) {
          case "PARTNER" -> registerPartner(
            fields[1],
            fields[2],
            fields[3]
          );

          case "BATCH_S" -> registerBatch(
            fields[1],
            fields[2],
            fields[3],
            fields[4]
          );

          case "BATCH_M" -> registerBatch(
            fields[1],
            fields[2],
            fields[3],
            fields[4],
            fields[5],
            fields[6]
          );

          default -> throw new BadEntryException(fields[0]);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    } catch (PartnerKeyAlreadyUsedException e) {
      e.printStackTrace();
    } catch (NoSuchPartnerKeyException e) {
      e.printStackTrace();
    } catch (NoSuchProductKeyException e) {
      e.printStackTrace();
    }
  }

  /** @return current warehouse date */
  public int getDate() {
    return _date;
  }

  /**
   * update warehouse's date
   * 
   * @param days days to add to current date
   * @throws NoSuchDateException
   */
  public void updateDate(int days) throws NoSuchDateException {
    if (days <= 0) {
      throw new NoSuchDateException(days);
    }
    _date += days;
  }

  /** @return warehouse available balance */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  /** @return warehouse accounting balance */
  public double getAccountingBalance() {
    double accountingBalance = _availableBalance;
    TransactionVisitor transactionVisitor = new TransactionManager();
    int date = getDate();
    for (Transaction t: _transactions.values()) {
      accountingBalance += t.accept(transactionVisitor, date);
    }
    return accountingBalance;
  }

  /**
   * register a partner to be associated with the warehouse
   * 
   * @param key     partner's key
   * @param name    partner's name
   * @param address partner's address
   * @throws PartnerKeyAlreadyUsedException
   */
  public void registerPartner(String key, String name, String address) 
    throws PartnerKeyAlreadyUsedException {
      String lowerCasePartnerKey = key.toLowerCase();
      for (String mapKey : _partners.keySet()) {
        if (mapKey.toLowerCase().equals(lowerCasePartnerKey)) {
          throw new PartnerKeyAlreadyUsedException(key);
        }
      }
      Partner partner = new Partner(key, name, address);
      _partners.put(key, partner);
      for (Product p: _products.values()) {
        p.partnerNowObserving(partner);
      }
  }

  /**
   * registers a batch to a given partner - applies to simple products
   * 
   * @param productKey product's key
   * @param partnerKey partner's key
   * @param price      batch's assoicated price
   * @param stock      batch's product stock
   * @throws NoSuchPartnerKeyException
   */
  public void registerBatch(String productKey, String partnerKey,
                            String price, String stock) 
    throws NoSuchPartnerKeyException {

      Integer parsedStock = Integer.parseInt(stock);
      Double parsedPrice = Double.parseDouble(price);

      Product product;
      Partner partner;

      try {
        product = getProduct(productKey);
        product.updateStock(parsedStock);
      } catch (NoSuchProductKeyException e) {
        product = new Product(productKey, parsedStock, parsedPrice);
        _products.put(productKey, product);
      }

      // TODO - is it supposed to be > or <?
      if (parsedPrice > product.getProductPrice()) {
        product.updatePrice(parsedPrice, _smallestWarehousePrices);
      }

      Double biggestPrice = _biggestKnownPrices.get(product);
      Double smallestPrice = _smallestWarehousePrices.get(product);

      if (biggestPrice == null) {
        _biggestKnownPrices.put(product, parsedPrice);
        _smallestWarehousePrices.put(product, parsedPrice);
      } else if (parsedPrice > biggestPrice) {
        _biggestKnownPrices.put(product, parsedPrice);
      } else if (parsedPrice < smallestPrice) {
        _smallestWarehousePrices.put(product, parsedPrice);
      }

      try {
        partner = getPartner(partnerKey);
        Batch batch = new Batch(product, parsedStock, parsedPrice, partner);
        partner.addBatch(batch);
        product.addBatch(batch);
      } catch (NoSuchPartnerKeyException e) {
        throw new NoSuchPartnerKeyException(e.getKey());
      }

      for (Partner p: _partners.values()) {
        product.partnerNowObserving(p);
      }

  }

  /**
   * registers a batch to a given partner - applies to breakdown products
   * 
   * @param productKey        product's key
   * @param partnerKey        partner's key
   * @param price             batch's associated proce
   * @param stock             batch's product stock
   * @param aggravationFactor product breakdown aggravation factor
   * @param recipe            recipe in it's string format
   * @throws NoSuchProductKeyException
   * @throws NoSuchPartnerKeyException
   */
  public void registerBatch(String productKey, String partnerKey, String price, 
                            String stock, String aggravationFactor, String recipe) 
    throws NoSuchProductKeyException, NoSuchPartnerKeyException {

      Integer parsedStock = Integer.parseInt(stock);
      Double parsedPrice = Double.parseDouble(price);
      Double parsedAggravationFactor = Double.parseDouble(aggravationFactor);

      Product batchProduct;
      Partner partner;

      String[] ingredients = recipe.split("\\#");

      Recipe productRecipe = new Recipe();

      for (String i : ingredients) {
        String[] ingredientFactors = i.split(":");

        try {
          Product ingredient = getProduct(ingredientFactors[0]);
          productRecipe.addIngredient(ingredient, Integer.parseInt(ingredientFactors[1]));
        } catch (NoSuchProductKeyException e) {
          throw new NoSuchProductKeyException(ingredientFactors[0]);
        }
      }

      try {
        batchProduct = getProduct(productKey);
        batchProduct.updateStock(parsedStock);
      } catch (NoSuchProductKeyException e) {
        batchProduct = new BreakdownProduct(productRecipe, parsedAggravationFactor, 
                                            productKey, parsedStock, parsedPrice);
        _products.put(productKey, batchProduct);
      }

      // TODO - is it supposed to be > or <?
      if (parsedPrice > batchProduct.getProductPrice()) {
        batchProduct.updatePrice(parsedPrice, _smallestWarehousePrices);
      }

      Double biggestPrice = _biggestKnownPrices.get(batchProduct);
      Double smallestPrice = _smallestWarehousePrices.get(batchProduct);

      if (biggestPrice == null) {
        _biggestKnownPrices.put(batchProduct, parsedPrice);
        _smallestWarehousePrices.put(batchProduct, parsedPrice);
      } else if (parsedPrice > biggestPrice) { 
        _biggestKnownPrices.put(batchProduct, parsedPrice);
      } else if (parsedPrice < smallestPrice) {
        _smallestWarehousePrices.put(batchProduct, parsedPrice);
      }

      try {
        partner = getPartner(partnerKey);
        Batch batch = new Batch(batchProduct, parsedStock, parsedPrice, partner);
        partner.addBatch(batch);
        batchProduct.addBatch(batch);
      } catch (NoSuchPartnerKeyException e) {
        throw new NoSuchPartnerKeyException(e.getKey());
      }

      for (Partner p: _partners.values()) {
        batchProduct.partnerNowObserving(p);
      }

  }

  /**
   * get a product, given their key
   * 
   * @param key product's key
   * @return desired product
   * @throws NoSuchProductKeyException
   */
  public Product getProduct(String key) throws NoSuchProductKeyException {
    String lowerCaseProductKey = key.toLowerCase();
    for (String mapKey : _products.keySet()) {
      if (mapKey.toLowerCase().equals(lowerCaseProductKey)) {
        return _products.get(mapKey);
      }
    }
    throw new NoSuchProductKeyException(key);
  }

  /**
   * get a partner, given their key
   * 
   * @param key partner's key
   * @return desired partner
   * @throws NoSuchPartnerKeyException
   */
  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    String lowerCasePartnerKey = key.toLowerCase();
    for (String mapKey : _partners.keySet()) {
      if (mapKey.toLowerCase().equals(lowerCasePartnerKey)) {
        return _partners.get(mapKey);
      }
    }
    throw new NoSuchPartnerKeyException(key);
  }

  public String getPartnerString(String key) throws NoSuchPartnerKeyException {
    return getPartner(key).partnerStringed(getDate());
  }

  public Collection<Notification> readPartnerNotifications(String key) 
    throws NoSuchPartnerKeyException {
      Partner partner = getPartner(key);
      List<Notification> notifications = new ArrayList<Notification>(partner.getNotifications());
      partner.clearNotifications();
      return notifications;
  }

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _partners;
  }

  /** @return all products associated with the warehouse */
  public Map<String, Product> getProducts() {
    return _products;
  }

  /** @return a Collection with all the products associated with the warehouse */
  public Collection<String> getProductsCollection() {
    getProducts()
      .values()
      .stream()
      .forEach(p -> p.setPrice(_biggestKnownPrices.get(p)));
    
    List<String> productKeys = getProducts()
      .values()
      .stream()
      .map(product -> product.getProductKey())
      .collect(Collectors.toList());

    Collections.sort(productKeys, Collator.getInstance(Locale.getDefault()));

    List<String> stringedProducts = new ArrayList<String>();

    for (String product: productKeys) {
      try {
        stringedProducts.add(getProduct(product).toString());
      } catch (NoSuchProductKeyException e) {
        // will never happen
        e.printStackTrace();
      }
    }

    return stringedProducts;

  }

  public Collection<Batch> getBatches() {
    List<Product> products = getProducts()
      .values()
      .stream()
      .collect(Collectors.toList());

    List<Batch> batches = new ArrayList<Batch>();

    for (Product p: products) {
      for (Batch b: p.getBatches()) {
        batches.add(b);
      }
    }

    return batches;
  }

  /**
   * @return a Collection with all the batches (in toString form)
   * with partners associated with the warehouse
   */
  public Collection<String> getBatchesCollection() {
    List<String> productKeys = getProducts()
      .values()
      .stream()
      .map(product -> product.getProductKey())
      .collect(Collectors.toList());

    Collections.sort(productKeys, Collator.getInstance(Locale.getDefault()));

    List<String> batches = new ArrayList<String>();

    for (String key: productKeys) {
      try {
        Product product = getProduct(key);
        for (String batch: product.getBatchStrings()) {
          batches.add(batch);
        }
      } catch (NoSuchProductKeyException e) {
        // will never happen
        e.printStackTrace();
      }
    }

    return batches;
  }

  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    List<String> partnerKeys = getPartners()
      .values()
      .stream()
      .map(partner -> partner.getPartnerKey())
      .collect(Collectors.toList());

    Collections.sort(partnerKeys, Collator.getInstance(Locale.getDefault()));

    List<String> stringedPartners = new ArrayList<String>();

    for (String partner: partnerKeys) {
      try {
        stringedPartners.add(getPartner(partner).partnerStringed(getDate()));
      } catch (NoSuchPartnerKeyException e) {
        // will never happen
        e.printStackTrace();
      }
    }

    return stringedPartners;
  }

  /** 
   * @param partnerKey partner's key
   * @return a Collection with all batches associated with a given partner  
   */
  public Collection<String> getBatchesByPartner(String partnerKey)
    throws NoSuchPartnerKeyException {
      List<String> batches = getPartner(partnerKey)
        .getPartnerBatches()
        .stream()
        .map(batch -> batch.toString())
        .collect(Collectors.toList());

      Collections.sort(batches, Collator.getInstance(Locale.getDefault()));

      return batches;
  }

  /** 
   * @param productKey product's key
   * @return a Collection with all batches associated with a given product  
   */
  public Collection<String> getBatchesByProduct(String productKey)
    throws NoSuchProductKeyException {
      return getProduct(productKey).getBatchStrings();
  }

  /**
   * @param transactionKey transaction's key
   * @return transaction with the given key
   */
  public Transaction getTransaction(int transactionKey) 
    throws NoSuchTransactionKeyException {
    Transaction t = _transactions.get(transactionKey);
    if (t == null) {
      throw new NoSuchTransactionKeyException(transactionKey);
    }
    t.updateActualPrice(getDate());
    return t;
  }

  public void toggleProductNotifications(String partnerKey, String productKey)
    throws NoSuchProductKeyException, NoSuchPartnerKeyException {
      Product product = getProduct(productKey);
      Partner partner = getPartner(partnerKey);
      if (!product.getObservingPartners().get(partner)) {
        product.partnerNowObserving(partner);
      } else {
        product.partnerNoLongerObserving(partner);
      }
  }

  public Collection<Acquisition> getPartnerAcquisitions(String partnerKey)
    throws NoSuchPartnerKeyException {
      // TODO - UNMODIFIABLE COLLECTION
      return getPartner(partnerKey).getAcquisitions();
  }

  public Collection<Transaction> getPartnerSales(String partnerKey)
    throws NoSuchPartnerKeyException {
      Collection<Transaction> sales = getPartner(partnerKey).getSales();
      int currentDate = getDate();
      for (Transaction sale: sales) {
        sale.updateActualPrice(currentDate);
      }
      return sales;
  }

  public Collection<Transaction> getPaymentsByPartner(String partnerKey)
    throws NoSuchPartnerKeyException {
      return getPartnerSales(partnerKey)
        .stream()
        .filter(sale -> sale.isPaid())
        .collect(Collectors.toList());
  }

  public Collection<Batch> getProductBatchesUnderGivenPrice(double priceCap) {
    Collection<Batch> availableBatches = getBatches();
    availableBatches
      .stream()
      .filter(batch -> batch.getPrice() < priceCap)
      .collect(Collectors.toList()).sort(new BatchComparator());
    return availableBatches; 
  }

  public void receivePayment(int transactionKey)
    throws NoSuchTransactionKeyException {
      Sale sale = (Sale) getTransaction(transactionKey);
      if (sale.isPaid()) {
        return;
      }
      int currentDate = getDate();
      sale.updatePaymentDate(currentDate);
      sale.updateActualPrice(currentDate);
      double pricePaid = sale.getActualPrice();
      sale.updatePaid(pricePaid);
      sale.getPartner().getPartnerStatus().payTransaction(sale, currentDate);
      updateBalanceSale(pricePaid);
  }

  public void registerProduct(String productKey, int stock) {
    Product product = new Product(productKey, stock);
    _products.put(productKey, product);
  }

  public void registerProduct(String productKey, int stock, Map<String, Integer> ingredients, double alpha)
    throws NoSuchProductKeyException {
      // check if all ingredients are registered
      try {
        for (String key: ingredients.keySet()) {
          getProduct(key);
        }
      } catch (NoSuchProductKeyException e) {
        throw new NoSuchProductKeyException(productKey);
      }
      Map<Product, Integer> productIngredients = new LinkedHashMap<Product, Integer>();
      for (String ingredientKey: ingredients.keySet()) {
        Product ingredient = getProduct(ingredientKey);
        productIngredients.put(ingredient, ingredients.get(ingredientKey));
      }
      Recipe recipe = new Recipe(productIngredients);
      Product product = new BreakdownProduct(recipe, alpha, productKey, stock);
      _products.put(productKey, product);
  }

  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount)
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      int currentDate = getDate();
      Sale sale;
      double transactionPrice = 0;
      if (product.getStock() < amount) {
        if (!product.hasRecipe()) {
          throw new NotEnoughStockException(productKey, amount, product.getStock());
        }
        BreakdownProduct breakdownProduct = (BreakdownProduct) product;
        // if product has a recipe, try to make a breakdown
        int neededAmount = amount - product.getStock();
        checkIngredientsStock(breakdownProduct, amount);
        transactionPrice = (breakdownProduct.getProductPrice() * breakdownProduct.getStock());
        double aggregationCost = makeAggregation(breakdownProduct, neededAmount) * (1 + breakdownProduct.getAggravationFactor());
        transactionPrice += aggregationCost * (neededAmount);
        Batch batch = new Batch(breakdownProduct, amount, breakdownProduct.getProductPrice(), partner);
        partner.addBatch(batch);
        sale = new Sale(_nextTransactionKey++, partner, breakdownProduct, currentDate, amount, transactionPrice, deadline);
        Double biggestKnownPrice = _biggestKnownPrices.get(breakdownProduct);
        Double smallestWarehousePrice = _smallestWarehousePrices.get(breakdownProduct);
        if (biggestKnownPrice == null) {
          _biggestKnownPrices.put(breakdownProduct, aggregationCost);
          _smallestWarehousePrices.put(breakdownProduct, aggregationCost);
        } else if (aggregationCost > biggestKnownPrice) {
          _biggestKnownPrices.put(breakdownProduct, aggregationCost);
        } else if (aggregationCost < smallestWarehousePrice) {
          _smallestWarehousePrices.put(breakdownProduct, aggregationCost);
        }
      } else {
        // if we get here, we have stock anyway, so it's a regular sale
        product.updateStock(-amount);
        Batch batch = new Batch(product, amount, product.getProductPrice(), partner);
        partner.addBatch(batch);
        sale = new Sale(_nextTransactionKey++, partner, product, currentDate, amount, product.getProductPrice() * amount, deadline);
      }
      // transaction "procedures"
      partner.addNewSaleOrBreakdown(sale);
      addTransaction(sale);
  }

  public void registerAcquisitionTransaction(String partnerKey, String productKey, double price, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      Acquisition acquisition = new Acquisition(_nextTransactionKey++, partner, product, getDate(), amount, price * amount);
      partner.addNewAcquisition(acquisition);
      product.updatePrice(price, _smallestWarehousePrices);
      product.updateStock(amount);
      Double biggestKnownPrice = _biggestKnownPrices.get(product);
      Double smallestWarehousePrice = _smallestWarehousePrices.get(product);
      if (biggestKnownPrice == null) {
        _biggestKnownPrices.put(product, price);
        _smallestWarehousePrices.put(product, price);
      } else if (price > biggestKnownPrice) {
        _biggestKnownPrices.put(product, price);
      } else if (price < smallestWarehousePrice) {
        _smallestWarehousePrices.put(product, price);
      }
      product.addBatch(new Batch(product, amount, price, partner));
      updateBalanceAcquisition(price * amount);
      addTransaction(acquisition);
  }

  /** Registers a breakdown transaction */
  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      if (product.getStock() < amount) {
        throw new NotEnoughStockException(productKey, amount, product.getStock());
      }
      if (!product.hasRecipe()) {
        return;
      }
      BreakdownProduct breakdownProduct = (BreakdownProduct) product;
      Recipe recipe = breakdownProduct.getRecipe();
      double productPrice = breakdownProduct.getStock() == 0 ? _biggestKnownPrices.get(product) : product.getProductPrice();
      productPrice *= amount;
      double transactionCost = breakdownProcedure(breakdownProduct, amount, productPrice);
      int currentDate = getDate();
      Breakdown breakdown = new Breakdown(_nextTransactionKey++, partner, breakdownProduct, currentDate, amount, transactionCost, currentDate, recipe);
      partner.addNewSaleOrBreakdown(breakdown);
      partner.getPartnerStatus().payTransaction(breakdown, currentDate);
      updateBalanceSale(transactionCost);
      breakdown.updatePaid(transactionCost);
      addTransaction(breakdown);
  }

  public double breakdownProcedure(BreakdownProduct product, int amount, double price)
    throws NoSuchProductKeyException {
    Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
    double ingredientsCost = 0;
    for (Product ingredient: ingredients.keySet()) {
      int ingredientAmount = ingredients.get(ingredient);
      int totalAmount = ingredientAmount * amount;
      ingredient.updateStock(totalAmount);
      ingredientsCost += ingredient.getProductPrice() * totalAmount;
    }
    product.updateStock(-amount);
    return price - ingredientsCost;
  }

  public void updateBalanceAcquisition(double money) {
    _availableBalance -= money;
  }

  public void updateBalanceSale(double money) {
    _availableBalance += money;
  }

  public void addProduct(Product p){
    _products.put(p.getProductKey(), p);
  }

  public void addTransaction(Transaction t) {
    _transactions.put(t.getTransactionKey(), t);
  }

  public void checkIngredientsStock(BreakdownProduct product, int amount)
    throws NotEnoughStockException, NoSuchProductKeyException {
      Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
      for (Product ingredient: ingredients.keySet()) {
        int ingredientAmount = ingredients.get(ingredient);
        int totalAmount = ingredientAmount * (amount - product.getStock());
        if (ingredient.getStock() < totalAmount) {
          if (ingredient.hasRecipe()) {
            BreakdownProduct breakdownProduct = (BreakdownProduct) ingredient;
            try {
              checkIngredientsStock(breakdownProduct, totalAmount);
            } catch (NotEnoughStockException e) {
              throw new NotEnoughStockException(breakdownProduct.getProductKey(), amount, breakdownProduct.getStock());
            }
          } else {
            throw new NotEnoughStockException(ingredient.getProductKey(), totalAmount, ingredient.getStock());
          }
        }
      }
  }

  public double makeAggregation(BreakdownProduct product, int amount)
    throws NotEnoughStockException, NoSuchProductKeyException {
      Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
      double cost = 0;
      for (Product ingredient: ingredients.keySet()) {
        String ingredientKey = ingredient.getProductKey();
        int ingredientAmount = ingredients.get(ingredient);
        int ingredientStock = ingredient.getStock();
        if (ingredientStock < ingredientAmount * amount) {
          // necessarily a breakdownProduct - checked in checkIngredientsStock
          BreakdownProduct breakdownProduct = (BreakdownProduct) getProduct(ingredientKey);
          cost += makeAggregation(breakdownProduct, ingredientAmount);
        } else {
          cost += ingredient.getProductPrice() * ingredientAmount;
          ingredient.updateStock(-(ingredientAmount * amount));
        }
      }
      product.updateStock(-amount);
      return cost;
  }

}
