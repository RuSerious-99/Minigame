package com.ruserious99.minigame.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap{
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File WorldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(WorldFolder, worldName);

        if(loadOnInit) load();
    }

    @Override
    public boolean load() {
        //if(isLoaded()) return true;
        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName());

        try{
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        }catch(IOException e){
            Bukkit.getLogger().severe("failed to load GameMap from source folder" + sourceWorldFolder.getName());
            e.printStackTrace();
            return false;
        }
        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
        if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false);

        return isLoaded();
    }

    @Override
    public void unload() {
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);

        File dataFile = new File((activeWorldFolder.getName() + "data"));
        FileUtil.deleteWorldFolder(dataFile);
        if(activeWorldFolder != null) FileUtil.deleteWorldFolder(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }
}

