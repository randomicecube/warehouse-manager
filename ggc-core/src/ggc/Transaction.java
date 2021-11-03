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

  /** Transaction's corresponding payment (or due, if a Sale) date */
  private int _paymentDate;

  private boolean _paid = false;

  public Transaction(int key, int paymentDate) {
    _transactionKey = key;
    _paymentDate = paymentDate;
  }

  /** @return transaction's key */
  public int getTransactionKey() {
    return _transactionKey;
  }

  /** @return transaction's associated payment/due date */
  public int getPaymentDate() {
    return _paymentDate;
  }

  public boolean isPaid() {
    return _paid;
  }

  /** Used only in Sales and Breakdown Transactions
   *  updates the previously set "due date" to a given one
   * 
   * @param date
   */
  public void updatePaymentDate(int date) {
    _paymentDate = date;
  }

  public void updatePaid() {
    _paid = true;
  }

  /** accepts a visitor - specifically, a TransactionVisitor */
  public abstract void accept(TransactionVisitor visitor);

}
