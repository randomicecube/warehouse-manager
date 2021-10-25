package ggc.exceptions;

/** Launched when a given string is already in the system */
public class PartnerKeyAlreadyUsedException extends PartnerKeyException {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252109L;

  public PartnerKeyAlreadyUsedException(String key) {
    super(key);
  }

}
