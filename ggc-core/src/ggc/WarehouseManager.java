package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;

import java.util.Collection;
import java.util.stream.Collectors;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  //FIXME define other attributes
  //FIXME define constructor(s)
  //FIXME define other methods

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    //FIXME implement serialization method
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    //FIXME implement serialization method
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
	    _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
	    throw new ImportFileException(textfile);
    }
  }

  public int getDate() {
    return _warehouse.getDate();
  }

  public void updateDate(int date) throws NoSuchDateException {
    _warehouse.updateDate(date);
  }

  public double getAvailableBalance() {
    return _warehouse.getAvailableBalance();
  }

  public double getAccountingBalance() {
    return _warehouse.getAccountingBalance();
  }

  public void registerPartner(String key, String name, String address) 
    throws PartnerKeyAlreadyUsedException {
      _warehouse.registerPartner(key, name, address);
  }

  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    return _warehouse.getPartner(key);
  }

  public Map<String, Partner> getPartners() {
    return _warehouse.getPartners();
  }

  public Map<String, Product> getProducts(){
    return _warehouse.getProducts();
  }

  public Map<String, Batch> getBatches(){
    return _warehouse.getBatches();
  }

  public Collection<String> getProductsCollection(){
    return _warehouse.getProductsCollection();
  }

  public Collection<String> getBatchesCollection(){
    return _warehouse.getBatchesCollection();
  }
}
