package ggc;

import java.io.Serializable;

/**
 * Normal Status is the "default" Status - when they are first registered,
 * they're registered with this Status
 */
public class NormalStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252100L;

  /** @return Normal Status price modifiers for P2 */
  public double getModifierP2(int currentDate, int limitDate) {
    return 1.0;
  }

  /** @return Normal Status price modifiers for P3 */
  public double getModifierP3(int currentDate, int limitDate) {
    return 1.0 + (currentDate - limitDate) * 0.05;
  }

  /** @return Normal Status price modifiers for P4 */
  public double getModifierP4(int currentDate, int limitDate) {
    return 1.0 + (currentDate - limitDate) * 0.10;
  }

  @Override
  public String toString() {
    return "NORMAL";
  }

}
