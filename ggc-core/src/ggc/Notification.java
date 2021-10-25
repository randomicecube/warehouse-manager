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
  private int _productPrice;

  /** The notification's type */
  private String _notificationType;

  /**
   * Main Constructor
   * 
   * @param productKey       associated product's key
   * @param productPrice     associated product's price
   * @param notificationType
   */
  public Notification(String productKey, int productPrice, String notificationType) {
    _productKey = productKey;
    _productPrice = productPrice;
    _notificationType = notificationType;
  }

  /** Updating the notification type (NEW vs BARGAIN) */
  public void updateNotificationType(String notificationType) {
    _notificationType = notificationType;
  }

  /** @return product's key */
  public String getProductKey() {
    return _productKey;
  }

  /** @return product's price */
  public int getProductPrice() {
    return _productPrice;
  }

  /** @return notification type */
  public String getNotificationType() {
    return _notificationType;
  }

  @Override
  public String toString() {
    return String.join("|", getNotificationType(), getProductKey(), String.valueOf(getProductPrice()));
  }

}
