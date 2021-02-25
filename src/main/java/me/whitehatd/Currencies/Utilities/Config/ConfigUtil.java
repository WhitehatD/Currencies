package me.whitehatd.Currencies.Utilities.Config;


import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.Utils;

import java.util.ArrayList;

public class ConfigUtil {

    public static String str(String configSec){
        return ChatUtil.c(Utils.getConfig().getString(configSec));
    }

    public static Integer num(String configSec){
        return Utils.getConfig().getInt(configSec);
    }

    public static ArrayList<String> arr(String configSec){
        ArrayList<String> ls = new ArrayList<>();
        Utils.getConfig().getStringList(configSec).forEach(str -> ls.add(ChatUtil.c(str)));
        return ls;
    }



}
