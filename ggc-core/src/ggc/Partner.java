package ggc;

import java.util.List;
import java.util.ArrayList;

public class Partner {

    /** Default notification is, by omission, an app notification */
    private static final String DEFAULT_NOTIFICATION_METHOD = "";

    /** Partner's status - partners always start with the "Normal" status */
    private Status _partnerStatus = new NormalStatus(); // TODO implement status (and its subclasses); STATUS STORES THE POINTS -> updatePoints() should be there too
                                                        // Points should be in the Status superclass, not in the sublcasses
    /** Partner's identification key */
    private String _partnerKey;

    /** Partner's name */
    private String _partnerName;

    /** Partner's address */
    private String _partnerAddress;

    /** Value representing the price of acquisitions warehouse-partner */
    private int _acquisitionPrices = 0;

    /** Value representing the total price of sales warehouse-partner */
    private double _overallSalePrices = 0;

    /** Value representing the amount the partner has actually paid  */
    private double _paidSalePrices = 0;

    /** Partner's transaction history */
    private List<Transaction>  _transactionHistory  = new ArrayList<Transaction>(); // TODO implement Transaction

    /** Partner's unread notifications */
    private List<Notification> _unreadNotifications = new ArrayList<Notification>(); // TODO implement Notification

    /** Partner's in-stock batches */
    private List<Batch> _partnerBatches = new ArrayList<Batch>(); // TODO implement Batch (and therefore Product)

    /** Partner's desired notification method */
    private String _notificationMethod;

    /**
     * Constructor without a given specific notification method
     * @param key partner's identification key
     * @param name partner's name
     * @param address partner's address
     */
    public Partner(String key, String name, String address) {
        this(key, name, address, DEFAULT_NOTIFICATION_METHOD);
    }

    /**
     * Main Constructor
     * @param key partner's identification key
     * @param name partner's name
     * @param address partner's address
     * @param method partner's desired notification delivery method
     */
    public Partner(String key, String name, String address, String method) {
        _partnerKey = key;
        _partnerName = name;
        _partnerAddress = address;
        _notificationMethod = method;
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

    /** @return partner's acquisition prices */
    public int getAcquisitionPrices() {
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

    /** Update the partner's status, according to their current point score */
    public void updateStatus() {
        // TODO implement
    }

    /** Update the partner's desired notification delivery method */
    public void updateNotificationMethod(String method) {
        _notificationMethod = method;
    }

    /** Add a transaction to the partner's transaction history collection */
    public void addTransaction(Transaction t) {
        _transactionHistory.add(t);
    }

    /** Add a notification to the partner's to-be-read notification collection */
    public void addToBeReadNotification(Notification n) {
        _unreadNotifications.add(n);
    }

    /** Add a batch to the partner's in-stock batch collection */
    public void addBatch(Batch b) {
        _partnerBatches.add(b);
    }

    /** Update the partner's total acquisition prices */
    public void updateAcquisitionPrices(int amount) {
        _acquisitionPrices += amount;
    }

    /** Update the partner's total price of sales (paid or not) */
    public void updateOverallSalePrices(double amount) {
        _overallSalePrices += amount;
    }

    /** Update the partner's total price of paid sales */
    public void updatePaidSalePrices(double amount) {
        _paidSalePrices += amount;
    }

    /** Read the partner's unread notifications (and clear them) */
    public void readNotifications() {
        // TODO implement
    }

    @Override
    public String toString() {
        // TODO implement toString() in Status (and Status itself)
        return String.join(
            "|",
            getPartnerKey(),
            getPartnerName(),
            getPartnerAddress(),
            getPartnerStatus(),
            getPartnerStatus().getPoints(),
            getAcquisitionPrices(),
            getOverallSalePrices(),
            getPaidSalePrices()
        );
    }

}
