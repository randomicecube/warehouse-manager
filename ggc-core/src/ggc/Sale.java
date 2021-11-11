package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - a Sale
 */
public class Sale extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252054L;

  private int _dueDate;

  private double _actualPrice;

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

  /**
   * 
   * @return dueDate
   */
  public int getDueDate() {
    return _dueDate;
  }

  /**
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

  /**
   * 
   * @return actual price
   */
  public double getActualPrice() {
    return _actualPrice;
  }

  /**
   * 
   * @return paid
   */
  public boolean isPaid() {
    return _paid;
  }

  /**
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
