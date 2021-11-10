package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - an Acquisition
 */
public class Acquisition extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252105L;

  public Acquisition(int transactionKey, Partner partner, Product product, int baseDate, int amount, double basePrice) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
  }

  public void updateActualPrice(int currentDate) { }

  public double accept(TransactionVisitor visitor, int date) {
    return visitor.visitAcquisition(this, date);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      "COMPRA",
      super.toString(),
      String.valueOf(Math.round(getBasePrice())),
      String.valueOf(getPaymentDate())
    );
  }

}
