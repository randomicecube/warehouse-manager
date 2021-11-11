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
   * @param a
   * @param currentDate
   * @return 0
   */
  public double visitAcquisition(Acquisition a, int currentDate) {
    // acquisitions are already accounted for in the available balance
    return 0;
  }

  /**
   * Visits sale
   * @param s
   * @param currentDate
   * @return 0 if it's paid, to be paid price if it isn't
   */
  public double visitSale(Sale s, int currentDate) {
    if (!s.isPaid()) {
      s.updateActualPrice(currentDate);
      return s.getActualPrice();
    }
    // already accounted for in the available balance, if paid
    return 0;
  }

  /**
   * Visits Breakdown
   * @param b
   * @param currentDate
   * @return 0
   */
  public double visitBreakdown(Breakdown b, int currentDate) {
    // breakdowns are already accounted for in the available balance
    return 0;
  }

}
