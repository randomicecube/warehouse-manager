package ggc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
  private Double _productPrice = 0.0;

  /** Product's stock */
  private Integer _stock = 0; // Integer instead of int for consistency's sake with Maps

  /** Product's in-stock batches */
  private List<Batch> _productBatches = new ArrayList<Batch>();


  /**
   * Main Constructor
   * 
   * @param productKey   product's identification key
   * @param stock        product's stock
   * @param productPrice product's price
   */
  public Product(String productKey, Integer stock, Double productPrice) {
    _productKey = productKey;
    _productPrice = productPrice;
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
   * 
   * @param price
   */
  public void updatePrice(Double price) {
    _productPrice = price;
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
    _stock += stock;
  }

  /** @return product's identification key */
  public String getProductKey() {
    return _productKey;
  }

  /** @return product's pricey */
  public Double getProductPrice() {
    return _productPrice;
  }

  /** @return product's stock */
  public Integer getStock() {
    return _stock;
  }

  /** @return product's in-stock batches in toString format */
  public Collection<String> getBatchStrings() {
    
    List<Batch> sortedBatches = _productBatches;
    
    sortedBatches.sort(
      new Comparator<Batch>() {

        public int comparePartnerKeys(Batch b1, Batch b2) {
          String partnerKeyB1 = b1.getPartner().getPartnerKey();
          String partnerKeyB2 = b2.getPartner().getPartnerKey();
          return partnerKeyB1.compareTo(partnerKeyB2);
        }
        
        public int comparePrices(Batch b1, Batch b2) {
          return (int) (b1.getPrice() - b2.getPrice());
        }

        public int compareStocks(Batch b1, Batch b2) {
          return b1.getAmount() - b2.getAmount();
        }

        @Override
        public int compare(Batch b1, Batch b2) {
          int keyComparator = comparePartnerKeys(b1, b2);
          if (keyComparator != 0) {
            return keyComparator;
          }

          int priceComparator = comparePrices(b1, b2);
          if (priceComparator != 0) {
            return priceComparator;
          }

          return compareStocks(b1, b2);
        }

      }
    );

    return sortedBatches
      .stream()
      .map(batch -> batch.toString())
      .collect(Collectors.toList());

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
