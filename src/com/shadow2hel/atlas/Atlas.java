package com.shadow2hel.atlas;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Atlas extends JavaPlugin {
    public CoordsMap coords;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        loadConfig();
        this.getCommand("atlas").setExecutor(new CommandA(this));
    }

    public void loadConfig(){
        coords = new CoordsMap(this);
    }

    public World getAppropriateWorld(World.Environment type){
        World rworld = null;
        for(World world : getServer().getWorlds()){
            if(world.getEnvironment() == type){
                rworld = world;
            }
        }

        return rworld;
    }
}
