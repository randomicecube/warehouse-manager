package ggc;

public class BreakdownProduct extends Product{

    private Recipe _recipe;
    private Double _aggravationfactor;

    public BreakdownProduct(Recipe recipe, Double aggravationfactor, String productkey){
        super(productkey);
        _recipe = recipe;
        _aggravationfactor = aggravationfactor;
    }

    public void setRecipe(Recipe recipe){
        _recipe = recipe;
    }

    public void setAggravationFactor(Double aggravationfactor){
        _aggravationfactor = aggravationfactor;
    }

    public Recipe getRecipe(){
        return _recipe;
    }

    public Double getAggravationFactor(){
        return _aggravationfactor;
    }
}