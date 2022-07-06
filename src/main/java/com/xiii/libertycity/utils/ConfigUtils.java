package com.xiii.libertycity.utils;

import com.xiii.libertycity.LAC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigUtils {

    LAC lac;
    FileConfiguration config;
    File checksFile = null;
    FileConfiguration checks = null;

    public ConfigUtils(LAC lac) {
        this.lac = lac;
        this.config = lac.getConfig();
        saveDefaults("config");
        saveDefaults("checks");
    }


    public void reloadConfigs() {
        lac.reloadConfig();
        if(checksFile == null) {
            checksFile = new File(lac.getDataFolder(), "checks.yml");
        }
        checks = YamlConfiguration.loadConfiguration(checksFile);

        InputStream checksfilestream = lac.getResource("checks.yml");
        if(checksfilestream != null) {
            YamlConfiguration dconfig = YamlConfiguration.loadConfiguration(new InputStreamReader(checksfilestream));
            checks.setDefaults(dconfig);
        }
    }


    public boolean getBooleanFromConfig(String configName, String value, boolean defaultvalue) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                reloadConfigs();
            }
            return config.getBoolean(value);

        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            return checks.getBoolean(value);
        }
        return defaultvalue;
    }
    public String getStringFromConfig(String configName, String value, String defaultvalue) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                lac.reloadConfig();
            }
            return config.getString(value);

        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            return checks.getString(value);
        }
        return defaultvalue;
    }

    public String getConvertedStringFromConfig(String configName, Player player, String value) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                lac.reloadConfig();
            }

            String s = config.getString(value);
            if(s != null) {
                String s2 = config.getString("prefix");
                s2.replace("\"", "");
                s.replace("%lac PREFIX%", s2);
                s.replace("%player%", player.getName());
                s.replace("\"", "");

                return s;
            }

        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            if (config == null) {
                lac.reloadConfig();
            }

            String s = checks.getString(value);
            if(s != null) {
                String s2 = config.getString("prefix");
                s2.replace("\"", "");
                s.replace("%lac PREFIX%", s2);
                s.replace("%player%", player.getName());
                s.replace("\"", "");
                return s;
            }
        }
        return "";
    }
    public int getIntFromConfig(String configName, String value, int defaultvalue) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                lac.reloadConfig();
            }
            return config.getInt(value);

        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            return checks.getInt(value);
        }
        return defaultvalue;
    }
    public double getDoubleFromConfig(String configName, String value, double defaultvalue) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                lac.reloadConfig();
            }
            return config.getDouble(value);

        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            return checks.getDouble(value);
        }
        return defaultvalue;
    }


    public FileConfiguration getConfig(String configName) {
        if(configName.equalsIgnoreCase("config")) {
            if (config == null) {
                lac.reloadConfig();
            }
            return config;
        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null) {
                reloadConfigs();
            }
            return checks;
        }
        return null;

    }

    public void saveConfig(String configName) {
        if(configName.equalsIgnoreCase("config")) {
            if(config == null) return;
            lac.saveDefaultConfig();
        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checks == null || checksFile == null) return;
            try {
                checks.save(checksFile);
            } catch (IOException e) {
                lac.getLogger().log(Level.SEVERE, "Could not save checks.yml!");
            }
        }
    }

    public void saveDefaults(String configName) {
        if(configName.equalsIgnoreCase("config")) {
            if(config == null) return;
            lac.saveDefaultConfig();
        }
        if(configName.equalsIgnoreCase("checks")) {
            if(checksFile == null) checksFile = new File(lac.getDataFolder(), "checks.yml");

            if(!checksFile.exists()) {
                lac.saveResource("checks.yml", false);
            }
        }
    }

}
