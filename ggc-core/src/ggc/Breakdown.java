package ggc;

import java.io.Serializable;

import java.util.Map;

/**
 * Class representing a specific Transaction - a Product Breakdown
 */
public class Breakdown extends Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252103L;

  /** Recipe of the BreakdownProduct associated with the Breakdown */
  private Recipe _recipe;

  /** Transaction's due date */
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
   * @param recipe
   */
  public Breakdown(int transactionKey, Partner partner, Product product, int baseDate, int amount, double basePrice, int dueDate, Recipe recipe) {
    super(transactionKey, partner, product, baseDate, amount, basePrice);
    _dueDate = dueDate;
    _recipe = recipe;
    _actualPrice = basePrice >= 0 ? basePrice : 0;
  }

  /**
   * @return recipe
   */
  public Recipe getRecipe() {
    return _recipe;
  }

  /**
   * 
   * @return _dueDate
   */
  public int getDueDate() {
    return _dueDate;
  }

  public double getActualPrice() {
    return _actualPrice;
  }

  @Override
  public boolean isPaid() {
    return _paid;
  }

  public void updatePaid(double paid) {
    _paid = true;
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
   * Accept transaction checker (a visitor)
   * 
   * @param checker
   * @return visitor's visit
   */
  public boolean accept(TransactionChecker checker) {
    return checker.visitBreakdown(this);
  }

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
