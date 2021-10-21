package ggc.exceptions;

public class PartnerKeyAlreadyUsedException extends Exception {
    private final String _key;

    public PartnerKeyAlreadyUsedException(String key) {
        _key = key;
    }

    public String getKey() {
        return _key;
    }
}
