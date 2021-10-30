package ggc.exceptions;

/** Launched when a given product string isn't in the system */
public class NoSuchTransactionKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110301051L;

  private String _key;

  public NoSuchTransactionKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

}