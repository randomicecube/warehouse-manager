package ggc;

/**
 * A class which compares two batches associated with a partner
 */
public class BatchComparatorByPartner extends BatchComparator {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111111327L;
  
  /**
   * Compares two batch's partnerKeys
   * @param b1
   * @param b2
   * @return
   */
  public int comparePartnerKeys(Batch b1, Batch b2) {
    String partnerKeyB1 = b1.getPartner().getPartnerKey();
    String partnerKeyB2 = b2.getPartner().getPartnerKey();
    return partnerKeyB1.compareTo(partnerKeyB2);
  }

  /**
   * Compares two batch's prices
   * @param b1
   * @param b2
   * @return
   */
  public double comparePrices(Batch b1, Batch b2) {
    return b1.getPrice() - b2.getPrice();
  }

  @Override
  public int compare(Batch b1, Batch b2) {
    int keyComparator = comparePartnerKeys(b1, b2);
    if (keyComparator != 0) {
      return keyComparator;
    }
    double priceComparator = comparePrices(b1, b2);
    if (priceComparator != 0) {
      return (int) priceComparator;
    }
    return super.compare(b1, b2);
  }

}
