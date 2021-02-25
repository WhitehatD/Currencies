package me.whitehatd.Currencies.Commands.Command;

import me.whitehatd.Currencies.Commands.CommandBase;
import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Pay extends CommandBase {

    public Pay(){
        super("pay", true, "currencies.pay");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        switch (args.length){
            case 3:{
                Player target = Bukkit.getPlayer(args[0]);
                if(target==null||!CurrencyUtil.playerExists(target.getUniqueId().toString(), args[2].toLowerCase())){
                    ChatUtil.cs(player, "invalid-player");
                    return;
                }
                if(!NumberUtils.isNumber(args[1]) || (NumberUtils.isNumber(args[1]) && NumberUtils.toLong(args[1]) < 0)){
                    ChatUtil.cs(sender, "invalid-number");
                    return;
                }
                if(!CurrencyUtil.getCurrencies().contains(args[2].toLowerCase())){
                    ChatUtil.cs(sender, "invalid-currency");
                    return;
                }
                if(target.getName().equals(player.getName())){
                    ChatUtil.cs(player, "pay.yourself");
                    return;
                }
                Long amount = NumberUtils.toLong(args[1]);
                if(amount > CurrencyUtil.getBalance(player.getUniqueId().toString(), args[2])){
                    ChatUtil.cs(player, "pay.no-money");
                    return;
                }
                CurrencyUtil.setBalance(player.getUniqueId().toString(), args[2],
                        CurrencyUtil.getBalance(player.getUniqueId().toString(), args[2]) - amount);
                CurrencyUtil.setBalance(target.getUniqueId().toString(), args[2],
                        CurrencyUtil.getBalance(target.getUniqueId().toString(), args[2]) + amount);
                ChatUtil.cs(sender, "pay.paid", s ->
                        s.replaceAll("%player%", target.getName())
                                .replaceAll("%currency%", args[2])
                                .replaceAll("%amount%", amount+"")
                                .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[2])));
                ChatUtil.cs(target, "pay.receive", s ->
                        s.replaceAll("%player%", player.getName())
                                .replaceAll("%currency%", args[2])
                                .replaceAll("%amount%", amount+"")
                                .replaceAll("%sign%", CurrencyUtil.getCurrencySign(args[2])));
                return;
            }
            default: {
                ChatUtil.cs(player, "pay.usage");
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
