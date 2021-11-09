package ggc;

import java.io.Serializable;

public class TransactionManager extends TransactionVisitor implements Serializable {
  
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111031210L;

  public double visitTransaction(Acquisition a) {
    // acquisitions are already accounted for in the available balance
    return 0;
  }

  public double visitTransaction(Sale s, int currentDate) {
    if (!s.isPaid()) {
      s.updateActualPrice(currentDate);
      return s.getActualPrice();
    }
    // already accounted for in the available balance, if paid
    return 0;
  }

}
