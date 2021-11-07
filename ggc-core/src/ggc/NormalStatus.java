package ggc;

import java.io.Serializable;

/**
 * Normal Status is the "default" Status - when they are first registered,
 * they're registered with this Status
 */
public class NormalStatus extends Status implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252100L;

  public NormalStatus(Partner partner) {
    super(partner);
  }

  public void payTransaction(Sale sale, int currentDate) {
    int dueDate = sale.getDueDate();
    boolean isLate = dueDate < currentDate;
    double price = sale.getActualPrice();
    Partner partner = getPartner();
    if (isLate) {
      partner.clearPartnerPoints();
    } else {
      partner.updatePartnerPoints((int) (10 * price));
      if (changeToSelection()) {
        partner.updatePartnerStatus(new SelectionStatus(partner));
      } else if (changeToElite()) {
        partner.updatePartnerStatus(new EliteStatus(partner));
      }
    }
  }

  /** @return Normal Status price modifiers for P2 */
  public double getModifierP2(int currentDate, int limitDate) {
    return 1.0;
  }

  /** @return Normal Status price modifiers for P3 */
  public double getModifierP3(int currentDate, int limitDate) {
    return 1.0 + (currentDate - limitDate) * 0.05;
  }

  /** @return Normal Status price modifiers for P4 */
  public double getModifierP4(int currentDate, int limitDate) {
    return 1.0 + (currentDate - limitDate) * 0.10;
  }

  @Override
  public String toString() {
    return "NORMAL";
  }

}
