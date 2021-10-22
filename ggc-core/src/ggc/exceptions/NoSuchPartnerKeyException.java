package ggc.exceptions;

public class NoSuchPartnerKeyException extends PartnerKeyException {
    
    public NoSuchPartnerKeyException(String key) {
        super(key);
    }
    
}
