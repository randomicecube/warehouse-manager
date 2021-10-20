package ggc;

import java.util.List;
import java.util.ArrayList;

public class Recipe implements Comparable<Recipe> {
    
    private List<Product> _ingredients = new ArrayList<Product>();

    public void addIngredient(Product p) {
        _ingredients.add(p);
    }

    public List<Product> getIngredients() {
        return _ingredients;
    }

    @Override
    public int compareTo(Recipe other) {
        return getIngredients().equals(other.getIngredients());
    }

}
