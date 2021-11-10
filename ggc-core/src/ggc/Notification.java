package ggc;

import java.io.Serializable;

/**
 * Class representing a notification in the system has a product's key and price
 * associated, as well as the notification type (NEW vs BARGAIN)
 */
public abstract class Notification implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252059L;

  /** The product associated with the notifications's key */
  private String _productKey;

  /** The product associated with the notifications's price */
  private double _productPrice;

  /**
   * Main Constructor
   * 
   * @param productKey       associated product's key
   * @param productPrice     associated product's price
   * @param notificationType
   */
  public Notification(String productKey, double productPrice) {
    _productKey = productKey;
    _productPrice = productPrice;
  }

  /** @return product's key */
  public String getProductKey() {
    return _productKey;
  }

  /** @return product's price */
  public double getProductPrice() {
    return _productPrice;
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      getProductKey(),
      String.valueOf(Math.round(getProductPrice()))
    );
  }

}
