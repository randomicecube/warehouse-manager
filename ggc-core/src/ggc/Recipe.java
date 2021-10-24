package ggc;

import java.io.Serializable;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;

import java.util.stream.Collectors;

public class Recipe implements Serializable {
    
    private Map<String, Integer> _ingredients = new TreeMap<String, Integer>();

    public void addIngredient(Product p, Integer amount) {
        _ingredients.put(p.getProductKey(), amount);
    }

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
        //TODO MAKE ORDER CORRECT

    }

}
