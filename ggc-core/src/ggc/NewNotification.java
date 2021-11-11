package ggc;

import java.io.Serializable;

/**
 * A class representing a New Notification
 */
public class NewNotification extends Notification implements Serializable {
  
  /**
   * Main Constructor
   * 
   * @param productKey
   * @param productPrice
   */
  public NewNotification(String productKey, double productPrice) {
    super(productKey, productPrice);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      "NEW",
      super.toString()
    );
  }

}
