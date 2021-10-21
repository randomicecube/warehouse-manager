package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.io.StringReader;
import java.util.Scanner;

import ggc.WarehouseManager;

import ggc.app.exceptions.InvalidDateException;
import ggc.exceptions.NoSuchDateException;

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("date", Prompt.daysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.updateDate(integerField("date"));
    } catch (NoSuchDateException e) {
      throw new InvalidDateException(e.getDate());
    }

  }

}
