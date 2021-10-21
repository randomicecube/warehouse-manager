package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

import ggc.app.exceptions.DuplicatePartnerKeyException;
import ggc.exceptions.PartnerKeyAlreadyUsedException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
    addStringField("name", Prompt.partnerName());
    addStringField("address", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    try {
      _receiver.registerPartner(
        stringField("key"),
        stringField("name"),
        stringField("address")
      );
    } catch (PartnerKeyAlreadyUsedException e) {
      throw new DuplicatePartnerKeyException(e.getKey());
    }
  }

}
