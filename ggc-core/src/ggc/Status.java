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

  /** Partner associated with the status */
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

  /** @return partner associated with the status */
  public Partner getPartner() {
    return _partner;
  }

  /** @return whether the Partner's ready to change to selection */
  public boolean changeToSelection() {
    return _partner.getPartnerPoints() > SELECTION_LIMIT;
  }

  /** @return whether the Partner's ready to change to elite */
  public boolean changeToElite() {
    return _partner.getPartnerPoints() > ELITE_LIMIT;
  }

  /**
   * Returns the modifier of a Status, depending on the days to the deadline
   * @return modifier
   */
  public abstract double getModifier(int delta, int currentDate, int limitDate);

}