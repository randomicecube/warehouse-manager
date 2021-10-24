package ggc;

public class BreakdownProduct extends Product {

    private Recipe _recipe;
    
    private Double _aggravationFactor;

    public BreakdownProduct(Recipe recipe, Double aggravationFactor, String productKey, Integer stock, int productPrice){
        super(productKey, productPrice, stock);
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

    @Override
    public String toString() {
        return String.join(
            "|",
            String.valueOf(getProductKey()),
            String.valueOf(getProductPrice()),
            String.valueOf(getProductStock()),
            String.valueOf(getAggravationFactor()),
            String.valueOf(getRecipe())
            );
    }
}