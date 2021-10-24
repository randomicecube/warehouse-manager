package ggc;

import java.io.Serializable;

public class Product implements Serializable {

    private String _productKey;

    private int _productPrice;
    // Integer instead of int for consistency's sake
    private Integer _stock;

    public Product(String productKey, int productPrice, Integer stock){
        _productKey = productKey;
       _productPrice = productPrice;
        _stock = stock;
    }

    public void setProductKey(String productKey){
        _productKey = productKey;
    }

    public void setProductPrice(int productPrice) {
        _productPrice = productPrice;
    }

    public void setStock(Integer stock){
        _stock = stock;
    }

    public String getProductKey(){
        return _productKey;
    }

    public int getProductPrice() {
        return _productPrice;
    }

    public Integer getProductStock(){
        return _stock;
    }


    // TODO - implement ToString() for Product (and Override it in BreakdownProduct)
    @Override
    public String toString() {
        return String.join(
            "|", 
            String.valueOf(getProductKey()),
            String.valueOf(getProductPrice()),
            String.valueOf(getProductStock())
        );
    }
}
