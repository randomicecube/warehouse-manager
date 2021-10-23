package ggc;

import java.io.Serializable;

public class BreakdownProduct extends Product implements Serializable {

    private Recipe _recipe;
    
    private Double _aggravationFactor;

    public BreakdownProduct(Recipe recipe, Double aggravationFactor, String productKey){
        super(productKey);
        _recipe = recipe;
        _aggravationFactor = aggravationFactor;
    }

    public void setRecipe(Recipe recipe){
        _recipe = recipe;
    }

    public void setAggravationFactor(Double aggravationFactor){
        _aggravationFactor = aggravationFactor;
    }

    public Recipe getRecipe(){
        return _recipe;
    }

    public Double getAggravationFactor(){
        return _aggravationFactor;
    }
}