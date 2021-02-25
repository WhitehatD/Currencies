package me.whitehatd.Currencies.Utilities.Config;

import me.whitehatd.Currencies.Currencies;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Config {

    private FileConfiguration config = null;
    private File configfile = null;
    private String name = "";

    public Config(String name){
        this.name = name;
    }

    public void reload() {
        if (config == null)
            configfile = new File(Currencies.getInstance().getDataFolder(), name);
        config = YamlConfiguration.loadConfiguration(configfile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(Currencies.getInstance().getResource(name)), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }

    public FileConfiguration get() {
        if (config == null)
            reload();
        return config;
    }

    public void save() {
        if (config == null || configfile == null)
            return;
        try {
            get().save(configfile);
        } catch (IOException ex) {
        }
    }

    public void saveDefault() {
        if (config == null)
            configfile = new File(Currencies.getInstance().getDataFolder(), name);
        if (!configfile.exists())
            Currencies.getInstance().saveResource(name, false);
    }
}
