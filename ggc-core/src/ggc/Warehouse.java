package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

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

  /**The next transaction key */
  private int _nextTransactionKey = 0;

  /** Warehouse's current available balance */
  private double _availableBalance = 0;

  /** All products associated with the warehouse */
  private Map<String, Product> _products = new HashMap<String, Product>();

  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new HashMap<String, Partner>();

  /** All transactions associated with warehouse */
  private Map<Integer, Transaction> _transactions = new LinkedHashMap<Integer, Transaction>();

  /** Biggest price for each product in the warehouse history */
  private Map<Product, Double> _biggestKnownPrices = new HashMap<Product, Double>();

  /** Smallest price for each product in the warehouse history */
  private Map<Product, Double> _smallestWarehousePrices = new HashMap<Product, Double>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException,
    NoSuchPartnerKeyException, NoSuchProductKeyException {
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

  /** #######################
   *   DATE RELATED METHODS
   * #######################
  */

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


  /** ##########################
   *   BALANCE RELATED METHODS
   * ##########################
  */

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
   * Updates balance due to an acquisition
   * 
   * @param money amount of money to be added from the balance
   */
  public void updateBalanceAcquisition(double money) {
    _availableBalance -= money;
  }

  /**
   * Updates balance due to a sale
   * 
   * @param money amount of money to be subtracted from the balance
   */
  public void updateBalanceSaleOrBreakdown(double money) {
    _availableBalance += money;
  }

  /** ##########################
   *   PARTNER RELATED METHODS
   * ##########################
  */

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

  /**
   * get a Partner in their String form, given their key
   * 
   * @param key partner's key
   * @return partner in String form
   * @throws NoSuchPartnerKeyException
   */
  public String getPartnerString(String key) throws NoSuchPartnerKeyException {
    return getPartner(key).partnerStringed(getDate());
  }

  /**
   * reads partner's notifications
   * 
   * @param key partner's key
   * @return partner's to-be-read notifications
   * @throws NoSuchPartnerKeyException
   */
  public Collection<Notification> readPartnerNotifications(String key) 
    throws NoSuchPartnerKeyException {
      Partner partner = getPartner(key);
      List<Notification> notifications = new ArrayList<Notification>(
        partner.getNotifications()
      );
      partner.clearNotifications();
      return notifications;
  }

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _partners;
  }

  /** @return a Collection with all partners associated with the warehouse
   * sorted by their natural order
   */
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

  /** ####################################
   *   PRODUCT AND BATCH RELATED METHODS
   * ####################################
  */

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

      if (parsedPrice < product.getProductPrice()) {
        product.updatePrice(parsedPrice, _smallestWarehousePrices);
      }

      updateSmallestBiggestPrices(product, parsedPrice);

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

      if (parsedPrice < batchProduct.getProductPrice()) {
        batchProduct.updatePrice(parsedPrice, _smallestWarehousePrices);
      }

      updateSmallestBiggestPrices(batchProduct, parsedPrice);

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

  /** @return all batches associated with everyone, warehouse or partners */
  public Collection<Batch> getBatches() {
    List<Product> products = getProducts()
      .values()
      .stream()
      .collect(Collectors.toList());

    List<Batch> batches = new ArrayList<Batch>();

    for (Product product: products) {
      if (product.getStock() != 0) {
        for (Batch b: product.getBatches()) {
          batches.add(b);
        }
      }
    }

    return batches;
  }

  /** @return a Collection with all the batches (in toString form) 
   * sorted by their natural order 
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
        for (Batch batch: product.getBatchesSorted()) {
          batches.add(batch.toString());
        }
      } catch (NoSuchProductKeyException e) {
        // will never happen
        e.printStackTrace();
      }
    }

    return batches;
  }

  /** 
   * @param partnerKey partner's key
   * @return a Collection with all batches associated with a given partner,
   * sorted by their natural order  
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
   * @return a Collection with all batches associated with a given product,
   * accounting for stock restraints 
   */
  public Collection<String> getBatchesByProduct(String productKey)
    throws NoSuchProductKeyException {
      List<String> availableInStockBatches = new ArrayList<String>();
      List<Batch> productBatches = getProduct(productKey).getBatchesSorted();
      int stock = getProduct(productKey).getStock();
      for (int i = 0; stock > 0; i++) {
        availableInStockBatches.add(productBatches.get(i).toString());
        stock -= productBatches.get(i).getAmount();
      }
      return availableInStockBatches;
  }

  /**
   * Gets every batch under a given price
   * 
   * @param priceCap maximum price
   * @return available Batches
   */
  public Collection<Batch> getProductBatchesUnderGivenPrice(double priceCap) {
    List<Batch> availableBatches = getBatches()
      .stream()
      .filter(
        batch ->
        batch.getPrice() < priceCap &&
        batch.getProductType().getStock() > 0
      )
      .collect(Collectors.toList());
    
    availableBatches.sort(new BatchComparatorByProduct());

    return availableBatches; 
  }

  /**
   * Registers a (simple) product in the warehouse's history
   * 
   * @param productKey product's key
   */
  public void registerProduct(String productKey) {
    Product product = new Product(productKey, 0);
    _products.put(productKey, product);
  }

  /**
   * Registers a Breakdown product in the warehouse's history
   * @param productKey product's key
   * @param ingredients product's ingredients
   * @param alpha product's aggravation factor
   * @throws NoSuchProductKeyException
   */
  public void registerProduct(String productKey, Map<String, Integer> ingredients, 
    double alpha) throws NoSuchProductKeyException {
      // check if all ingredients are registered
      try {
        for (String key: ingredients.keySet()) {
          getProduct(key);
        }
      } catch (NoSuchProductKeyException e) {
        throw new NoSuchProductKeyException(e.getKey());
      }
      Map<Product, Integer> productIngredients = new LinkedHashMap<Product, Integer>();
      for (String ingredientKey: ingredients.keySet()) {
        Product ingredient = getProduct(ingredientKey);
        productIngredients.put(ingredient, ingredients.get(ingredientKey));
      }
      Recipe recipe = new Recipe(productIngredients);
      Product product = new BreakdownProduct(recipe, alpha, productKey, 0);
      _products.put(productKey, product);
  }

  /**
   * Toggles partner's notifications for a specific product
   * @param partnerKey partner's key
   * @param productKey product's key
   * @throws NoSuchProductKeyException
   * @throws NoSuchPartnerKeyException
   */
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

  /**
   * Updates biggest/smallest price of a product in the warehouse's history
   * 
   * @param product product to be updated
   * @param price price to be updated
   */
  public void updateSmallestBiggestPrices(Product product, double price) {
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
  }

  /** ##############################
   *   TRANSACTION RELATED METHODS
   * ##############################
  */

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

  /**
   * Gets a given partner's list of acquisition Transactions
   * 
   * @param partnerKey partner's key
   * @return partner's acquisitions
   * @throws NoSuchPartnerKeyException
   */
  public Collection<Acquisition> getPartnerAcquisitions(String partnerKey)
    throws NoSuchPartnerKeyException {
      List<Acquisition> acquisitions = new ArrayList<Acquisition>(
        getPartner(partnerKey).getAcquisitions()
      );
      return acquisitions;
  }

  /**
   * Gets a given partner's list of sale and breakdown Transactions
   * 
   * @param partnerKey partner's key
   * @return partner's sales and breakdowns
   * @throws NoSuchPartnerKeyException
   */
  public Collection<Transaction> getPartnerSales(String partnerKey)
    throws NoSuchPartnerKeyException {
      Collection<Transaction> sales = new ArrayList<Transaction>(
        getPartner(partnerKey).getSales()
      );
      int currentDate = getDate();
      for (Transaction sale: sales) {
        sale.updateActualPrice(currentDate);
      }
      return sales;
  }

  /**
   * Gets a given partner's list of all paid sales and breakdowns
   * 
   * @param partnerKey partner's key
   * @return partner's paid transactions
   * @throws NoSuchPartnerKeyException
   */
  public Collection<Transaction> getPaymentsByPartner(String partnerKey)
    throws NoSuchPartnerKeyException {      
      return getPartnerSales(partnerKey)
        .stream()
        .filter(sale -> sale.isPaid())
        .collect(Collectors.toList());
  }

  /**
   * Receives a payment for a sale
   * 
   * @param transactionKey transaction's key
   * @throws NoSuchTransactionKeyException
   */
  public void receivePayment(int transactionKey)
    throws NoSuchTransactionKeyException {
      Transaction sale = getTransaction(transactionKey);
      TransactionChecker checker = new BreakdownChecker();
      if (sale.isPaid() && !sale.accept(checker)) {
        return;
      }
      int currentDate = getDate();
      sale.updatePaymentDate(currentDate);
      if (!sale.accept(checker)) {
        sale.updateActualPrice(currentDate);
      }
      double pricePaid = sale.getActualPrice();
      if (!sale.isPaid()) {
        sale.updatePaid(pricePaid);
        sale.getPartner().getPartnerStatus().payTransaction(sale, currentDate);
        updateBalanceSaleOrBreakdown(pricePaid);
      }
  }

  /**
   * Registers a Sale Transaction
   * 
   * @param partnerKey partner's key
   * @param deadline transaction's deadline
   * @param productKey product's key
   * @param amount amount of product to be sold
   * @throws NoSuchPartnerKeyException
   * @throws NoSuchProductKeyException
   * @throws NotEnoughStockException
   */
  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount)
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      int currentDate = getDate();
      Sale sale;
      double transactionPrice = 0;
      BreakdownProductChecker checker = new BreakdownProductChecker();
      if (product.getStock() < amount) {
        if (!product.accept(checker)) {
          throw new NotEnoughStockException(productKey, amount, product.getStock());
        }
        // if product has a recipe, try to make a breakdown
        int neededAmount = amount - product.getStock();
        checkIngredientsStock(product, amount, new HashMap<Product, Integer>());
        transactionPrice = (product.getProductPrice() * product.getStock());
        double aggregationCost = 
          makeAggregation(product, neededAmount) * (1 + product.getAggravationFactor());
        transactionPrice += aggregationCost * neededAmount;
        Batch batch = new Batch(product, amount, product.getProductPrice(), partner);
        partner.addBatch(batch);
        sale = new Sale(_nextTransactionKey++, partner, product, currentDate,
          amount, transactionPrice, deadline
        );
        updateSmallestBiggestPrices(product, aggregationCost);
      } else {
        // if we get here, we have stock anyway, so it's a regular sale
        product.updateStock(-amount);
        Batch batch = new Batch(product, amount, product.getProductPrice(), partner);
        partner.addBatch(batch);
        sale = new Sale(_nextTransactionKey++, partner, product, currentDate,
          amount, product.getProductPrice() * amount, deadline
        );
      }
      // transaction "procedures"
      removeOutOfStockWarehouseBatches(product, amount);
      partner.addNewSaleOrBreakdown(sale);
      addTransactionToWarehouse(sale);
  }

  /**
   * Registers a Acquisition transaction
   * 
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param price product's price
   * @param amount amount of product to be acquired
   * @throws NoSuchPartnerKeyException
   * @throws NoSuchProductKeyException
   */
  public void registerAcquisitionTransaction(String partnerKey, String productKey,
    double price, int amount) throws NoSuchPartnerKeyException, NoSuchProductKeyException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      Acquisition acquisition = new Acquisition(_nextTransactionKey++, partner, product,
        getDate(), amount, price * amount
      );
      partner.addNewAcquisition(acquisition);
      product.updatePrice(price, _smallestWarehousePrices);
      product.updateStock(amount);
      updateSmallestBiggestPrices(product, price);
      Batch batch = new Batch(product, amount, price, partner);
      product.addWarehouseBatch(batch);
      updateBalanceAcquisition(price * amount);
      addTransactionToWarehouse(acquisition);
      removeOutOfStockPartnerBatches(partner, product, amount);
  }

  /**
   * Registers a breakdown transaction
   * 
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param amount amount of product to be broken down
   * @throws NoSuchPartnerKeyException
   * @throws NoSuchProductKeyException
   * @throws NotEnoughStockException
   */
  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      BreakdownProductChecker checker = new BreakdownProductChecker();
      if (product.getStock() < amount) {
        throw new NotEnoughStockException(productKey, amount, product.getStock());
      }
      if (!product.accept(checker)) {
        return;
      }
      Recipe recipe = product.getRecipe();
      double productPrice = product.getStock() == 0 ? 
        _biggestKnownPrices.get(product) : product.getProductPrice();
      productPrice *= amount;
      double transactionCost = breakdownProcedure(product, amount, productPrice);
      int currentDate = getDate();
      Breakdown breakdown = new Breakdown(_nextTransactionKey++, partner, product, currentDate,
        amount, transactionCost, currentDate, recipe
      );
      partner.addNewSaleOrBreakdown(breakdown);
      addTransactionToWarehouse(breakdown);
      try {
        receivePayment(_nextTransactionKey - 1);
      } catch (NoSuchTransactionKeyException e) {
        // will never happen
        e.printStackTrace();
      }
  }

  /**
   * Makes a breakdown
   * 
   * @param product product to be broken down
   * @param amount amount of product to be broken down
   * @param price product's price
   * @return the breakdown's cost
   * @throws NoSuchProductKeyException
   */
  public double breakdownProcedure(Product product, int amount, double price)
    throws NoSuchProductKeyException {
    Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
    double ingredientsCost = 0;
    for (Product ingredient: ingredients.keySet()) {
      int ingredientAmount = ingredients.get(ingredient);
      int totalAmount = ingredientAmount * amount;
      ingredient.updateStock(totalAmount);
      ingredientsCost += ingredient.getProductPrice() * totalAmount;
      removeOutOfStockWarehouseBatches(ingredient, totalAmount);
    }
    product.updateStock(-amount);
    removeOutOfStockWarehouseBatches(product, amount);
    return price - ingredientsCost;
  }

  /**
   * Adds a transaction to the warehouse
   * 
   * @param transaction transaction to be added
   */
  public void addTransactionToWarehouse(Transaction transaction) {
    _transactions.put(transaction.getTransactionKey(), transaction);
  }

  /**
   * Recursive function to check if there's enough ingredients to do an aggregation
   * @param product product to be aggregated
   * @param amount amount of product to be aggregated
   * @throws NotEnoughStockException
   * @throws NoSuchProductKeyException
   */
  public void checkIngredientsStock(Product product, int amount, Map<Product, Integer> accountedFor)
    throws NotEnoughStockException, NoSuchProductKeyException {
      Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
      BreakdownProductChecker checker = new BreakdownProductChecker();
      for (Product ingredient: ingredients.keySet()) {
        int ingredientAmount = ingredients.get(ingredient);
        int totalAmount = ingredientAmount * (amount - product.getStock());
        Integer accountedAmount = accountedFor.get(ingredient);
        accountedAmount = accountedAmount == null ? 0 : accountedAmount;
        if (ingredient.getStock() < totalAmount + accountedAmount) {
          if (ingredient.accept(checker)) {
            checkIngredientsStock(ingredient, totalAmount, accountedFor);
          } else {
            throw new NotEnoughStockException(
              ingredient.getProductKey(),
              totalAmount + accountedAmount,
              ingredient.getStock()
            );
          }
        }
        accountedFor.put(ingredient, accountedAmount + totalAmount);
      }
  }

  /**
   * Make an aggregation
   * @param product product to be aggregated
   * @param amount amount of product to be aggregated
   * @return aggregation's cost
   * @throws NotEnoughStockException
   * @throws NoSuchProductKeyException
   */
  public double makeAggregation(Product product, int amount)
    throws NotEnoughStockException, NoSuchProductKeyException {
      Map<Product, Integer> ingredients = product.getRecipe().getIngredients();
      double cost = 0;
      for (Product ingredient: ingredients.keySet()) {
        String ingredientKey = ingredient.getProductKey();
        int ingredientAmount = ingredients.get(ingredient);
        int ingredientStock = ingredient.getStock();
        if (ingredientStock < ingredientAmount * amount) {
          // necessarily a breakdownProduct - checked in checkIngredientsStock
          Product breakdownProduct = getProduct(ingredientKey);
          cost += makeAggregation(breakdownProduct, ingredientAmount);
        } else {
          cost += ingredient.getProductPrice() * ingredientAmount;
          ingredient.updateStock(-(ingredientAmount * amount));
        }
      }
      product.updateStock(-amount);
      return cost;
  }

  /**
   * Removes out of stock warehouse batches
   * 
   * @param product product to be checked
   * @param amount amount of product to be checked
   */
  public void removeOutOfStockWarehouseBatches(Product product, int amount) {
    PriorityQueue<Batch> batches = product.getWarehouseBatches();
    int left = amount;
    for (Batch batch: batches) {
      if (batch.getAmount() > left) {
        batch.updateAmount(-left);
        return;
      } else {
        left -= batch.getAmount();
        product.removeWarehouseBatch(batch);
      }
    }
  }

  /**
   * Removes out of stock partner's batches
   * 
   * @param partner partner to be checked
   * @param product product to be checked
   * @param amount amount of product to be checked
   */
  public void removeOutOfStockPartnerBatches(Partner partner, Product product, int amount) {
    PriorityQueue<Batch> batches = partner.getPartnerBatches();
    List<Batch> toRemove = new ArrayList<Batch>();
    int left = amount;
    for (Batch batch: batches) {
      if (batch.getAmount() > left) {
        return;
      } else {
        left -= batch.getAmount();
        toRemove.add(batch);
      }
    }
    for (Batch batch: toRemove) {
      partner.removeBatch(batch);
      product.removeBatch(batch);
    }
  }

}
