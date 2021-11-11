package ggc;

import java.io.Serializable;

/**
 * This class is used to check properties about a given product
 */
public abstract class ProductChecker implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110110905L;
  
  /**
   * Visits a simple product
   * @param product
   * @return
   */
  public abstract boolean visitSimple(Product product);

  /**
   * Visits a breakdown product
   * @param product
   * @return
   */
  public abstract boolean visitBreakdownProduct(BreakdownProduct product);

}
