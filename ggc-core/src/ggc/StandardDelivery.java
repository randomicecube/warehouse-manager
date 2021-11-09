package ggc;

import java.io.Serializable;

public class StandardDelivery implements Serializable {
  
  public Notification deliver(Notification notification, List<Notification> unread) {
    unread.add(notification);
  }

}
