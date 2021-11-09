package ggc;

import java.io.Serializable;

public class StandardDelivery extends DeliveryStrategy implements Serializable {
  
  public void deliver(Partner partner, Notification notification) {
    partner.updateNotificationMethod(new StandardDelivery());
    partner.getNotifications().add(notification);
  }

}
