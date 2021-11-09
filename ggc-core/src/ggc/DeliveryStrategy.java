package ggc;

import java.io.Serializable;

public abstract class DeliveryStrategy implements Serializable {
  
  public abstract void deliver(Partner partner, Notification notification);

}
