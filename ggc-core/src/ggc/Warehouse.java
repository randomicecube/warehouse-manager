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

  /** Warehouse's current accounting balance */
  private double _accountingBalance = 0;

  /** All products associated with the warehouse */
  private Map<String, Product> _products = new HashMap<String, Product>();

  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new HashMap<String, Partner>();

  private Map<Integer, Transaction> _transactions = new LinkedHashMap<Integer, Transaction>();

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
    return _accountingBalance;
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
      _partners.put(key, new Partner(key, name, address));
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

      try {
        product = getProduct(productKey);
        product.updateStock(parsedStock);
      } catch (NoSuchProductKeyException e) {
        product = new Product(productKey, parsedStock, parsedPrice);
        _products.put(productKey, product);
      }

      if (parsedPrice > product.getProductPrice()) {
        product.updatePrice(parsedPrice);
      }
      try {
        Partner partner = getPartner(partnerKey);
        Batch batch = new Batch(product, parsedStock, parsedPrice, partner);
        partner.addBatch(batch);
        product.addBatch(batch);
      } catch (NoSuchPartnerKeyException e) {
        throw new NoSuchPartnerKeyException(e.getKey());
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

      Product batchProduct;

      try {
        batchProduct = getProduct(productKey);
        batchProduct.updateStock(parsedStock);
      } catch (NoSuchProductKeyException e) {
        batchProduct = new BreakdownProduct(productRecipe, parsedAggravationFactor, 
                                            productKey, parsedStock, parsedPrice);
        _products.put(productKey, batchProduct);
      }

      if (parsedPrice > batchProduct.getProductPrice()) {
        batchProduct.updatePrice(parsedPrice);
      }

      try {
        Partner partner = getPartner(partnerKey);
        Batch batch = new Batch(batchProduct, parsedStock, parsedPrice, partner);
        partner.addBatch(batch);
        batchProduct.addBatch(batch);
      } catch (NoSuchPartnerKeyException e) {
        throw new NoSuchPartnerKeyException(e.getKey());
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

  public Collection<Notification> readPartnerNotifications(String key) 
    throws NoSuchPartnerKeyException {
      Partner partner = getPartner(key);
      Collection<Notification> notifications = partner.getNotifications();
      partner.getNotifications().clear();
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
   * @return a Collection with all the batches (in toStrinf form)
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
        stringedPartners.add(getPartner(partner).toString());
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

  public Collection<Sale> getPartnerSales(String partnerKey)
    throws NoSuchPartnerKeyException {
      return getPartner(partnerKey).getSales();
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
    return availableBatches
      .stream()
      .filter(batch -> batch.getPrice() < priceCap)
      .collect(Collectors.toList());    
  }

  public void receivePayment(int transactionKey)
    throws NoSuchTransactionKeyException {
      // TODO - missing updating points and stuff
      Sale sale = (Sale) getTransaction(transactionKey);
      if (sale.isPaid()) {
        return;
      }
      sale.updatePaid(); //TODO - here update stuff 
      sale.updatePaymentDate(_date);
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
      Recipe recipe = new Recipe(ingredients);
      Product product = new BreakdownProduct(recipe, alpha, productKey, stock);
      _products.put(productKey, product);
  }

  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount)
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      //baseprice??
      Sale sale = new Sale(_nextTransactionKey++, partner, product, getDate(), amount, basePrice, deadline);
      partner.addNewSale(sale);
      addTransaction(sale);
      partner.addProduct(product);
      // TODO implement
      // Don't forget -> Sale and Transaction need to account for
      // creating new BreakdownProducts (and their prices)
  }

  public void registerAcquisitionTransaction(String partnerKey, String productKey, double price, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      Acquisition acquisition = new Acquisition(_nextTransactionKey++, partner, product, getDate(), amount, price);
      partner.addNewAcquisition(acquisition);
      product.updatePrice(price);
      updateBalanceAcquisition(price);
      addTransaction(acquisition);
  }

  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      Partner partner = getPartner(partnerKey);
      Product product = getProduct(productKey);
      Breakdown breakdown = new Breakdown(_nextTransactionKey++, partner, product, getDate(), amount, basePrice, getDate(), recipe);
      partner.addNewTransation(breakdown); //?
      addTransaction(breakdown);
      // TODO implement
  }

  public void updateBalanceAcquisition(double money){
    _availableBalance -= money;
    _accountingBalance -= money;
  }

  public void updateBalanceSale(double money){
    _availableBalance += money;
    _accountingBalance  += money;
  }

  public void addProduct(Product p){
    _products.put(p.getProductKey(), p);
  }

  public void addTransaction(Transaction t) {
    _transactions.put(t.getTransactionKey(), t);
  }

}
