package ggc;

import java.io.Serializable;

/**
 * The "in-between" Status between Normal and Elite
 */
public class SelectionStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252055L;

  private static final int LOSE_POINTS_GAP = -2;

  public SelectionStatus(Partner partner) {
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
      partner.updatePartnerStatus(new NormalStatus(partner));
      if (dayDifference < LOSE_POINTS_GAP) {
        partner.updatePartnerPoints(-0.9 * partner.getPartnerPoints());
      }
    } else {
      partner.updatePartnerPoints(10 * price);
      if (changeToElite()) {
        partner.updatePartnerStatus(new EliteStatus(partner));
      }
    }
  }

  /** @return Selection Status price modifiers for P2 */
  public double getModifierP2(int currentDate, int limitDate) {
    return limitDate - currentDate >= 2 ? 0.95 : 1.0;
  }

  /** @return Selection Status price modifiers for P3 */
  public double getModifierP3(int currentDate, int limitDate) {
    int gap = currentDate - limitDate;
    return gap > 1 ? 1.0 + gap * 0.02 : 1.0;
  }

  /** @return Selection Status price modifiers for P4 */
  public double getModifierP4(int currentDate, int limitDate) {
    return 1.0 + (currentDate - limitDate) * 0.05;
  }

  @Override
  public String toString() {
    return "SELECTION";
  }

}
