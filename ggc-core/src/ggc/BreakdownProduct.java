package ggc;

import java.io.Serializable;

/**
 * Class representing a breakdown product, 
 * which is a Product consisting of other Products
 */
public class BreakdownProduct extends Product implements Serializable {

    /** Product's associated recipe */
    private Recipe _recipe;
    
    /** Product's associated aggravation factor */
    private Double _aggravationFactor;

    public BreakdownProduct(Recipe recipe, Double aggravationFactor, String productKey, Integer stock, int productPrice){
        super(productKey, productPrice, stock);
        _recipe = recipe;
        _aggravationFactor = aggravationFactor;
    }

    /** @return product's recipe */
    public Recipe getRecipe(){
        return _recipe;
    }

    /** @return product's aggravation factor */
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