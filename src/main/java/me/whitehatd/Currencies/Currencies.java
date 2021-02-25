package me.whitehatd.Currencies;


import com.samjakob.spigui.SpiGUI;
import me.whitehatd.Currencies.Commands.CommandBase;
import me.whitehatd.Currencies.Commands.CommandRegister;
import me.whitehatd.Currencies.Events.EventRegister;
import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.Config.Config;
import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import me.whitehatd.Currencies.Utilities.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Currencies extends JavaPlugin {

    public static Config config;
    private static Currencies instance;
    public static SpiGUI spiGUI;

    @Override
    public void onEnable(){
        instance = this;

        config = new Config("config.yml");
        config.saveDefault();

        CommandRegister cmdReg = new CommandRegister();
        for(CommandBase cmdBase : cmdReg.commandBases) {
            getCommand(cmdBase.getCommandName()).setExecutor(cmdBase);
            getCommand(cmdBase.getCommandName()).setTabCompleter(cmdBase);
        }

        EventRegister evtReg = new EventRegister();
        for(Listener listener : evtReg.events){
            Bukkit.getPluginManager().registerEvents(listener, getInstance());
        }

        MySQL.connect();
        CurrencyUtil.setupCurrencies();

        spiGUI = new SpiGUI(this);

        ChatUtil.s(Bukkit.getConsoleSender(), "&aCurrencies enabled! [Made by WhitehatD#6615]");
    }

    @Override
    public void onDisable(){
        MySQL.disconnect();
    }

    public static Currencies getInstance(){
        return instance;
    }

}
