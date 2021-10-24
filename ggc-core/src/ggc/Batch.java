package ggc;

import java.io.Serializable;

/**
 * Class representing Product batches
 * All instances have a product type, a Product amount and the price per product
 */
public class Batch implements Serializable {

    /** The Batch's associated Product*/
    private Product _productType;

    /** The Batch's product current stock */
    private int _amount;
    
    /** The Batch's per-unit Product price */
    private double _price;

    /**
     * Main Constructor
     * @param productType batch's product
     * @param amount batch's product stock
     * @param price batch's per-unit product price
     */
    public Batch(Product productType, int amount, double price){
        _productType = productType;
        _amount = amount;
        _price = price;
    }

    /**
     * Update the batch's current product stock
     * @param amount
     */
    public void updateAmount(int amount){
        _amount += amount;
    }

    /** @return batch's associated product type */
    public Product getProductType(){
        return _productType;
    }

    /** @return batch's current product stock */
    public int getAmount(){
        return _amount;
    }

    /** @return batch's per-unit product price */
    public double getPrice(){
        return _price;
    }
    
}