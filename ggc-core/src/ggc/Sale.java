package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Sale
 */
public class Sale extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252054L;

  /** Sale's due date */
  private int _dueDate;

  /** Sale's actual price (accounting for taxes/benefits) */
  private double _actualPrice;

  /** Flag corresponding to whether the sale is paid or not */
  private boolean _paid = false;

  /**
   * Main Constructor
   * @param transactionKey
   * @param partner
   * @param product
   * @param baseDate
   * @param amount
   * @param basePrice
   * @param dueDate
   */
  public Sale(int transactionKey, Partner partner, Product product, int baseDate, int amount, double basePrice, int dueDate) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
    _dueDate = dueDate;
  }

  /** Gets sale's due date
   * 
   * @return dueDate
   */
  public int getDueDate() {
    return _dueDate;
  }

  /** Updates sale's actual price (accounting for taxes/benefits)
   * 
   * Update the to be paid price considering the current date
   * @param currentDate
   */
  public void updateActualPrice(int currentDate) {
    int delta = getProduct().getProductDeadlineDelta();
    int deadline = getDueDate();
    int daysToDeadline = deadline - currentDate;
    Status partnerStatus = getPartner().getPartnerStatus();

    if (daysToDeadline >= delta) {
      _actualPrice = getBasePrice() * partnerStatus.getModifierP1();
    } else if (daysToDeadline >= 0) {
      _actualPrice = getBasePrice() * partnerStatus.getModifierP2(currentDate, deadline);
    } else if (daysToDeadline >= -delta) {
      _actualPrice = getBasePrice() * partnerStatus.getModifierP3(currentDate, deadline);
    } else {
      _actualPrice = getBasePrice() * partnerStatus.getModifierP4(currentDate, deadline);
    }
  }

  /** Gets sale's actual price
   * 
   * @return actual price
   */
  public double getActualPrice() {
    return _actualPrice;
  }

  /** @return whether the sale is paid or not */
  public boolean isPaid() {
    return _paid;
  }

  /** Sets sale's paid status to true, update's its price
   * 
   * @param price
   */
  public void updatePaid(double price) {
    _paid = true;
    _actualPrice = price;
  }

  /**
   * Accept transaction visitor
   * @param visitor
   * @param date
   * @return
   */
  public double accept(TransactionVisitor visitor, int date) {
    return visitor.visitSale(this, date);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      "VENDA",
      super.toString(),
      String.valueOf(Math.round(getBasePrice())),
      String.valueOf(Math.round(getActualPrice())),
      String.valueOf(getDueDate())
    ) + (isPaid() ? ("|" + getPaymentDate()) : "");
  }

}
