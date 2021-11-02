package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    // TODO - is it an Integer field (?)
    addRealField("limit", Prompt.priceLimit());
  }

  @Override
  public void execute() throws CommandException {
    _display.popup(_receiver.getProductBatchesUnderGivenPrice(realField("limit")));
  }

}
