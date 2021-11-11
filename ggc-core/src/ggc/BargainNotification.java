package ggc;

import java.io.Serializable;

/**
 * Class representing a Bargain Notification
 */
public class BargainNotification extends Notification implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111111329L;
  
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