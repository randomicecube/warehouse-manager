package ggc;

import java.io.Serializable;

/**
 * Class representing a Bargain Notification
 */
public class BargainNotification extends Notification implements Serializable {
  
  /**
   * Main Constructor
   * @param productKey
   * @param productPrice
   */
  public BargainNotification(String productKey, double productPrice) {
    super(productKey, productPrice);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      "BARGAIN",
      super.toString()
    );
  }

}