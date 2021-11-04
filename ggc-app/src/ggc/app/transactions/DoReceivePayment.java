package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.exceptions.NoSuchTransactionKeyException;
import ggc.app.exceptions.UnknownTransactionKeyException;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("key", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.receivePayment(integerField("key"));
    } catch (NoSuchTransactionKeyException e) {
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }

}
