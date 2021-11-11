package ggc;

import java.io.Serializable;

/**
 * A class representing delivery strategies for notifications
 */
public abstract class DeliveryStrategy implements Serializable {
  
  /**
   * Strategy itself
   * 
   * @param partner
   * @param notification
   */
  public abstract void deliver(Partner partner, Notification notification);

}
