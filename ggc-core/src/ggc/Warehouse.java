package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;

import javax.lang.model.element.UnknownAnnotationValueException;

import java.util.HashMap;

import java.util.Collection;
import java.util.stream.Collectors;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  private int _date = 0;

  private double _availableBalance = 0;

  private double _accountingBalance = 0;

  private Map<String, Partner> _partners = new HashMap<String, Partner>();

  private Map<String, Product> _products = new HashMap<String, Product>();

  private Map<String, Batch> _batches = new HashMap<String, Batch>();

  // FIXME define attributes
  // FIXME define contructor(s)
  // FIXME define methods

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
   
  void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
    //FIXME implement method
  }

  public int getDate() {
    return _date;
  }

  public void updateDate(int date) throws NoSuchDateException {
    if (date <= 0) {
      throw new NoSuchDateException(date);
    }
    _date += date;
  }

  public double getAvailableBalance() {
    return _availableBalance;
  }

  public double getAccountingBalance() {
    return _accountingBalance;
  }

  public void registerPartner(String key, String name, String address)
    throws PartnerKeyAlreadyUsedException {
      if (_partners.get(key) != null) {
        throw new PartnerKeyAlreadyUsedException(key);
      }      
      _partners.put(key, new Partner(key, name, address));
  }

  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    Partner partner = _partners.get(key);
    if (partner == null) {
      throw new NoSuchPartnerKeyException(key);
    }
    return partner;
  }

  public Map<String, Partner> getPartners() {
    return _partners;
  }

  public Map<String, Product> getProducts(){
    return _products;
  }

  public Map<String, Batch> getBatches(){
    return _batches;
  }

  public Collection<String> getProductsCollection(){
    return getProducts().values().stream().map(product -> product.toString()).collect(Collectors.toList());
  }

  public Collection<String> getBatchesCollection(){
    return getBatches().values().stream().map(batch -> batch.toString()).collect(Collectors.toList());
  }

}
