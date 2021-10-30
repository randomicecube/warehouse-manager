package ggc.exceptions;

/** Launched when a given product string isn't in the system */
public class NoSuchTransactionKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110301051L;

  private int _key;

  public NoSuchTransactionKeyException(int key) {
    _key = key;
  }

  public int getKey() {
    return _key;
  }

}