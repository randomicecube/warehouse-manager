package ggc;

public class Product{

    // TODO - Add Product Stock, either in a map (?) in the warehouse or associated with each product 

    private String _productKey;

    private int _productPrice;

    public Product(String productKey){
        _productKey = productKey;
    }

    public void setProductKey(String productKey){
        _productKey = productKey;
    }

    public void setProductPrice(int productPrice) {
        _productPrice = productPrice;
    }

    public String getProductKey(){
        return _productKey;
    }

    public int getProductPrice() {
        return _productPrice;
    }

    // TODO - implement ToString() for Product (and Override it in BreakdownProduct)
}
