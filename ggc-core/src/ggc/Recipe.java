package ggc;

import java.util.Map;
import java.util.HashMap;

public class Recipe {
    
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

    @Override
    public String toString(){
        String recipe = "";
        for (Product p : _ingredients.keySet()){
            String key = p.toString();
            String value = _ingredients.get(p).toString();
            recipe += key + ":" + value;
        }
        //TODO MAKE ORDER CORRECT
        return recipe;
    }

}
