package me.whitehatd.Currencies.Commands;

import me.whitehatd.Currencies.Commands.Command.Balance;
import me.whitehatd.Currencies.Commands.Command.Economy;
import me.whitehatd.Currencies.Commands.Command.EconomyNPC;
import me.whitehatd.Currencies.Commands.Command.Pay;

import java.util.ArrayList;

public class CommandRegister {

    public ArrayList<CommandBase> commandBases = new ArrayList<>();

    public CommandRegister(){
        commandBases.add(new Balance());
        commandBases.add(new Economy());
        commandBases.add(new Pay());
        commandBases.add(new EconomyNPC());
    }
}
