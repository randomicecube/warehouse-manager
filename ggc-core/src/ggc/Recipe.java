package ggc;

import java.util.Map;
import java.util.HashMap;

public class Recipe implements Comparable<Recipe> {
    
    private Map<Product, Integer> _ingredients = new HashMap<Product, Integer>();

    public void addIngredient(Product p, Integer amount) {
        _ingredients.put(p, amount);
    }

    public Map<Product, Integer> getIngredients() {
        return _ingredients;
    }

    @Override
    public int compareTo(Recipe other) {
        return getIngredients().equals(other.getIngredients());
    }

}
