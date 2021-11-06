package ggc;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.stream.Collectors;

import java.text.Normalizer.Form;

/**
 * Class representing a product's recipe
 */
public class Recipe implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252056L;

  /** Recipe's ingredients and amount */
  private Map<String, Integer> _ingredients = new LinkedHashMap<String, Integer>();

  public Recipe() {
    // create an empty recipe
  }

  public Recipe(Map<String, Integer> ingredients) {
    _ingredients = ingredients;
  }

  /**
   * Add an ingredient to the recipe
   * 
   * @param product
   * @param amount
   */
  public void addIngredient(Product product, Integer amount) {
    _ingredients.put(product.getProductKey(), amount);
  }

  /** @return recipe's ingredients */
  public Map<String, Integer> getIngredients() {
    return _ingredients;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Recipe recipe) {
      if (getIngredients().equals(recipe.getIngredients())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getIngredients()
      .entrySet()
      .stream()
      .map(entry -> entry.getKey() + ":" + entry.getValue())
      .collect(Collectors.joining("#"));
  }

}
