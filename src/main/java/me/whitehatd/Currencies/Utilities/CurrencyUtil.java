package me.whitehatd.Currencies.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyUtil {

    public static void deleteCurrency(String currencyName) {
        Utils.sqlExec("DELETE FROM " + currencyName);
    }

    public static void setupCurrencies(){
        for(String currency : getCurrencies())
        Utils.sqlExec("CREATE TABLE IF NOT EXISTS "+currency.toLowerCase() +" (UUID varchar(100), Username varchar(100), Balance integer)");
    }

    public static boolean playerExists(String uuid, String currencyName){
        try(PreparedStatement ps =
                    MySQL.getConnection().prepareStatement("SELECT Balance FROM " + currencyName.toLowerCase() + " WHERE UUID = \"" + uuid + "\"")){
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next())
                return resultSet.getLong(1)>=0;
        }catch (SQLException exc){
            exc.printStackTrace();
        }
        return false;
    }

    public static Long getBalance(String uuid, String currencyName){
        try(PreparedStatement ps =
                    MySQL.getConnection().prepareStatement("SELECT Balance FROM " + currencyName.toLowerCase() + " WHERE UUID = \""+ uuid + "\"")){
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next())
                return resultSet.getLong(1);
        }catch (SQLException exc){
            exc.printStackTrace();
        }
        return null;
    }

    public static void setBalance(String uuid, String currencyName, Long amount){
        Utils.sqlExec("UPDATE " + currencyName.toLowerCase() + " SET Balance = " + amount + " WHERE UUID = \"" + uuid + "\"");
    }

    public static List<String> getCurrencies(){
        List<String> c = new ArrayList<>();
        c.addAll(Utils.getConfig().getConfigurationSection("currencies").getKeys(false));
        return c;
    }

    public static String getCurrencySign(String currencyName){
        return Utils.getConfig().getString("currencies." + currencyName.toLowerCase() + ".sign");
    }

    public static void registerPlayer(String uuid, String playerName){
        for(String currency : getCurrencies())
        Utils.sqlExec("INSERT INTO " + currency + "(UUID, Username, Balance)\n" +
                            "SELECT * FROM (SELECT \"" + uuid + "\", \"" + playerName + "\", 0) AS tmp\n" +
                            "WHERE NOT EXISTS (\n" +
                            "SELECT UUID FROM " + currency + " WHERE UUID = \"" + uuid + "\"\n" +
                            ") LIMIT 1");
    }
}
