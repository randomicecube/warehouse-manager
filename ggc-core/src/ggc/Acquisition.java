package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - an Acquisition
 */
public class Acquisition extends Transaction implements Serializable {
  // TODO - implement Acquisition

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252105L;

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitAcquisition(this);
  }

}
