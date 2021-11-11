package ggc;

import java.io.Serializable;

/**
 * Interface representing an observer for notifications
 */
public interface Observer extends Serializable {

  public void update(Notification notification);

}
