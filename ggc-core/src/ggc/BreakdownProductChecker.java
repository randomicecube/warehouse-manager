package ggc;

/**
 * Visitor class for ProductChecker, to check if a product is a breakdownProduct
 */
public class BreakdownProductChecker extends ProductChecker {
  
  /**
   * Visits simple product
   * @param product
   * @return false
   */
  public boolean visitSimple(Product product) {
    return false;
  }

  /**
   * Visits breakdown product
   * @param product
   * @return true
   */
  public boolean visitBreakdownProduct(BreakdownProduct product) {
    return true;
  }

}
