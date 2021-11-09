package ggc;

import java.io.Serializable;
import java.util.List;

public abstract class DeliveryStrategy implements Serializable {
  
  public abstract Notification deliver(Notification notification, List<Notification> unread);

}
