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

  private Map<String, Product> _products = new TreeMap<String, Product>();
  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new TreeMap<String, Partner>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */   
  void importFile(String txtfile) throws IOException, BadEntryException, NoSuchPartnerKeyException, NoSuchProductKeyException /* TODO? - maybe add other exceptions */ {
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
      // TODO - FIX CATCHES
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    } catch (PartnerKeyAlreadyUsedException e) {
      e.printStackTrace();
      // TODO - change this to throwing an actual exception
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
      if (_partners.get(key) != null) {
        throw new PartnerKeyAlreadyUsedException(key);
      }      
      _partners.put(key, new Partner(key, name, address));
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

  /**
   * clear a partner's given unread notifications
   * @param key partner's key
   */
  public void clearNotifications(String key) {
    _partners.get(key).getNotifications().clear();
  }


  public void registerBatch(String productKey, String partnerKey, String price, String stock)
    throws NoSuchPartnerKeyException {
    // TODO - THROW EXCEPTIONS FOR WHEN PARTNER DOESNT EXIST
    
    Integer parsedStock = Integer.parseInt(stock);
    Integer parsedPrice = Integer.parseInt(price);

    Product product;

    if (getProducts().get(productKey) == null) {
      product = new Product(productKey, parsedStock, parsedPrice);
      _products.put(productKey, product);
    } else {
      product = getProducts().get(productKey);
      product.updateStock(parsedStock);

      if (parsedPrice > product.getProductPrice()) {
        product.updatePrice(parsedPrice);
      }

    }

    try {
      Partner partner = getPartner(partnerKey);
      partner.addBatch(new Batch(product, parsedStock, parsedPrice, partner));
    } catch (NoSuchPartnerKeyException e) {
      throw new NoSuchPartnerKeyException(e.getKey());
    }

  }

  public void registerBatch(String productKey, String partnerKey, String price,
                            String stock, String aggravationFactor, String recipe)
                            throws NoSuchProductKeyException, NoSuchPartnerKeyException {
    // TODO - THROW EXCEPTIONS FOR WHEN PARTNER DOESNT EXIST
    
    Integer parsedStock = Integer.parseInt(stock);
    Integer parsedPrice = Integer.parseInt(price);
    Double parsedAggravationFactor = Double.parseDouble(aggravationFactor);

    String[] ingredients = recipe.split("\\#");

    Recipe productRecipe = new Recipe();
    
    for (String i: ingredients) {

      String[] ingredientFactors = i.split(":");
      
      Product ingredient = getProducts().get(ingredientFactors[0]); 

      if (ingredient == null) {
        throw new NoSuchProductKeyException(ingredientFactors[0]);
      }

      productRecipe.addIngredient(ingredient, Integer.parseInt(ingredientFactors[1]));
    }

    Product batchProduct = _products.get(productKey);

    if (batchProduct == null) {
      batchProduct = new BreakdownProduct(productRecipe, parsedAggravationFactor, productKey, parsedStock, parsedPrice);
      _products.put(productKey, batchProduct);
    } else {
      batchProduct.updateStock(parsedStock);
    }

    try {
      Partner partner = getPartner(partnerKey);
      partner.addBatch(new Batch(batchProduct, parsedStock, parsedPrice, partner));
    } catch (NoSuchPartnerKeyException e) {
      throw new NoSuchPartnerKeyException(e.getKey());
    }

  }



  /**
   * GETTERS FOR COLLECTIONS OF PRODUCTS, PARTNERS, BATCHES, ETC 
   */



  

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _partners;
  }
  
  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    return getPartners()
      .values()
      .stream()
      .map(partner -> partner.toString())
      .collect(Collectors.toList());
  }
  
  public Map<String, Product> getProducts(){
    return _products;
  }

  public Collection<String> getProductsCollection(){
    return getProducts()
      .values()
      .stream()
      .map(product -> product.toString())
      .collect(Collectors.toList());
  }

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

}
