package ggc.exceptions;

/** Launched when a given partner string isn't in the system */
public class NoSuchPartnerKeyException extends PartnerKeyException {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252107L;

  public NoSuchPartnerKeyException(String key) {
    super(key);
  }

}
