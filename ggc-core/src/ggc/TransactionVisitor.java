package ggc;

import java.io.Serializable;

public abstract class TransactionVisitor implements Serializable {
  
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110310005L;

  /**
   * Visit an Acquisition Transaction
   * @param acquisiton to-be-visited acquisition
   */
  public abstract void visitAcquisition(Acquisition acquisiton);

  /**
   * Visit a Breakdown Transaction
   * @param breakdown to-be-visited breakdown
   */
  public abstract void visitBreakdown(Breakdown breakdown);
  
  /**
   * Visit a Sale Transaction
   * @param sale to-be-visited sale
   */
  public abstract void visitSale(Sale sale);

}
