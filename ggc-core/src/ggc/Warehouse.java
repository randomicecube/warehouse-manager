package ggc;

import java.io.*;
import ggc.exceptions.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;

import java.util.TreeSet;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Warehouse's current date */
  private int _date = 0;

  /** Warehouse's current available balance */
  private double _availableBalance = 0;

  /** Warehouse's current accounting balance */
  private double _accountingBalance = 0;

  /** All partners associated with the warehouse */
  private Map<String, Partner> _partners = new TreeMap<String, Partner>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */   
  void importFile(String txtfile) throws IOException, BadEntryException /* TODO? - maybe add other exceptions */ {
    try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
      String s;
      while ((s = in.readLine()) != null) {
        String line = new String(s.getBytes(), "UTF-8");

        String[] fields = line.split("\\|");

        switch (fields[0]) {
          case "PARTNER" -> registerPartner(
            fields[1],
            fields[2],
            fields[3]
          );
          // TODO case "BATCH_S"
          // TODO case "BATCH_M"
          default -> throw new BadEntryException(fields[0]); 
        }

      }
      // TODO - FIX CATCHES
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    } catch (PartnerKeyAlreadyUsedException e) {
      e.printStackTrace();
      // TODO - change this to throwing an actual exception
    }
  }

  /** @return current warehouse date */
  public int getDate() {
    return _date;
  }

  /**
   * update warehouse's date
   * @param days days to add to current date
   * @throws NoSuchDateException
   */
  public void updateDate(int days) throws NoSuchDateException {
    if (days <= 0) {
      throw new NoSuchDateException(days);
    }
    _date += days;
  }

  /** @return warehouse available balance */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  /** @return warehouse accounting balance */
  public double getAccountingBalance() {
    return _accountingBalance;
  }

  /**
   * register a partner to be associated with the warehouse
   * @param key partner's key
   * @param name partner's name
   * @param address partner's address
   * @throws PartnerKeyAlreadyUsedException
   */
  public void registerPartner(String key, String name, String address)
    throws PartnerKeyAlreadyUsedException {
      if (_partners.get(key) != null) {
        throw new PartnerKeyAlreadyUsedException(key);
      }      
      _partners.put(key, new Partner(key, name, address));
  }

  /**
   * get a partner, given their key
   * @param key
   * @return desired partner
   * @throws NoSuchPartnerKeyException
   */
  public Partner getPartner(String key) throws NoSuchPartnerKeyException {
    Partner partner = _partners.get(key);
    if (partner == null) {
      throw new NoSuchPartnerKeyException(key);
    }
    return partner;
  }

  /** @return all partners associated with the warehouse */
  public Map<String, Partner> getPartners() {
    return _partners;
  }
  
  /** @return a Collection with all partners associated with the warehouse */
  public Collection<String> getPartnersCollection() {
    return getPartners()
      .values()
      .stream()
      .map(partner -> partner.toString())
      .collect(Collectors.toList());
  }

  /**
   * clear a partner's given unread notifications
   * @param key partner's key
   */
  public void clearNotifications(String key) {
    _partners.get(key).getNotifications().clear();
  }

}
