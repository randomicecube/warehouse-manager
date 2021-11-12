package ggc;

import java.io.Serializable;

/**
 * The "in-between" Status between Normal and Elite
 */
public class SelectionStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252055L;

  /** Day gap for partner to lose points */
  private static final int LOSE_POINTS_GAP = -2;

  /**
   * Main Constructor
   * @param partner
   */
  public SelectionStatus(Partner partner) {
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
      return daysToDeadline >= 2 ? 0.95 : 1.0;
    } else if (daysToDeadline >= -delta) {
      int gap = currentDate - limitDate;
      return gap > 1 ? 1.0 + gap * 0.02 : 1.0;
    } else {
      return 1.0 + (-daysToDeadline) * 0.05;
    }

  }

  /**
   * Selection's payment strategy
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
        partner.updatePartnerPoints(-0.9 * partner.getPartnerPoints());
        partner.updatePartnerStatus(new NormalStatus(partner));
      }
    } else {
      partner.updatePartnerPoints(10 * price);
      if (changeToElite()) {
        partner.updatePartnerStatus(new EliteStatus(partner));
      }
    }
  }

  @Override
  public String toString() {
    return "SELECTION";
  }

}
