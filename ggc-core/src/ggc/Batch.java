package ggc;

import java.io.Serializable;

public class Batch implements Serializable {

    private Product _productType;

    private int _amount;
    
    private Partner _partner;
    
    private double _price;

    public Batch(Product productType, int amount, Partner partner, double price){
        _productType = productType;
        _amount = amount;
        _partner = partner;
        _price = price;
    }

    public void setProduct(Product productType){
        _productType = productType;
    }

    public void setAmount(int amount){
        _amount =  amount;
    }

    public void setPrice(double price){
        _price = price;
    }

    public void updatePartner(Partner p){
        _partner = p;
    }

    public Product getProductType(){
        return _productType;
    }

    public int getAmount(){
        return _amount;
    }

    public Partner getPartner(){
        return _partner;
    }

    public double getPrice(){
        return _price;
    }
}