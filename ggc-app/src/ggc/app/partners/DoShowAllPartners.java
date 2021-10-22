package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Partner;
import ggc.WarehouseManager;

import java.util.Map;

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

  DoShowAllPartners(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PARTNERS, receiver);
  }

  @Override
  public void execute() {
    Map<String, Partner> partners = _receiver.getPartners();
    
    for (String key: partners.keySet()) {
      _display.popup(partners.get(key));
    }
  }

}
