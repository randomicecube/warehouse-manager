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

  /**
   * Main Constructor
   * @param partner
   */
  public Status(Partner partner) {
    _partner = partner;
  }

  /**
   * Status' payment strategy
   * @param transaction
   * @param currentDate
   */
  public abstract void payTransaction(Transaction transaction, int currentDate);

  /**
   * 
   * @return
   */
  public Partner getPartner() {
    return _partner;
  }

  /**
   * Check if the Partner's ready to change to selection
   * @return boolean
   */
  public boolean changeToSelection() {
    return _partner.getPartnerPoints() > SELECTION_LIMIT && !changeToElite();
  }

  /**
   * Check if the Partner's ready to change to elite
   * @return boolean
   */
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