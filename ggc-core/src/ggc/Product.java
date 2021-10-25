package ggc;

import java.io.Serializable;

public class Product implements Serializable {

    private String _productKey;

    private Integer _productPrice = 0;
    
    // Integer instead of int for consistency's sake with Maps
    private Integer _stock = 0;

    public Product(String productKey, Integer stock, Integer productPrice) {
        _productKey = productKey;
        _productPrice = productPrice;
        _stock = stock;
    }

    public void setProductKey(String productKey){
        _productKey = productKey;
    }

    public void updatePrice(Integer price) {
        _productPrice = price;
    }

    public void updateStock(Integer stock) {
        _stock += stock;
    }

    public String getProductKey(){
        return _productKey;
    }

    public Integer getProductPrice() {
        return _productPrice;
    }

    public Integer getStock() {
        return _stock;
    }


    // TODO - implement ToString() for Product (and Override it in BreakdownProduct)
    @Override
    public String toString() {
        return String.join(
            "|", 
            getProductKey(),
            // TODO FIX
            String.valueOf(getProductPrice()),
            String.valueOf(getStock())
        );
    }
}
