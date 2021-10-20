package ggc;

public abstract class Notification {
    
    private String _productKey;

    private int _productPrice;

    private String _notificationType;

    public Notification(String productKey, int productPrice, String notificationType) {
        _productKey = productKey;
        _productPrice = productPrice;
        _notificationType = notificationType;
    }

    public void setProductKey(String productKey) {
        _productKey = productKey;
    }

    public void setProductPrice(int productPrice) {
        _productPrice = productPrice;
    }

    public void setNotificationType(String notificationType) {
        _notificationType = notificationType;
    }

    public String getProductKey() {
        return _productKey;
    }

    public int getProductPrice() {
        return _productPrice;
    }

    public String getNotificationType() {
        return _notificationType;
    }

    @Override
    public String toString() {
        return String.join(
            "|",
            getNotificationType(),
            getProductKey(),
            getProductPrice()
        );
    }

}
