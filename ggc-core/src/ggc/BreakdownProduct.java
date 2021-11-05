package ggc;

import java.io.Serializable;

/**
 * Class representing a breakdown product, which is a Product consisting of
 * other Products
 */
public class BreakdownProduct extends Product implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252102L;

  /** Product's associated recipe */
  private Recipe _recipe;

  /** Product's associated aggravation factor */
  private Double _aggravationFactor;

  public BreakdownProduct(Recipe recipe, Double aggravationFactor, 
                          String productKey, Integer stock, Double price) {
    super(productKey, stock, price);
    _recipe = recipe;
    _aggravationFactor = aggravationFactor;
  }

  /** @return product's recipe */
  public Recipe getRecipe() {
    return _recipe;
  }

  /** @return product's aggravation factor */
  public Double getAggravationFactor() {
    return _aggravationFactor;
  }

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