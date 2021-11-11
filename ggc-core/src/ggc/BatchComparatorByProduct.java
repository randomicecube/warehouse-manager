package ggc;

public class BatchComparatorByProduct extends BatchComparator {
  
  public int compareProductKeys(Batch b1, Batch b2) {
    String productKeyB1 = b1.getProductType().getProductKey();
    String productKeyB2 = b2.getProductType().getProductKey();
    return productKeyB1.compareTo(productKeyB2);
  }

  @Override
  public int compare(Batch b1, Batch b2) {
    int keyComparator = compareProductKeys(b1, b2);
    if (keyComparator != 0) {
      return keyComparator;
    }
    return super.compare(b1, b2);
  }

}
