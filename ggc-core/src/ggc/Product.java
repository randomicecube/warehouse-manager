package ggc;

import java.io.Serializable;

/**
 * Class representing a Product in the system
 */
public class Product implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252057L;

  /** Product's identification key */
  private String _productKey;

  /** Product's price */
  private Integer _productPrice = 0;

  /** Product's stock */
  private Integer _stock = 0; // Integer instead of int for consistency's sake with Maps

  /**
   * Main Constructor
   * 
   * @param productKey   product's identification key
   * @param stock        product's stock
   * @param productPrice product's price
   */
  public Product(String productKey, Integer stock, Integer productPrice) {
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
  public void updatePrice(Integer price) {
    _productPrice = price;
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
  public Integer getProductPrice() {
    return _productPrice;
  }

  /** @return product's stock */
  public Integer getStock() {
    return _stock;
  }

  @Override
  public String toString() {
    return String.join("|", getProductKey(), String.valueOf(getProductPrice()), String.valueOf(getStock()));
  }
}
