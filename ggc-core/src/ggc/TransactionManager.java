package ggc;

import java.io.Serializable;

public class TransactionManager extends TransactionVisitor implements Serializable {
  
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111031210L;

  public void visitAcquisition(Acquisition a) {
    // TODO implement
  }

  public void visitBreakdown(Breakdown b) {
    // TODO implement 
  }

  public void visitSale(Sale s) {
    // TODO implement
  }

}
