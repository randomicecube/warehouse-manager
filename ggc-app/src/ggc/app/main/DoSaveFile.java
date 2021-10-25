package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import pt.tecnico.uilib.forms.Form;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {

    try {
      _receiver.save();
    } catch (MissingFileAssociationException mfe) {
      boolean saved = false;
      while (!saved) {
        try {
          _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
          saved = true;
        } catch (MissingFileAssociationException | IOException e) {
          // happens when the user clicks "Enter" before writing anything
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
