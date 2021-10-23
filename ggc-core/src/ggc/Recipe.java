package ggc;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;

public class Recipe implements Serializable {
    
    private Map<Product, Integer> _ingredients = new HashMap<Product, Integer>();

    public void addIngredient(Product p, Integer amount) {
        _ingredients.put(p, amount);
    }

    public Map<Product, Integer> getIngredients() {
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

}
