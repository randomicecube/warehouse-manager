package ggc;

public class BatchComparatorByPartner extends BatchComparator {
  
  public int comparePartnerKeys(Batch b1, Batch b2) {
    String partnerKeyB1 = b1.getPartner().getPartnerKey();
    String partnerKeyB2 = b2.getPartner().getPartnerKey();
    return partnerKeyB1.compareTo(partnerKeyB2);
  }

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
