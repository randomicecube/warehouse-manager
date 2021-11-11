package ggc;

import java.io.Serializable;

/**
 * A class representing delivery strategies for notifications
 */
public abstract class DeliveryStrategy implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111111325L;
  
  /**
   * Strategy itself
   * 
   * @param partner
   * @param notification
   */
  public abstract void deliver(Partner partner, Notification notification);

}
