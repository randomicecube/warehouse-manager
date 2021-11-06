package ggc.exceptions;

import java.io.Serializable;

public class NotEnoughStockException extends Exception implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111042317L;

  private String _productKey;
  private int _requested;
  private int _available;

  public NotEnoughStockException(String productKey, int requested, int available) {
    _productKey = productKey;
    _requested = requested;
    _available = available;
  }

  public String getKey() {
    return _productKey;
  }

  public int getRequested() {
    return _requested;
  }

  public int getAvailable() {
    return _available;
  }

}
  
