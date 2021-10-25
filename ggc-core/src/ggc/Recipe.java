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
    
    /** Recipe's ingredients and amount */
    private Map<String, Integer> _ingredients = new LinkedHashMap<String, Integer>();

    /**
     * Add an ingridient to the recipe
     * @param product
     * @param amount
     */
    public void addIngredient(Product product, Integer amount) {
        _ingredients.put(product.getProductKey(), amount);
    }

    /**@return recipe's ingridients */
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
    public String toString(){
        return getIngredients()
            .entrySet()
            .stream()
            .map(entry -> entry.getKey() + ":" + entry.getValue())
            .collect(Collectors.joining("#"));
    }

}
