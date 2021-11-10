package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;

import java.util.Collection;
import java.util.stream.Collectors;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** If true, the program hasn't been changed since the last save() call */
  private boolean _saveFlag = false;

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, 
                            MissingFileAssociationException {

    if (_saveFlag) {
      return;
    }

    if (!hasFileAssociated()) {
      throw new MissingFileAssociationException();
    }

    _saveFlag = true;
    ObjectOutputStream oos = 
      new ObjectOutputStream(
        new BufferedOutputStream(
          new FileOutputStream(getFilename())
        )
      );
    oos.writeObject(_warehouse);
    oos.close();

  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) 
    throws MissingFileAssociationException, FileNotFoundException, IOException {
    setFilename(filename);
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {

    try {

      ObjectInputStream ois = 
        new ObjectInputStream(
          new BufferedInputStream(
            new FileInputStream(filename)
          )
        );

      _warehouse = (Warehouse) ois.readObject();
      ois.close();

      setFilename(filename);

    } catch (IOException | ClassNotFoundException e) {
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
      _saveFlag = false;
    } catch (IOException | BadEntryException | NoSuchPartnerKeyException | 
        NoSuchProductKeyException e) {
      throw new ImportFileException(textfile);
    }
  }

  /** @return current associated filename */
  public String getFilename() {
    return _filename;
  }

  /**
   * set the manager's associated filename (for future save() calls)
   * @param filename
   */
  public void setFilename(String filename) {
    _filename = filename;
  }

  /** @return whether the manager has a filename associated or not */
  public boolean hasFileAssociated() {
    return !_filename.equals("");
  }

  /** @return current warehouse date */
  public int getDate() {
    return _warehouse.getDate();
  }

  /**
   * update current warehouse date
   * @param days
   * @throws NoSuchDateException
   */
  public void updateDate(int days) throws NoSuchDateException {
    _warehouse.updateDate(days);
    _saveFlag = false;
  }

  /** @return current warehouse available balance */
  public double getAvailableBalance() {
    return _warehouse.getAvailableBalance();
  }

  /** @return current warehouse accounting balance */
  public double getAccountingBalance() {
    return _warehouse.getAccountingBalance();
  }

  /**
   * register a partner to be associated with the warehouse
   * @param key     partner's key
   * @param name    partner's name
   * @param address partner's address
   * @throws PartnerKeyAlreadyUsedException
   */
  public void registerPartner(String key, String name, String address) 
    throws PartnerKeyAlreadyUsedException {
    _warehouse.registerPartner(key, name, address);
    _saveFlag = false;
  }

  /**
   * get a partner, given their key
   * @param key
   * @return desired partner
   * @throws NoSuchPartnerKeyException
   */
  public String getPartnerString(String key) throws NoSuchPartnerKeyException {
    return _warehouse.getPartnerString(key);
  }

  /** @return a Collection with all the products associated with the warehouse */
  public Collection<String> getProductsCollection() {
    return _warehouse.getProductsCollection();
  }

  /**
   * @return a Collection with all the batches with partners associated with the
   *  warehouse
   */
  public Collection<String> getBatchesCollection() {
    return _warehouse.getBatchesCollection();
  }

  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    return _warehouse.getPartnersCollection();
  }

  /** 
   * @param partnerKey partner's key
   * @return a Collection with all batches associated with a given partner  
   */
  public Collection<String> getBatchesByPartner(String partnerKey)
    throws NoSuchPartnerKeyException {
      return _warehouse.getBatchesByPartner(partnerKey);
  }

  /** 
   * @param productKey product's key
   * @return a Collection with all batches associated with a given product  
   */
  public Collection<String> getBatchesByProduct(String productKey)
    throws NoSuchProductKeyException {
      return _warehouse.getBatchesByProduct(productKey);
  }

  /**
   * @param transactionKey transaction's key
   * @return transaction with the given key
   */
  public Transaction getTransaction(int transactionKey) 
    throws NoSuchTransactionKeyException {
      return _warehouse.getTransaction(transactionKey);
  }

  public void toggleProductNotifications(String partnerKey, String productKey)
    throws NoSuchProductKeyException, NoSuchPartnerKeyException {
      _warehouse.toggleProductNotifications(partnerKey, productKey);
      _saveFlag = false;
  }

  public Collection<Acquisition> getPartnerAcquisitions(String partnerKey)
    throws NoSuchPartnerKeyException {
      return _warehouse.getPartnerAcquisitions(partnerKey);
  }

  public Collection<Sale> getPartnerSales(String partnerKey)
    throws NoSuchPartnerKeyException {
      return _warehouse.getPartnerSales(partnerKey); 
  }

  public Collection<Transaction> getPaymentsByPartner(String partnerKey)
    throws NoSuchPartnerKeyException {
      return _warehouse.getPaymentsByPartner(partnerKey);
  }

  public Collection<Batch> getProductBatchesUnderGivenPrice(double priceCap) {
    return _warehouse.getProductBatchesUnderGivenPrice(priceCap);
  }

  public void receivePayment(int transactionKey)
    throws NoSuchTransactionKeyException {
      _warehouse.receivePayment(transactionKey);
      _saveFlag = false;
  }

  public void registerProduct(String productKey, int stock) {
    _warehouse.registerProduct(productKey, stock);
    _saveFlag = false;
  }

  public void registerProduct(String productKey, int stock, Map<String, Integer> ingredients, double alpha)
    throws NoSuchProductKeyException {
    _warehouse.registerProduct(productKey, stock, ingredients, alpha);
    _saveFlag = false;
  }

  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount)
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      _warehouse.registerSaleTransaction(partnerKey, deadline, productKey, amount);
      _saveFlag = false;
  }

  public void registerAcquisitionTransaction(String partnerKey, String productKey, double price, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException {
      _warehouse.registerAcquisitionTransaction(partnerKey, productKey, price, amount);
      _saveFlag = false;
    }

  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) 
    throws NoSuchPartnerKeyException, NoSuchProductKeyException, NotEnoughStockException {
      _warehouse.registerBreakdownTransaction(partnerKey, productKey, amount);
      _saveFlag = false;
    }

  public Collection<Notification> readPartnerNotifications(String key) 
    throws NoSuchPartnerKeyException {
      _saveFlag = false;
      return _warehouse.readPartnerNotifications(key);
    }

}
