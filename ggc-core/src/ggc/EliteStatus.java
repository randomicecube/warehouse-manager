package ggc;

import java.io.Serializable;

/**
 * A specification of Status - the "most prestigious" Status in the system
 */
public class EliteStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252101L;

  private static final int LOSE_POINTS_GAP = -15;

  public EliteStatus(Partner partner) {
    super(partner);
  }

  public void payTransaction(Sale sale, int currentDate) {
    int dueDate = sale.getDueDate();
    double dayDifference = dueDate - currentDate;
    boolean isLate = dueDate < currentDate;
    sale.updateActualPrice(currentDate);
    double price = sale.getActualPrice();
    Partner partner = getPartner();
    if (isLate) {
      partner.updatePartnerStatus(new SelectionStatus(partner));
      if (dayDifference < LOSE_POINTS_GAP) {
        partner.updatePartnerPoints(-0.75 * partner.getPartnerPoints());
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
