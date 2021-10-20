package ggc;

public class Product{

    private String _productkey;

    public Product(String productkey){
        _productkey = productkey;
    }

    public void setProductKey(String productkey){
        _productkey = productkey;
    }

    public String getProductKey(){
        return _productkey;
    }
}
