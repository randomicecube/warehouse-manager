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
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {

    if (_saveFlag) {
      return;
    }

    if (!hasFileAssociated()) {
      throw new MissingFileAssociationException();
    }

    _saveFlag = true;
    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getFilename())));
    oos.writeObject(_warehouse);
    oos.close();

  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    setFilename(filename);
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {

    try {

      ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));

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
    } catch (IOException | BadEntryException | NoSuchPartnerKeyException | NoSuchProductKeyException e) {
      throw new ImportFileException(textfile);
    }
  }

  /** @return current associated filename */
  public String getFilename() {
    return _filename;
  }

  /**
   * set the manager's associated filename (for future save() calls)
   * 
   * @param filename
   */
  public void setFilename(String filename) {
    _filename = filename;
  }

  /** @return whether the manager has a filename associated or not */
  public boolean hasFileAssociated() {
    return !_filename.equals("");
  }

  /**
   * clear a partner's given unread notifications
   * 
   * @param key partner's key
   */
  public void clearNotifications(String key) throws NoSuchPartnerKeyException {
    _saveFlag = false;
    _warehouse.clearNotifications(key);
  }

  /** @return current warehouse date */
  public int getDate() {
    return _warehouse.getDate();
  }

  /**
   * update current warehouse date
   * 
   * @param days
   * @throws NoSuchDateException
   */
  public void updateDate(int days) throws NoSuchDateException {
    _saveFlag = false;
    _warehouse.updateDate(days);
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
   * 
   * @param key     partner's key
   * @param name    partner's name
   * @param address partner's address
   * @throws PartnerKeyAlreadyUsedException
   */
  public void registerPartner(String key, String name, String address) throws PartnerKeyAlreadyUsedException {
    _saveFlag = false;
    _warehouse.registerPartner(key, name, address);
  }

  /**
   * get a partner, given their key
   * 
   * @param key
   * @return desired partner
   * @throws NoSuchPartnerKeyException
   */
  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    return _warehouse.getPartner(key);
  }

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _warehouse.getPartners();
  }

  /** @return all products associated with the warehouse */
  public Map<String, Product> getProducts() {
    return _warehouse.getProducts();
  }

  /** @return a Collection with all the products associated with the warehouse */
  public Collection<String> getProductsCollection() {
    return _warehouse.getProductsCollection();
  }

  /**
   * @return a Collection with all the batches with partners associated with the
   *         warehouse
   */
  public Collection<String> getBatchesCollection() {
    return _warehouse.getBatchesCollection();
  }

  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    return _warehouse.getPartnersCollection();
  }

}
