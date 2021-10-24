package ggc.exceptions;

/** Launched when a given string is already in the system */
public class PartnerKeyAlreadyUsedException extends PartnerKeyException {

    public PartnerKeyAlreadyUsedException(String key) {
        super(key);
    }

}
