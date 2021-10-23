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

  private int _date = 0;

  private double _availableBalance = 0;

  private double _accountingBalance = 0;

  private Map<String, Partner> _partners = new TreeMap<String, Partner>();

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
   
  void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
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
  
  public Collection<String> getPartnersCollection() {
    return getPartners()
      .values()
      .stream()
      .map(partner -> partner.toString())
      .collect(Collectors.toList());
  }

  public void clearNotifications(String key) {
    _partners.get(key).getNotifications().clear();
  }

}
