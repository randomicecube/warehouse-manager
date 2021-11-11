package ggc;

import java.io.Serializable;

import java.util.Map;

/**
 * Class representing a specific Transaction - a Product Breakdown
 */
public class Breakdown extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252103L;

  private Recipe _recipe;

  private int _dueDate;

  /**
   * Main Constructor
   * @param transactionKey
   * @param partner
   * @param product
   * @param baseDate
   * @param amount
   * @param basePrice
   * @param dueDate
   * @param recipe
   */
  public Breakdown(int transactionKey, Partner partner, BreakdownProduct product, int baseDate, int amount, double basePrice, int dueDate, Recipe recipe) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
    _dueDate = dueDate;
    _recipe = recipe;
  }

  /**
   * @return recipe
   */
  public Recipe getRecipe() {
    return _recipe;
  }

  /**
   * @return true
   */
  @Override
  public boolean hasRecipe() {
    return true;
  }

  /**
   * 
   * @return _dueDate
   */
  public int getDueDate() {
    return _dueDate;
  }

  /**
   * 
   * @return a breakdown product
   */
  @Override
  public BreakdownProduct getProduct() {
    return (BreakdownProduct) super.getProduct();
  }

  /**
   * Accept transaction visitor
   * 
   * @param visitor
   * @param date
   * @return visitor's visit
   */
  public double accept(TransactionVisitor visitor, int date) {
    return visitor.visitBreakdown(this, date);
  }

  /**
   * Updates actual price (Breakdown does nothing)
   * @param currentDate
   */
  public void updateActualPrice(int currentDate) { }

  @Override
  public String toString() {
    String recipeString = "";
    Map<Product, Integer> recipe = getRecipe().getIngredients();
    for (Product ingredient : recipe.keySet()){
      String ingredientKey = ingredient.getProductKey(); 
      int ingredientAmount = getAmount() * recipe.get(ingredient);
      long ingredientCost = Math.round(ingredient.getProductPrice() * ingredientAmount);
      recipeString += ingredientKey + ":" + ingredientAmount + ":" + ingredientCost + "#";
    }
    recipeString = recipeString.substring(0, recipeString.length() - 1);
    return String.join(
      "|",
      "DESAGREGAÇÃO",
      super.toString(),
      String.valueOf(Math.round(getBasePrice())),
      String.valueOf(Math.round(getActualPrice())),
      String.valueOf(getDueDate()),
      recipeString
    );
  }

}
