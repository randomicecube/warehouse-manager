package ggc.exceptions;

public class NoSuchProductKeyException extends Exception {
    
    private String _key;

    public NoSuchProductKeyException(String key) {
        _key = key;
    }

    public String getKey() {
        return _key;
    }

}
