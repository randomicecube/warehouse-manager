package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.HashMap;
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

  /** Warehouse's current available balance */
  private double _availableBalance = 0;

  /** Warehouse's current accounting balance */
  private double _accountingBalance = 0;

  /** All products associated with the warehouse */
  private Map<String, Product> _products = new HashMap<String, Product>();

  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new HashMap<String, Partner>();

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

          case "BATCH_M" -> registerBatch(fields[1],
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

  /**
   * clear a partner's given unread notifications
   * 
   * @param key partner's key
   */
  public void clearNotifications(String key) throws NoSuchPartnerKeyException {
    try {
      getPartner(key).getNotifications().clear();
    } catch (NoSuchPartnerKeyException e) {
      throw new NoSuchPartnerKeyException(key);
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
  public void registerPartner(String key, String name, String address) throws PartnerKeyAlreadyUsedException {
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
  public void registerBatch(String productKey, String partnerKey, String price, String stock)
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
      partner.addBatch(new Batch(product, parsedStock, parsedPrice, partner));
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
  public void registerBatch(String productKey, String partnerKey, String price, String stock, String aggravationFactor,
      String recipe) throws NoSuchProductKeyException, NoSuchPartnerKeyException {

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
      batchProduct = new BreakdownProduct(productRecipe, parsedAggravationFactor, productKey, parsedStock, parsedPrice);
      _products.put(productKey, batchProduct);
    }

    try {
      Partner partner = getPartner(partnerKey);
      partner.addBatch(new Batch(batchProduct, parsedStock, parsedPrice, partner));
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
    List<String> products = getProducts()
      .values()
      .stream()
      .map(product -> product.toString())
      .collect(Collectors.toList());

    Collections.sort(products, Collator.getInstance(Locale.getDefault()));

    return products;

  }

  /**
   * @return a Collection with all the batches with partners associated with the
   *         warehouse
   */
  public Collection<String> getBatchesCollection() {
    ArrayList<List<Batch>> partnerBatchesCollection = new ArrayList<List<Batch>>();
    List<String> batchCollection = new ArrayList<String>();

    for (Partner p : getPartners().values()) {
      partnerBatchesCollection.add(p.getPartnerBatches());
    }

    for (List<Batch> array : partnerBatchesCollection) {
      for (Batch b : array) {
        batchCollection.add(b.toString());
      }
    }

    Collections.sort(batchCollection, Collator.getInstance(Locale.getDefault()));

    return batchCollection;
  }

  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    List<String> partners = getPartners()
      .values()
      .stream()
      .map(partner -> partner.toString())
      .collect(Collectors.toList());

    Collections.sort(partners, Collator.getInstance(Locale.getDefault()));

    return partners;
  }

}
