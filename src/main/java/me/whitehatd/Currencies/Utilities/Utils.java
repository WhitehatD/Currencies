package me.whitehatd.Currencies.Utilities;

import me.whitehatd.Currencies.Currencies;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static FileConfiguration getConfig(){
        return Currencies.config.get();
    }
    public static void saveConfig(){
        Currencies.config.save();
    }

    public static void sqlExec(String query){
        try(PreparedStatement ps = MySQL.getConnection().prepareStatement(query)){
            ps.executeUpdate();
        }catch (SQLException exc){
            exc.printStackTrace();
        }
    }

}
