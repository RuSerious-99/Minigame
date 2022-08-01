package com.ruserious99.minigame.managers;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataMgr {

    private static FileConfiguration customFile;
    private static File file;

    public static void setupConfig() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Minigame")).getDataFolder(), "data.yml");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static  FileConfiguration getConfig() {
        return customFile;
    }

    public static void saveConfig() {
        try {
            System.out.println("success saving file!!!!!!!!!!!!!!!!!!!");
            getConfig().save(file);
        } catch (IOException e) {
            System.out.println("'Error saving file");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
        }
    }
