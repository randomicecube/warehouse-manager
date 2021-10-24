package ggc.exceptions;

/** Exception for invalid (<= 0) given dates */
public class NoSuchDateException extends Exception{
    private final int _date;

    public NoSuchDateException(int date) {
        _date = date;
    }

    public int getDate() {
        return _date;
    }
}
