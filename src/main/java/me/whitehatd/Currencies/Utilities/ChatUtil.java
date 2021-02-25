package me.whitehatd.Currencies.Utilities;


import me.whitehatd.Currencies.Utilities.Config.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class ChatUtil {


    public static String c(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void s(CommandSender player, String message){
        player.sendMessage(c(message));
    }

    public static void cs(CommandSender player, String configSec, Function<String, String> addPlaceholders){
        String str = c(ConfigUtil.str(configSec));
        player.sendMessage(addPlaceholders.apply(str));
    }

    public static void cs(CommandSender player, String configSec){
        player.sendMessage(c(ConfigUtil.str(configSec)));
    }



}
