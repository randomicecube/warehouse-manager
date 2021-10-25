package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import java.text.Normalizer;
import java.text.Normalizer.Form;

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
  private Map<String, Product> _products = new TreeMap<String, Product>();
  
  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new TreeMap<String, Partner>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */   
  void importFile(String txtfile) throws IOException, BadEntryException, NoSuchPartnerKeyException, NoSuchProductKeyException  {
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

  /**
   * Aux function that returns a version of a key in all caps, without the special characters 
   * for example: água -> AGUA
   * @param key
   * @return
   */
  public String normalizeKeys(String key) {
    String processedKey = Normalizer.normalize(key, Form.NFD);
    processedKey = processedKey.replaceAll("\\p{M}", "");
    processedKey = processedKey.toUpperCase();
    return processedKey;
  }

  /**
   * clear a partner's given unread notifications
   * @param key partner's key
   */
  public void clearNotifications(String key) throws NoSuchPartnerKeyException{
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
   * @param key partner's key
   * @param name partner's name
   * @param address partner's address
   * @throws PartnerKeyAlreadyUsedException
   */
  public void registerPartner(String key, String name, String address)
    throws PartnerKeyAlreadyUsedException {
      String processedPartnerKey = normalizeKeys(key);
      if (_products.get(processedPartnerKey) != null) {
        throw new PartnerKeyAlreadyUsedException(key);
      }
      _partners.put(processedPartnerKey, new Partner(key, name, address));
  }


  /**
   * registers a batch to a given partner - applies to simple products
   * @param productKey
   * @param partnerKey
   * @param price
   * @param stock
   * @throws NoSuchPartnerKeyException
   */
  public void registerBatch(String productKey, String partnerKey, String price, String stock)
    throws NoSuchPartnerKeyException {
    
    Integer parsedStock = Integer.parseInt(stock);
    Integer parsedPrice = Integer.parseInt(price);

    Product product;

    String processedProductKey = normalizeKeys(productKey);

    try {
      product = getProduct(processedProductKey);
      product.updateStock(parsedStock);
    } catch (NoSuchProductKeyException e) {
      product = new Product(productKey, parsedStock, parsedPrice);
      _products.put(processedProductKey, product);

      if (parsedPrice > product.getProductPrice()) {
        product.updatePrice(parsedPrice);
      }
    }

    String processedPartnerKey = normalizeKeys(partnerKey);

    try {
      Partner partner = getPartner(processedPartnerKey);
      partner.addBatch(new Batch(product, parsedStock, parsedPrice, partner));
    } catch (NoSuchPartnerKeyException e) {
      throw new NoSuchPartnerKeyException(e.getKey());
    }

  }

  /**
   * registers a batch to a given partner - applies to breakdown products
   * @param productKey
   * @param partnerKey
   * @param price
   * @param stock
   * @param aggravationFactor
   * @param recipe
   * @throws NoSuchProductKeyException
   * @throws NoSuchPartnerKeyException
   */
  public void registerBatch(String productKey, String partnerKey, String price,
                            String stock, String aggravationFactor, String recipe)
                            throws NoSuchProductKeyException, NoSuchPartnerKeyException {
    
    Integer parsedStock = Integer.parseInt(stock);
    Integer parsedPrice = Integer.parseInt(price);
    Double parsedAggravationFactor = Double.parseDouble(aggravationFactor);

    String[] ingredients = recipe.split("\\#");

    Recipe productRecipe = new Recipe();
    
    for (String i: ingredients) {
      String[] ingredientFactors = i.split(":");

      String processedIngredientKey = normalizeKeys(ingredientFactors[0]);

      try {
        Product ingredient = getProduct(processedIngredientKey);
        productRecipe.addIngredient(ingredient, Integer.parseInt(ingredientFactors[1]));
      } catch (NoSuchProductKeyException e) {
        throw new NoSuchProductKeyException(ingredientFactors[0]);
      }
    }

    Product batchProduct;

    String processedProductKey = normalizeKeys(productKey);

    try {
      batchProduct = getProduct(processedProductKey);
      batchProduct.updateStock(parsedStock);
    } catch (NoSuchProductKeyException e) {
      batchProduct = new BreakdownProduct(productRecipe, parsedAggravationFactor, productKey, parsedStock, parsedPrice);
      _products.put(processedProductKey, batchProduct);
    }

    String processedPartnerKey = normalizeKeys(partnerKey);

    try {
      Partner partner = getPartner(processedPartnerKey);
      partner.addBatch(new Batch(batchProduct, parsedStock, parsedPrice, partner));
    } catch (NoSuchPartnerKeyException e) {
      throw new NoSuchPartnerKeyException(e.getKey());
    }

  }



  /**
   * GETTERS FOR PRODUCTS, PARTNERS, BATCHES, ETC 
   */

  /**
   * get a product, given their key
   * @param key
   * @return desired product
   * @throws NoSuchProductKeyException
   */
   public Product getProduct(String key) throws NoSuchProductKeyException {
    Product product = _products.get(key); 
    if (product == null) {
      throw new NoSuchProductKeyException(key);
    }    
    return product;
   }


  /**
   * get a partner, given their key
   * @param key
   * @return desired partner
   * @throws NoSuchPartnerKeyException
   */
  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    Partner partner = _partners.get(key);
    if (partner == null) {
      throw new NoSuchPartnerKeyException(key);
    }
    return partner;
  }

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _partners;
  }

  /** @return a Collection with all the products associated with the warehouse */
  public Collection<String> getProductsCollection(){
    return getProducts()
      .values()
      .stream()
      .map(product -> product.toString())
      .collect(Collectors.toList());
  }

  /** @return a Collection with all the batches with partners associated with the warehouse */
  public Collection<String> getBatchesCollection(){
    ArrayList<List<Batch>> partnerBatchesCollection = new ArrayList<List<Batch>>();
    List<String> batchCollection = new ArrayList<String>();

    for (Partner p: getPartners().values()) {
      partnerBatchesCollection.add(p.getPartnerBatches());
    }

    for (List<Batch> array: partnerBatchesCollection) {
      for (Batch b: array) {
        batchCollection.add(b.toString());
      }
    }

    batchCollection.sort(null);

    return batchCollection;
  }

  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    return getPartners()
      .values()
      .stream()
      .map(partner -> partner.toString())
      .collect(Collectors.toList());
  }
  
  /** @return all products associated with the warehouse */
  public Map<String, Product> getProducts(){
    return _products;
  }

}
