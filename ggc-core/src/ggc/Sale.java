package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Sale
 */
public class Sale extends Transaction implements Serializable {
  // TODO - implement Sale

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252054L;

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitSale(this);
  }

}
