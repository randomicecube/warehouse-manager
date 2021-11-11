package ggc;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A class which compares batches
 */
public class BatchComparator implements Comparator<Batch>, Serializable {

  /**
   * Compares two batches' stocks
   * @param b1
   * @param b2
   * @return
   */
  public int compareStocks(Batch b1, Batch b2) {
    return b1.getAmount() - b2.getAmount();
  }

  @Override
  public int compare(Batch b1, Batch b2) {
    return compareStocks(b1, b2);
  }

}
