package ggc;

import java.io.Serializable;

/**
 * Abstract class representing a Status in the system
 */
public abstract class Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252053L;

  /** The "border" between Normal and Selection Status is 2000 points */
  private final static double SELECTION_LIMIT = 2000.0;

  /** The "border" between Selection and Elite Status is 25000 points */
  private final static double ELITE_LIMIT = 25000.0;

  private Partner _partner;

  public Status(Partner partner) {
    _partner = partner;
  }

  public abstract void payTransaction(Sale sale, int currentDate);

  public Partner getPartner() {
    return _partner;
  }

  public boolean changeToNormal() {
    return _partner.getPartnerPoints() <= SELECTION_LIMIT;
  }

  public boolean changeToSelection() {
    return _partner.getPartnerPoints() > SELECTION_LIMIT && !changeToElite();
  }

  public boolean changeToElite() {
    return _partner.getPartnerPoints() > ELITE_LIMIT;
  }

  /** @return Status price modifiers for P1 */
  public double getModifierP1() {
    return 0.9;
  }

  /**
   * @param currentDate
   * @param limitDate
   * @return Status price modifiers for P2
   */
  public abstract double getModifierP2(int currentDate, int limitDate);

  /**
   * @param currentDate
   * @param limitDate
   * @return Status price modifiers for P3
   */
  public abstract double getModifierP3(int currentDate, int limitDate);

  /**
   * @param currentDate
   * @param limitDate
   * @return Status price modifiers for P4
   */
  public abstract double getModifierP4(int currentDate, int limitDate);
  
}