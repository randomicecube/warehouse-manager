package ggc;

import java.io.Serializable;

/**
 * Normal Status is the "default" Status - when they are first registered,
 * they're registered with this Status
 */
public class NormalStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252100L;

  /**
   * Main constructor
   * @param partner
   */
  public NormalStatus(Partner partner) {
    super(partner);
  }

  /**
   * Returns the modifier of this Status, depending on the days to the deadline
   * @return modifier
   */
  public double getModifier(int delta, int currentDate, int limitDate) {

    int daysToDeadline = limitDate - currentDate;
    if (daysToDeadline >= delta) {
      return 0.9;
    } else if (daysToDeadline >= 0) {
      return 1.0;
    } else if (daysToDeadline >= -delta) {
      return 1.0 + (-daysToDeadline) * 0.05;
    } else {
      return 1.0 + (-daysToDeadline) * 0.10;
    }

  }


  /**
   * Normal's payment strategy
   * 
   * @param transaction
   * @param currentDate
   */
  public void payTransaction(Transaction transaction, int currentDate) {
    int dueDate = transaction.getDueDate();
    boolean isLate = dueDate < currentDate;
    double price = transaction.getActualPrice();
    Partner partner = getPartner();
    if (isLate) {
      partner.clearPartnerPoints();
    } else {
      partner.updatePartnerPoints(10 * price);
      if (changeToElite()) {
        partner.updatePartnerStatus(new EliteStatus(partner));
      } else if (changeToSelection()) {
        partner.updatePartnerStatus(new SelectionStatus(partner));
      }
    }
  }

  @Override
  public String toString() {
    return "NORMAL";
  }

}
