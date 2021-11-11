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
   * Elite's payment strategy
   * 
   * @param transaction
   * @param currentDate
   */
  public void payTransaction(Transaction transaction, int currentDate) {
    int dueDate = transaction.getDueDate();
    double dayDifference = dueDate - currentDate;
    boolean isLate = dueDate < currentDate;
    transaction.updateActualPrice(currentDate);
    double price = transaction.getActualPrice();
    Partner partner = getPartner();
    TransactionChecker checker = new BreakdownChecker();
    if (isLate) {
      if (!transaction.accept(checker)) { // accounting only for sales
        partner.updatePartnerStatus(new SelectionStatus(partner));
        if (dayDifference < LOSE_POINTS_GAP) {
          partner.updatePartnerPoints(-0.75 * partner.getPartnerPoints());
        }
      }
    } else {
      partner.updatePartnerPoints(10 * price);
    }
  }

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
