package ggc;

import java.io.Serializable;

/**
 * This class is used to check properties about a given transaction
 */
public abstract class TransactionChecker implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110110906L;
  
  /**
   * Visits acquisition
   * @param acquisition
   * @return
   */
  public abstract boolean visitAcquisition(Acquisition acquisition);

  /**
   * Visits sale
   * @param sale
   * @return
   */
  public abstract boolean visitSale(Sale sale);

  /**
   * Visits breakdown
   * @param breakdown
   * @return
   */
  public abstract boolean visitBreakdown(Breakdown breakdown);

}
