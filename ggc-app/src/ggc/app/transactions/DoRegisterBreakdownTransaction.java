package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.NoSuchProductKeyException;
import ggc.exceptions.NotEnoughStockException;
import ggc.exceptions.NoSuchPartnerKeyException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.app.exceptions.UnavailableProductException;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.registerBreakdownTransaction(
        stringField("partnerKey"),
        stringField("productKey"),
        integerField("amount")
      );
    } catch (NoSuchPartnerKeyException e) {
      throw new UnknownPartnerKeyException(e.getKey());
    } catch (NoSuchProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (NotEnoughStockException e) {
      throw new UnavailableProductException(
        e.getKey(),
        e.getRequested(),
        e.getAvailable()
      );
    }
  }

}
