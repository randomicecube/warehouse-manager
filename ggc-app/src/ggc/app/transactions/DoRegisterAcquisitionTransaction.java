package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.Map;

import ggc.WarehouseManager;
import ggc.exceptions.NoSuchProductKeyException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      // stuff - mandar p WM
    } catch (NoSuchPartnerKeyException e) {
      throw new UnknownPartnerKeyException(e.getKey());
    } catch (NoSuchProductKeyException eOutside) {
      if (Form.confirm(Prompt.addRecipe())) {
        int numberOfComponents = Form.requestInteger(Prompt.numberOfComponents());
        int alpha = Form.requestInteger(Prompt.alpha());
        Map<String, Integer> ingredients = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < numberOfComponents; i++) {
          try {
            // está mal - deviamos verificar _sempre_ in the meantime
            // se o produto existe
            ingredients.put(
              Form.requestString(Prompt.productKey()),
              Form.requestString(Prompt.amount())
            );
          } catch (NoSuchProductKeyException eInside) {
            throw new UnknownProductKeyException(eInside.getKey());
          }
        }
      } else {

      }
    }

  }

}
