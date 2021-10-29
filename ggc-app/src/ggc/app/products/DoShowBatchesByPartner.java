package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.NoSuchPartnerKeyException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _display.popup(_receiver.getBatchesByPartner(stringField("key")));
    } catch (NoSuchPartnerKeyException e) {
      throw new UnknownPartnerKeyException(e.getKey());
    }
  }

}
