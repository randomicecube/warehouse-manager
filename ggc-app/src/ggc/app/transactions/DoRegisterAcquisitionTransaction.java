package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.Map;
import java.util.LinkedHashMap;

import ggc.WarehouseManager;
import ggc.exceptions.NoSuchProductKeyException;
import ggc.exceptions.NoSuchPartnerKeyException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;

import pt.tecnico.uilib.forms.Form;

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
      _receiver.registerAcquisitionTransaction(
        stringField("partnerKey"),
        stringField("productKey"),
        realField("price"),
        integerField("amount")
      );
    } catch (NoSuchPartnerKeyException e) {
      throw new UnknownPartnerKeyException(e.getKey());
    } catch (NoSuchProductKeyException eOutside) {
      if (Form.confirm(Prompt.addRecipe())) {
        int numberOfComponents = Form.requestInteger(Prompt.numberOfComponents());
        double alpha = Form.requestReal(Prompt.alpha());
        Map<String, Integer> ingredients = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < numberOfComponents; i++) {
          ingredients.put(
            Form.requestString(Prompt.productKey()),
            Form.requestInteger(Prompt.amount())
          );
        }
        try {
          _receiver.registerProduct(
            stringField("productKey"),
            integerField("amount"),
            ingredients,
            alpha
          );
        } catch (NoSuchProductKeyException e) {
          throw new UnknownProductKeyException(e.getKey());
        }
        // TODO -> IN CORE try -> catch noSuchProductKeyException
      } else {
        _receiver.registerProduct(
          stringField("productKey"),
          integerField("amount")
        );
      }

      try {
        _receiver.registerAcquisitionTransaction(
          stringField("partnerKey"),
          stringField("productKey"),
          realField("price"),
          integerField("amount")
        );
      } catch (NoSuchPartnerKeyException | NoSuchProductKeyException e) {
        e.printStackTrace(); // will never happen
      }
    }

  }

}
