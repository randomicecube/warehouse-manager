package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Partner;
import ggc.WarehouseManager;
import ggc.Notification;

import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.NoSuchPartnerKeyException;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      Partner partner = _receiver.getPartner(stringField("key"));
      _display.popup(partner);
      for (Notification n: partner.getNotifications()) {
        _display.popup(n);
      }
      partner.clearNotifications();
    } catch (NoSuchPartnerKeyException e) {
      throw new UnknownPartnerKeyException(e.getKey());
    }

  }

}
