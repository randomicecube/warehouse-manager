package ggc;

import java.io.Serializable;

/**
 * A class representing a transaction visitor
 */
public abstract class TransactionVisitor implements Serializable {
  
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110310005L;

  /**
   * Visit an Acquisition Transaction
   * @param acquisiton to-be-visited acquisition
   */
  public abstract double visitAcquisition(Acquisition acquisiton, int currentDate);
  
  /**
   * Visit a Sale Transaction
   * @param sale to-be-visited sale
   */
  public abstract double visitSale(Sale sale, int currentDate);

  /**
   * Visit a Breakdown transaction
   * @param breakdown
   * @param currentDate
   */
  public abstract double visitBreakdown(Breakdown breakdown, int currentDate);

}
