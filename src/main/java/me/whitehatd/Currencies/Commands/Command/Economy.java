package me.whitehatd.Currencies.Commands.Command;

import me.whitehatd.Currencies.Commands.CommandBase;
import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class Economy extends CommandBase {

    public Economy(){
        super("economy", false, "currencies.eco");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        switch (args.length){
            case 4: {
                if(!CurrencyUtil.getCurrencies().contains(args[2].toLowerCase())){
                    ChatUtil.cs(sender, "invalid-currency");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(!CurrencyUtil.playerExists(target.getUniqueId().toString(), args[2].toLowerCase())){
                    ChatUtil.cs(sender, "invalid-player");
                    return;
                }
                if(!NumberUtils.isNumber(args[3]) || (NumberUtils.isNumber(args[3]) && NumberUtils.toLong(args[3]) < 0)){
                    ChatUtil.cs(sender, "invalid-number");
                    return;
                }
                String uuid = target.getUniqueId().toString();
                Long amount = NumberUtils.toLong(args[3]);
                switch (args[0].toLowerCase()){
                    default: {
                        ChatUtil.cs(sender, "economy.usage");
                        return;
                    }
                    case "set":{
                        CurrencyUtil.setBalance(uuid, args[2], amount);
                        ChatUtil.cs(sender, "economy.success.set", s ->
                                s.replaceAll("%player%", target.getName())
                                 .replaceAll("%currency%", args[2])
                                 .replaceAll("%amount%", CurrencyUtil.getBalance(uuid, args[2])+"")
                                 .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[2])));
                        return;
                    }
                    case "add":{
                        CurrencyUtil.setBalance(uuid, args[2], CurrencyUtil.getBalance(uuid, args[2]) + amount);
                        ChatUtil.cs(sender, "economy.success.add", s ->
                                s.replaceAll("%player%", target.getName())
                                        .replaceAll("%currency%", args[2])
                                        .replaceAll("%amount%", amount+"")
                                        .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[2])));
                        return;
                    }
                    case "remove":{
                        if(CurrencyUtil.getBalance(uuid, args[2]) - amount < 0){
                            ChatUtil.cs(sender, "economy.no-money");
                            return;
                        }
                        CurrencyUtil.setBalance(uuid, args[2], CurrencyUtil.getBalance(uuid, args[2]) - amount);
                        ChatUtil.cs(sender, "economy.success.remove", s ->
                                s.replaceAll("%player%", target.getName())
                                        .replaceAll("%currency%", args[2])
                                        .replaceAll("%amount%", amount+"")
                                        .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[2])));
                        return;
                    }
                }
            }
            default: {
                ChatUtil.cs(sender, "economy.usage");
                return;
            }
        }
    }

    @Override
    public List<String> tab(CommandSender sender, Command cmd, String[] args) {
        if(args.length == 3){
            return CurrencyUtil.getCurrencies()
                    .stream()
                    .filter(def -> def.startsWith(args[2]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
