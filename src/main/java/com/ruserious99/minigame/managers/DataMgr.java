package com.ruserious99.minigame.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import com.ruserious99.minigame.Minigame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class DataMgr {

    private final Minigame plugin;
    private FileConfiguration datacfg;
    private File cfgfile;

    public DataMgr(Minigame plugin) {
        this.plugin = plugin;
        savedefaultcfg();
    }

    public void reloadcfg() {
        if(this.cfgfile == null) {
            this.cfgfile = new File(this.plugin.getDataFolder(), "data.yml");

            this.datacfg = YamlConfiguration.loadConfiguration(this.cfgfile);
            InputStream defaultStream = this.plugin.getResource("data.yml");
            if(defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                this.datacfg.setDefaults(defaultConfig);
            }
        }
    }

    public FileConfiguration getConfig() {
        if(this.datacfg == null)
            reloadcfg();

        return this.datacfg;
    }

    public void savecfg() {
        if(this.datacfg == null || this.cfgfile == null)
            return;


        try {
            this.getConfig().save(cfgfile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save the config file!" + this.cfgfile, e);
        }
    }

    public void savedefaultcfg() {
        if(this.cfgfile == null)
            this.cfgfile = new File(plugin.getDataFolder(), "data.yml");

        if(!this.cfgfile.exists()) {
            this.plugin.saveResource("data.yml", false);
        }
    }


}