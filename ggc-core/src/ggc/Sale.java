package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Sale
 */
public class Sale extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252054L;

  private int _dueDate;

  private boolean _paid = false;

  public Sale(int transactionKey, Partner partner, Product product, int baseDate, int amount, double basePrice, int dueDate) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
    _dueDate = dueDate;
  }

  public int getDueDate() {
    return _dueDate;
  }

  public double getActualPrice() {
    // TODO - IMPLEMENT CORRECTLY
    return getBasePrice(); // currently just a placeholder
  }

  public boolean isPaid() {
    return _paid;
  }

  public void updatePaid() {
    _paid = true;
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      super.toString(),
      String.valueOf(getBasePrice()),
      String.valueOf(getActualPrice()),
      String.valueOf(getDueDate())
    ) + (isPaid() ? ("|" + getPaymentDate()) : "");
  }

}
