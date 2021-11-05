package ggc;

import java.io.Serializable;

public interface Observer extends Serializable {

  public void update(String productKey, double productPrice, String notificationType);

}
