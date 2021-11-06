package ggc.exceptions;

import java.io.Serializable;

/** Launched when a given partner string isn't in the system */
public class NoSuchPartnerKeyException extends Exception implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252107L;

  private String _key;

  public NoSuchPartnerKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

}
