package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Product Breakdown
 */
public class Breakdown extends Transaction implements Serializable {
  // TODO - implement Breakdown

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252103L;

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitBreakdown(this);
  }

}
