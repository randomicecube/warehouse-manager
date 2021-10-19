package ggc.exceptions;

public class NoSuchDateException extends Exception{
    private final int _date;

    public NoSuchDateException(int date) {
        _date = date;
    }

    public int getDate() {
        return _date;
    }
}
