package ggc;

import java.io.Serializable;

/**
 * A specification of Status - the "most prestigious" Status in the system
 */
public class EliteStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252101L;

  /** @return Elite Status price modifiers for P2 */
  public double getModifierP2(int currentDate, int limitDate) {
    return 0.9;
  }

  /** @return Elite Status price modifiers for P3 */
  public double getModifierP3(int currentDate, int limitDate) {
    return 0.95;
  }

  /** @return Elite Status price modifiers for P4 */
  public double getModifierP4(int currentDate, int limitDate) {
    return 1.0;
  }

  @Override
  public String toString() {
    return "ELITE";
  }

}
