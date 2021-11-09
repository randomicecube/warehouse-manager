package ggc;

import java.io.Serializable;

/**
 * Class representing a Transaction in the system
 */
public abstract class Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252052L;

  /** Transaction's key */
  private int _transactionKey;

  /** Transaction's corresponding base date */
  private int _paymentDate;

  private Partner _partner;

  private Product _product;

  private int _amount;

  private double _basePrice;

  public Transaction(int transactionKey, Partner partner, Product product, int baseDate, int amount, double basePrice) {
    _transactionKey = transactionKey;
    _partner = partner;
    _product = product;
    _paymentDate = baseDate;
    _amount = amount;
    _basePrice = basePrice;
  }

  /** @return transaction's key */
  public int getTransactionKey() {
    return _transactionKey;
  }

  /** @return transaction's associated payment/due date */
  public int getPaymentDate() {
    return _paymentDate;
  }

  public double getBasePrice() {
    return _basePrice;
  }

  public Product getProduct() {
    return _product;
  }
  
  public Partner getPartner() {
    return _partner;
  }

  /** Used only in Sales and Breakdown Transactions
   *  updates the previously set "due date" to a given one
   * 
   * @param date
   */
  public void updatePaymentDate(int date) {
    _paymentDate = date;
  }

  public abstract void updateActualPrice(int currentDate);

  public abstract double accept(TransactionVisitor visitor, int date);

  @Override
  public String toString() {
    return String.join(
      "|",
      String.valueOf(_transactionKey),
      _partner.getPartnerKey(),
      _product.getProductKey(),
      String.valueOf(_amount)
    );
  }

}
