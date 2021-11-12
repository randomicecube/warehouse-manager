package ggc;

import java.io.Serializable;

/**
 * A specification of Status - the "most prestigious" Status in the system
 */
public class EliteStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252101L;

  /** Day gap for partner to lose points */
  private static final int LOSE_POINTS_GAP = -15;

  /**
   * Main constructor
   * @param partner
   */
  public EliteStatus(Partner partner) {
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
      return 0.9;
    } else if (daysToDeadline >= -delta) {
      return 0.95;
    } else {
      return 1.0;
    }

  }

  /**
   * Elite's payment strategy
   * 
   * @param transaction
   * @param currentDate
   */
  public void payTransaction(Transaction transaction, int currentDate) {
    int dueDate = transaction.getDueDate();
    double dayDifference = dueDate - currentDate;
    boolean isLate = dueDate < currentDate;
    double price = transaction.getActualPrice();
    Partner partner = getPartner();
    if (isLate) {
      if (dayDifference < LOSE_POINTS_GAP) {
        partner.updatePartnerPoints(-0.75 * partner.getPartnerPoints());
        partner.updatePartnerStatus(new SelectionStatus(partner));
      }
    } else {
      partner.updatePartnerPoints(10 * price);
    }
  }

  @Override
  public String toString() {
    return "ELITE";
  }

}
