package ggc.exceptions;

/** Launched when a given partner string isn't in the system */
public class NoSuchPartnerKeyException extends PartnerKeyException {
    
    public NoSuchPartnerKeyException(String key) {
        super(key);
    }
    
}
