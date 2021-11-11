package ggc;

import java.io.Serializable;

/**
 * A class representing Standard delivery method
 */
public class StandardDelivery extends DeliveryStrategy implements Serializable {
  
  /**
   * Deliver default notification
   */
  public void deliver(Partner partner, Notification notification) {
    partner.updateNotificationMethod(new StandardDelivery());
    partner.getNotifications().add(notification);
  }

}
