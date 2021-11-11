package ggc;

import java.io.Serializable;

/**
 * A class representing a New Notification
 */
public class NewNotification extends Notification implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111111324L;
  
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
