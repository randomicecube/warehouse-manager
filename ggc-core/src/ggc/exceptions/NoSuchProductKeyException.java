package ggc.exceptions;

import java.io.Serializable;

/** Launched when a given product string isn't in the system */
public class NoSuchProductKeyException extends Exception implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252108L;

  private String _key;

  public NoSuchProductKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

}
