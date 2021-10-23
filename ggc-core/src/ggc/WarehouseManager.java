package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.Collection;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  private boolean _saveFlag = true;

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

     _saveFlag = true;
     ObjectOutputStream oos = new ObjectOutputStream(
       new BufferedOutputStream(
         new FileOutputStream(
           getFilename()
         )
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

      ObjectInputStream ois = 
        new ObjectInputStream(
          new BufferedInputStream(
            new FileInputStream(
              filename
            )
          )
        );
      
      _warehouse = (Warehouse)ois.readObject();
      ois.close();

      setFilename(filename);
      
    } catch(IOException | ClassNotFoundException e) {
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
    } catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
	    throw new ImportFileException(textfile);
    }
  }

  public String getFilename() {
    return _filename;
  }

  public void setFilename(String filename) {
    _filename = filename;
  }

  public boolean hasFileAssociated() {
    return !_filename.equals("");
  }

  public int getDate() {
    return _warehouse.getDate();
  }

  public void updateDate(int date) throws NoSuchDateException {
    _saveFlag = false;
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
      _saveFlag = false;
      _warehouse.registerPartner(key, name, address);
  }

  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    return _warehouse.getPartner(key);
  }

  public Map<String, Partner> getPartners() {
    return _warehouse.getPartners();
  }

  public Collection<String> getPartnersCollection() {
    return _warehouse.getPartnersCollection();
  }

  public void clearNotifications(String key) {
    _saveFlag = false;
    _warehouse.clearNotifications(key);
  }

}
