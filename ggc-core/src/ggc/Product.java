package ggc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.text.Collator;
import java.util.Locale;

import java.util.Comparator;

/**
 * Class representing a Product in the system
 */
public class Product implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252057L;

  /** Product's identification key */
  private String _productKey;

  /** Product's price */
  private double _productPrice;

  /** Product's stock */
  private int _stock = 0; // Integer instead of int for consistency's sake with Maps

  /** Product's in-stock batches */
  private List<Batch> _productBatches = new ArrayList<Batch>();

  /** Stores partners' intent to receive notifications about the product
   *  true if they want to, false if they do not
   */
  private Map<Observer, Boolean> _notifiedPartners = new HashMap<Observer, Boolean>();


  /**
   * Main Constructor
   * 
   * @param productKey   product's identification key
   * @param stock        product's stock
   * @param productPrice product's price
   */
  public Product(String productKey, int stock, double productPrice) {
    _productKey = productKey;
    _productPrice = productPrice;
    _stock += stock;
  }

  public Product(String productKey, int stock) {
    _productKey = productKey;
    _stock = stock;
  }

  /**
   * Set product's identification key
   * 
   * @param productKey
   */
  public void setProductKey(String productKey) {
    _productKey = productKey;
  }

  /**
   * Update product's current price
   * Notify partners if price has changed
   * 
   * @param price
   */
  public void updatePrice(Double price, Map<Product, Double> smallestPrices) {
    Double smallestPrice = smallestPrices.get(this);
    if (smallestPrice == null || price < smallestPrice) {
      notifyPartners(
        new BargainNotification(getProductKey(), price)
      );
    }
    _productPrice = price;
  }

  public void setPrice(double price) {
    _productPrice = price;
  }

  public void partnerNowObserving(Observer partner) {
    _notifiedPartners.put(partner, true);
  }

  public void partnerNoLongerObserving(Observer partner) {
    _notifiedPartners.put(partner, false);
  }

  public void notifyPartners(Notification notification) {
    for (Observer partner : _notifiedPartners.keySet()) {
      if (_notifiedPartners.get(partner)) {
        partner.update(notification);
      }
    }
  }

  /**
   * A given batch is associated with a product - now literally
   * 
   * @param batch to-add batch
   */

  public void addBatch(Batch batch) {
    _productBatches.add(batch);
  }

  /**
   * Update product's current stock
   * 
   * @param stock
   */
  public void updateStock(Integer stock) {
    if (getStock() == 0) {
      notifyPartners(
        new NewNotification(getProductKey(), getProductPrice())
      );
    }
    _stock += stock;
  }

  public int getProductDeadlineDelta() {
    return 3; // 3 for "Simple" Products
  }

  /** @return product's identification key */
  public String getProductKey() {
    return _productKey;
  }

  /** @return product's pricey */
  public double getProductPrice() {
    return _productPrice;
  }

  public List<Batch> getBatches() {
    return _productBatches;
  }

  public List<Batch> getSortedBatches() {
    List<Batch> sortedBatches = getBatches();  
    sortedBatches.sort(new BatchComparatorByPartner());
    return sortedBatches;
  }

  /** @return product's stock */
  public int getStock() {
    return _stock;
  }

  /** @return product's in-stock batches in toString format */
  public Collection<String> getBatchStrings() {    
    return getSortedBatches()
      .stream()
      .map(batch -> batch.toString())
      .collect(Collectors.toList());
  }

  public Map<Observer, Boolean> getObservingPartners() {
    return _notifiedPartners;
  }

  public boolean hasRecipe() {
    return false;
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      getProductKey(),
      String.valueOf(Math.round(getProductPrice())),
      String.valueOf(getStock())
    );
  }
}
