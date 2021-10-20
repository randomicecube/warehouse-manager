package ggc;

public class Product{

    private String _productKey;

    public Product(String productKey){
        _productKey = productKey;
    }

    public void setProductKey(String productKey){
        _productKey = productKey;
    }

    public String getProductKey(){
        return _productKey;
    }
}
