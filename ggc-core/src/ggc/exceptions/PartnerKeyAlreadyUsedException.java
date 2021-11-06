package ggc.exceptions;

import java.io.Serializable;

/** Launched when a given string is already in the system */
public class PartnerKeyAlreadyUsedException extends Exception implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252109L;

  private String _key;

  public PartnerKeyAlreadyUsedException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

}
