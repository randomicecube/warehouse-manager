package ggc;

import java.io.Serializable;

import java.util.Map;

/**
 * Class representing a specific Transaction - a Product Breakdown
 */
public class Breakdown extends Sale implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252103L;

  private Recipe _recipe;

  public Breakdown(int transactionKey, Partner partner, BreakdownProduct product, int baseDate, int amount, double basePrice, int dueDate, Recipe recipe) {
    super(transactionKey, partner, product, baseDate, amount, basePrice, dueDate);
    _recipe = recipe;
  }

  public Recipe getRecipe() {
    return _recipe;
  }

  @Override
  public boolean hasRecipe() {
    return true;
  }

  @Override
  public BreakdownProduct getProduct() {
    return (BreakdownProduct) super.getProduct();
  }

  @Override
  public void updateActualPrice(int currentDate) { }

  @Override
  public String toString() {
    String recipeString = "";
    Map<String, Integer> recipe = getRecipe().getIngredients();
    for (String ingredientKey : recipe.keySet()){
      int ingredientAmount = getAmount() * recipe.get(ingredientKey);
      double ingredientCost = getBasePrice();
      recipeString += ingredientKey + ":" + ingredientAmount + ":" + ingredientCost + "#";
    }
    recipeString.substring(0, recipeString.length() - 1);
    return String.join(
      "|",
      "DESAGREGAÇÃO",
      super.toString().substring(6), // removing "VENDA|"
      recipeString
    );
  }

}
