package ggc.exceptions;

public abstract class PartnerKeyException extends Exception {
    private final String _key;

    public PartnerKeyException(String key) {
        _key = key;
    }

    public String getKey() {
        return _key;
    }
}
