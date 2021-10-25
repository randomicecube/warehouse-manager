package ggc.exceptions;

/** Exception for invalid (<= 0) given dates */
public class NoSuchDateException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252106L;

  private final int _date;

  public NoSuchDateException(int date) {
    _date = date;
  }

  public int getDate() {
    return _date;
  }
}
