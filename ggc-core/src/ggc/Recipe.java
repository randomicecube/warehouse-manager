package ggc;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.stream.Collectors;

/**
 * Class representing a product's recipe
 */
public class Recipe implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252056L;

  /** Recipe's ingredients and amount */
  private Map<Product, Integer> _ingredients = new LinkedHashMap<Product, Integer>();

  /**
   * Secondary Constructor - creates an empty recipe
   */
  public Recipe() {
    // create an empty recipe
  }

  /**
   * Main Constructor
   * @param ingredients
   */
  public Recipe(Map<Product, Integer> ingredients) {
    _ingredients = ingredients;
  }

  /**
   * Add an ingredient to the recipe
   * 
   * @param product
   * @param amount
   */
  public void addIngredient(Product product, Integer amount) {
    _ingredients.put(product, amount);
  }

  /** @return recipe's ingredients */
  public Map<Product, Integer> getIngredients() {
    return _ingredients;
  }

  @Override
  public String toString() {
    return getIngredients()
      .entrySet()
      .stream()
      .map(entry -> entry.getKey().getProductKey() + ":" + entry.getValue())
      .collect(Collectors.joining("#"));
  }

}
