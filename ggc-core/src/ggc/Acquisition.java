package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - an Acquisition
 */
public class Acquisition extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252105L;

  public Acquisition(int transactionKey, Partner partner, Product product, int baseDate, int amount, int basePrice) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
  }

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitAcquisition(this);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      super.toString(),
      String.valueOf(getBasePrice()),
      String.valueOf(getPaymentDate())
    );
  }

}
