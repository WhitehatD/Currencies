package me.whitehatd.Currencies.Commands.Command;

import me.whitehatd.Currencies.Commands.CommandBase;
import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Balance extends CommandBase {

    public Balance(){
        super("balance", true, "currencies.balance");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        switch (args.length){
            case 1: {
                if(!CurrencyUtil.getCurrencies().contains(args[0].toLowerCase())){
                    ChatUtil.cs(player, "balance.invalid");
                    return;
                }
                ChatUtil.cs(player, "balance.success", s ->
                        s.replaceAll("%currency%", args[0])
                         .replaceAll("%balance%", ""+CurrencyUtil.getBalance(player.getUniqueId().toString(), args[0]))
                         .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[0])));
                return;
            }
            case 2: {
                if(!player.hasPermission("currencies.balance.others")){
                    ChatUtil.cs(player, "no-perm");
                    return;
                }
                if(!CurrencyUtil.getCurrencies().contains(args[0].toLowerCase())){
                    ChatUtil.cs(player, "invalid-currency");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(!CurrencyUtil.playerExists(target.getUniqueId().toString(), args[0].toLowerCase())){
                    ChatUtil.cs(player, "invalid-player");
                    return;
                }
                ChatUtil.cs(player, "balance.success-admin", s ->
                        s.replaceAll("%currency%", args[0])
                                .replaceAll("%balance%", ""+CurrencyUtil.getBalance(target.getUniqueId().toString(), args[0]))
                                .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[0]))
                                .replaceAll("%player%", target.getName()));
                return;
            }
            default: {
                if(!player.hasPermission("currencies.balance.others"))
                    ChatUtil.cs(player, "balance.usage");
                else ChatUtil.cs(player, "balance.usage-admin");
                return;
            }
        }
    }

    @Override
    public List<String> tab(CommandSender sender, Command cmd, String[] args) {
        if(args.length == 1){
            return CurrencyUtil.getCurrencies()
                    .stream()
                    .filter(def -> def.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
