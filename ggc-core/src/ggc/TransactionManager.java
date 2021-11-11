package ggc;

import java.io.Serializable;

/**
 * A class representing a specific transaction visitor helping getting the accounting
 * balance 
 */
public class TransactionManager extends TransactionVisitor implements Serializable {
  
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111031210L;

  /**
   * Visits acquisition
   * @param acquisition
   * @param currentDate
   * @return 0
   */
  public double visitAcquisition(Acquisition acquisition, int currentDate) {
    // acquisitions are already accounted for in the available balance
    return 0;
  }

  /**
   * Visits sale
   * @param sale
   * @param currentDate
   * @return 0 if it's paid, to be paid price if it isn't
   */
  public double visitSale(Sale sale, int currentDate) {
    if (!sale.isPaid()) {
      sale.updateActualPrice(currentDate);
      return sale.getActualPrice();
    }
    // already accounted for in the available balance, if paid
    return 0;
  }

  /**
   * Visits Breakdown
   * @param breakdown
   * @param currentDate
   * @return 0
   */
  public double visitBreakdown(Breakdown breakdown, int currentDate) {
    // breakdowns are already accounted for in the available balance
    return 0;
  }

}
