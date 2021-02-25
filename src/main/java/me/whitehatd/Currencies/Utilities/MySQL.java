package me.whitehatd.Currencies.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class MySQL {

    public static String host = Utils.getConfig().getString("mysql.host");
    public static String port = Utils.getConfig().getString("mysql.port");
    public static String database = Utils.getConfig().getString("mysql.database");
    public static String username = Utils.getConfig().getString("mysql.username");
    public static String password = Utils.getConfig().getString("mysql.password");
    public static Connection con;


    public static void connect(){
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "?useSSL=false", username, password);
                ChatUtil.s(Bukkit.getConsoleSender(), "&a[Currencies] MySQL connection started successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("CREATE DATABASE IF NOT EXISTS " + database);
            ps.executeUpdate();
            con.setCatalog(database);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                ChatUtil.s(Bukkit.getConsoleSender(), "&c[Currencies] MySQL connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (con != null);
    }

    public static Connection getConnection() {
        return con;
    }
}
