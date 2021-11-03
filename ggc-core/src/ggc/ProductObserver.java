package ggc;

import java.io.Serializable;

public interface ProductObserver extends Serializable {

  public void update(String productKey, int productPrice, String notificationType);

}
