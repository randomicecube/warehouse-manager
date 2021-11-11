package ggc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.stream.Collectors;

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
  private PriorityQueue<Batch> _productBatches = new PriorityQueue<Batch>(new BatchComparator());

  private PriorityQueue<Batch> _batchesInWarehouse = new PriorityQueue<Batch>(new BatchComparator());

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

  /**
   * Secondary constructor when there's no price associated with the product
   * @param productKey
   * @param stock
   */
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

  /** Sets a product's price
   * 
   * @param price
   */
  public void setPrice(double price) {
    _productPrice = price;
  }

  /** Puts an observer on the notified partners map
   * 
   * @param partner
   */
  public void partnerNowObserving(Observer partner) {
    _notifiedPartners.put(partner, true);
  }

  /** Removes an observer from the notified partners map
   * 
   * @param partner
   */
  public void partnerNoLongerObserving(Observer partner) {
    _notifiedPartners.put(partner, false);
  }

  /** Notifies partners of a product's action
   * 
   * @param notification
   */
  public void notifyPartners(Notification notification) {
    for (Observer partner : _notifiedPartners.keySet()) {
      if (_notifiedPartners.get(partner)) {
        partner.update(notification);
      }
    }
  }

  /**
   * Adds a batch to the product's overall batches list
   * 
   * @param batch to-add batch
   */

  public void addBatch(Batch batch) {
    _productBatches.add(batch);
  }

  /** Removes a batch from the product's overall batches list
   * 
   * @param batch
   */
  public void removeBatch(Batch batch) {
    _productBatches.remove(batch);
  }

  /** Adds a batch to the product's batches in warehouse
   * 
   * @param batch
   */
  public void addWarehouseBatch(Batch batch) {
    _batchesInWarehouse.add(batch);
    addBatch(batch);
  }

  /** Removes a batch from the product's batches in warehouse
   * 
   * @param batch
   */
  public void removeWarehouseBatch(Batch batch) {
    _batchesInWarehouse.remove(batch);
    removeBatch(batch);
  }

  /** Update product's current stock
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

  /** Get the transaction's "N"
   * 
   * @return
   */
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

  /** @return product's aggravation factor - 0 for "simple" products */
  public double getAggravationFactor() {
    return 0.0;
  }

  /** @return product's recipe - an empty one for "simple" products */
  public Recipe getRecipe() {
    return new Recipe(new HashMap<Product, Integer>());
  }

  /**
   * 
   * @return productBatches
   */
  public PriorityQueue<Batch> getBatches() {
    return _productBatches;
  }

  /**
   * 
   * @return batches in the Warehouse
   */
  public PriorityQueue<Batch> getWarehouseBatches() {
    return _batchesInWarehouse;
  }

  /** @return product's stock */
  public int getStock() {
    return _stock;
  }

  /** @return product's in-stock batches (sorted) */
  public List<Batch> getBatchesSorted() {    
    List<Batch> batches = getBatches()
      .stream()
      .collect(Collectors.toList());
    
    batches.sort(new BatchComparatorByPartner());

    return batches;
  }

  /**
   * 
   * @return notified Partners
   */
  public Map<Observer, Boolean> getObservingPartners() {
    return _notifiedPartners;
  }

  /** @return whether the product has a recipe or not */
  public boolean hasRecipe() {
    return !getRecipe().getIngredients().isEmpty();
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
