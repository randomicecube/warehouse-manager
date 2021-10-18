package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.io.StringReader;
import java.util.Scanner;

import ggc.WarehouseManager;
//FIXME import classes

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    //FIXME add command field
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    Scanner days = new Scanner(System.in);

    //alguma cena para ler do terminal
    try{
      days.nextInt();
      this.updateDate(days);
      //dias tem de ser inteiro >= 0 
    }

    catch(Exception e){
      //se nao for manda o erro
      throw new InvalidDateException(e);
    }

  }

}
