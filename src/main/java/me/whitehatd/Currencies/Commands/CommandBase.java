package me.whitehatd.Currencies.Commands;

import me.whitehatd.Currencies.Utilities.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {

    String commandName, permission;
    boolean playerOnly;

    public CommandBase(String commandName, boolean playerOnly, String permission){
        this.commandName = commandName;
        this.playerOnly = playerOnly;
        this.permission = permission;
    }

    public CommandBase(String commandName, boolean playerOnly){
        this.commandName = commandName;
        this.playerOnly = playerOnly;
    }

    public abstract void execute(CommandSender sender, Command cmd, String[] args);

    public abstract List<String> tab(CommandSender sender, Command cmd, String[] args);


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(permission != null && permission.length() > 1 && !commandSender.hasPermission(permission)){
            ChatUtil.cs(commandSender, "no-perm");
            return true;
        }
        if(playerOnly) {
            if(commandSender instanceof Player) {
                Player executor = (Player) commandSender;
                execute(executor, command, strings);
            }else commandSender.sendMessage(ChatUtil.c("&eThis is a player-only command."));
        } else execute(commandSender, command, strings);
        return true;
    }

    public String getCommandName(){
        return commandName;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return tab(commandSender, command, args);
    }
}
