package ggc;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

/**
 * Class representing a Partner in the system
 */
public class Partner implements Serializable, Observer {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110252058L;

  /** Partner's status - partners always start with the "Normal" status */
  private Status _partnerStatus = new NormalStatus(this);

  /** Partner's identification key */
  private String _partnerKey;

  /** Partner's name */
  private String _partnerName;

  /** Partner's address */
  private String _partnerAddress;

  /** Partner's current point score */
  private double _partnerPoints = 0;

  /** Value representing the price of acquisitions warehouse-partner */
  private double _acquisitionPrices = 0;

  /** Value representing the total price of sales warehouse-partner */
  private double _overallSalePrices = 0;

  /** Value representing the amount the partner has actually paid */
  private double _paidSalePrices = 0;

  /** Partner's transaction history */
  private List<Transaction> _transactionHistory = new ArrayList<Transaction>();

  private List<Sale> _saleHistory = new ArrayList<Sale>();

  private List<Acquisition> _acquisitionHistory = new ArrayList<Acquisition>();

  /** Partner's unread notifications */
  private List<Notification> _unreadNotifications = new ArrayList<Notification>();

  /** Partner's in-stock batches */
  private List<Batch> _partnerBatches = new ArrayList<Batch>();

  /** Partner's desired notification method */
  private DeliveryStrategy _notificationMethod;

  /**
   * Constructor without a given specific notification method
   * 
   * @param key     partner's identification key
   * @param name    partner's name
   * @param address partner's address
   */
  public Partner(String key, String name, String address) {
    this(key, name, address, new StandardDelivery());
  }

  /**
   * Main Constructor
   * 
   * @param key     partner's identification key
   * @param name    partner's name
   * @param address partner's address
   * @param method  partner's desired notification delivery method
   */
  public Partner(String key, String name, String address, DeliveryStrategy strategy) {
    _partnerKey = key;
    _partnerName = name;
    _partnerAddress = address;
    _notificationMethod = strategy;
  }

  /** @return partner's identification key */
  public String getPartnerKey() {
    return _partnerKey;
  }

  /** @return partner's name */
  public String getPartnerName() {
    return _partnerName;
  }

  /** @return partner's address */
  public String getPartnerAddress() {
    return _partnerAddress;
  }

  /** @return partner's status */
  public Status getPartnerStatus() {
    return _partnerStatus;
  }

  /** @return partner's points (related to Status) */
  public double getPartnerPoints() {
    return _partnerPoints;
  }

  /** @return partner's acquisition prices */
  public double getAcquisitionPrices() {
    return _acquisitionPrices;
  }

  /** @return partner's total price of sales (paid or not) */
  public double getOverallSalePrices() {
    return _overallSalePrices;
  }

  /** @return partner's total price of paid sales */
  public double getPaidSalePrices() {
    return _paidSalePrices;
  }

  /** @return partner's transactions */
  public List<Transaction> getTransactions() {
    return _transactionHistory;
  }

  public List<Sale> getSales() {
    return _saleHistory;
  }

  public List<Acquisition> getAcquisitions() {
    return _acquisitionHistory;
  }

  /** @return partner's batches */
  public List<Batch> getPartnerBatches() {
    return _partnerBatches;
  }

  /** @return partner's unread notifications */
  public List<Notification> getNotifications() {
    return _unreadNotifications;
  }

  /** Update the partner's status, according to their current point score */
  public void updatePartnerStatus(Status status) {
    _partnerStatus = status;
  }

  /**
   * Update partner's point total
   * 
   * @param points
   */
  public void updatePartnerPoints(double points) {
    _partnerPoints += points;
  }

  public void clearPartnerPoints() {
    _partnerPoints = 0;
  }

  /**
   * Update the partner's desired notification delivery method
   * 
   * @param method
   */
  public void updateNotificationMethod(DeliveryStrategy method) {
    _notificationMethod = method;
  }

  public void addNewSale(Sale sale) {
    _saleHistory.add(sale);
    _transactionHistory.add(sale);
  }

  public void addNewAcquisition(Acquisition acquisition) {
    _acquisitionHistory.add(acquisition);
    _transactionHistory.add(acquisition);
  }

  /**
   * Add a batch to the partner's in-stock batch collection
   * 
   * @param batch
   */
  public void addBatch(Batch batch) {
    _partnerBatches.add(batch);
  }

  /**
   * Update the partner's total acquisition prices
   * 
   * @param amount
   */
  public void updateAcquisitionPrices(double amount) {
    _acquisitionPrices += amount;
  }

  /**
   * Update the partner's total price of sales (paid or not)
   * 
   * @param amount
   */
  public void updateOverallSalePrices(double amount) {
    _overallSalePrices += amount;
  }

  /**
   * Update the partner's total price of paid sales
   * 
   * @param amount
   */
  public void updatePaidSalePrices(double amount) {
    _paidSalePrices += amount;
  }

  /** Clear the partner's unread notifications array */
  public void clearNotifications() {
    _unreadNotifications.clear();
  }

  public void update(Notification notification) {
    _notificationMethod.deliver(this, notification);
  }

  @Override
  public String toString() {
    return String.join(
      "|",
      getPartnerKey(),
      getPartnerName(),
      getPartnerAddress(),
      getPartnerStatus().toString(),
      String.valueOf(Math.round(getPartnerPoints())),
      String.valueOf(Math.round(getAcquisitionPrices())),
      String.valueOf(Math.round(getOverallSalePrices())),
      String.valueOf(Math.round(getPaidSalePrices()))
    );
  }

}
