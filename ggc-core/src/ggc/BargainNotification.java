package ggc;

import java.io.Serializable;

public class BargainNotification extends Notification implements Serializable {
  
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