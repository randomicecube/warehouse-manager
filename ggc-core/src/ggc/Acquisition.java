package ggc;

import java.io.Serializable;

/**
 * Class representing a specific Transaction - an Acquisition
 */
public class Acquisition extends Transaction implements Serializable {
  // TODO - implement Acquisition
  private String _partnerID;
  private String _productID;
  private double _productPrice;
  private int _productAmount;

  public Acquisition(String partnerID, String productID, double productPrice,
  int productAmount, int key, int paymentDate){
    super(key, paymentDate);
    _partnerID = partnerID;
    _productID = productID;
    _productPrice = productPrice;
    _productAmount = productAmount;
  }

  public String getPartnerID(){
    return _partnerID;
  }

  public String getProductID(){
    return _productID;
  }

  public double getProductPrice(){
    return _productPrice;
  }

  public int getProductAmount(){
    return _productAmount;
  }

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252105L;

  /** accepts a visitor - specifically, a TransactionVisitor */
  public void accept(TransactionVisitor visitor) {
    visitor.visitAcquisition(this);
  }

}