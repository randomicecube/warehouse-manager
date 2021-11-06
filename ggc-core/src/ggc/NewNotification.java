package ggc;

import java.io.Serializable;

public class NewNotification extends Notification implements Serializable {
  
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
