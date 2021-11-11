package ggc;

import java.io.Serializable;

/**
 * Class representing a breakdown product, which is a Product consisting of
 * other Products (has a Recipe)
 */
public class BreakdownProduct extends Product implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252102L;

  /** Product's associated recipe */
  private Recipe _recipe;

  /** Product's associated aggravation factor */
  private Double _aggravationFactor;

  /**
   * Main Constructor
   * 
   * @param recipe
   * @param aggravationFactor
   * @param productKey
   * @param stock
   * @param price
   */
  public BreakdownProduct(Recipe recipe, Double aggravationFactor, 
                          String productKey, Integer stock, Double price) {
    super(productKey, stock, price);
    _recipe = recipe;
    _aggravationFactor = aggravationFactor;
  }

  /**
   * Secondary Constructor when there's no price associated
   * 
   * @param recipe
   * @param aggravationFactor
   * @param productKey
   * @param stock
   */
  public BreakdownProduct(Recipe recipe, Double aggravationFactor, 
                          String productKey, Integer stock) {
    super(productKey, stock);
    _recipe = recipe;
    _aggravationFactor = aggravationFactor;
  }

  /** @return product's recipe */
  @Override
  public Recipe getRecipe() {
    return _recipe;
  }

  /** @return product's aggravation factor */
  @Override
  public double getAggravationFactor() {
    return _aggravationFactor;
  }

  /**
   * @return 5
   */
  @Override
  public int getProductDeadlineDelta() {
    return 5; // 5 for "Breakdown" Products
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      super.toString(),
      String.valueOf(getAggravationFactor()),
      String.valueOf(getRecipe())
    );
  }
}