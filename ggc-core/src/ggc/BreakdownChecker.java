package ggc;

/**
 * Visitor for the breakdown checker, checks if a transaction is a breakdown or not
 */
public class BreakdownChecker extends TransactionChecker {
  
  /**
   * Visits acquisition
   * @param acquisition
   * @return false
   */
  public boolean visitAcquisition(Acquisition acquisition) {
    return false;
  }

  /**
   * Visits sale
   * @param sale
   * @return false
   */
  public boolean visitSale(Sale sale) {
    return false;
  }

  /**
   * Visits breakdown
   * @param breakdown
   * @return true
   */
  public boolean visitBreakdown(Breakdown breakdown) {
    return true;
  }

}
