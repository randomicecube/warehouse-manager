package ggc;

import java.io.*;
import ggc.exceptions.*;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  private int _date;

  // FIXME define attributes
  // FIXME define contructor(s)
  // FIXME define methods

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */

  public int getDate() {
    return _date;
  }

  public void updateDate(int date) throws NoSuchDateException {
    if (date <= 0) {
      throw new NoSuchDateException(date);
    }
    _date += date;
  }
   
  void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
    //FIXME implement method
  }

}
