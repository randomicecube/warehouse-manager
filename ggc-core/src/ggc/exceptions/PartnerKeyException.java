package ggc.exceptions;

/** Exceptions related to the Partner's keys */
public abstract class PartnerKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252110L;

  private final String _key;

  public PartnerKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }
}
