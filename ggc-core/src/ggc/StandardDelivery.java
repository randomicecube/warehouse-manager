package ggc;

import java.io.Serializable;

/**
 * A class representing Standard delivery method
 */
public class StandardDelivery extends DeliveryStrategy implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 20211111132L;
  
  /**
   * Deliver default notification
   */
  public void deliver(Partner partner, Notification notification) {
    partner.updateNotificationMethod(new StandardDelivery());
    partner.getNotifications().add(notification);
  }

}
