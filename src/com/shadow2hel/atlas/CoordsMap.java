package com.shadow2hel.atlas;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class CoordsMap {
    private HashMap<String, Location> earthmap;
    private HashMap<String, Location> hellmap;
    private HashMap<String, Location> endmap;
    private Atlas main;

    CoordsMap(Atlas main) { //test
        this.main = main;
        this.earthmap = new HashMap<>();
        this.hellmap = new HashMap<>();
        this.endmap = new HashMap<>();
        loadConfig();
    }

    private void loadConfig(){
        if(main.getConfig().getConfigurationSection("Worlds") != null){
            if(main.getConfig().getConfigurationSection("Worlds.Overworld") != null){
                Map<String, ?> map = main.getConfig().getConfigurationSection("Worlds.Overworld").getValues(true);
                addToMap(main.getAppropriateWorld(World.Environment.NORMAL), map, earthmap);
            } else {
                main.getConfig().createSection("Worlds.Overworld");
            }

            if(main.getConfig().getConfigurationSection("Worlds.Nether") != null){
                Map<String, ?> map = main.getConfig().getConfigurationSection("Worlds.Nether").getValues(true);
                addToMap(main.getAppropriateWorld(World.Environment.NETHER), map, hellmap);
            } else {
                main.getConfig().createSection("Worlds.Nether");
            }

            if(main.getConfig().getConfigurationSection("Worlds.End") != null){
                Map<String, ?> map = main.getConfig().getConfigurationSection("Worlds.End").getValues(true);
                addToMap(main.getAppropriateWorld(World.Environment.THE_END), map, endmap);
            } else {
                main.getConfig().createSection("Worlds.End");
            }

        } else {
            main.getConfig().createSection("Worlds");
        }

    }

    private void addToMap(World world, Map<String, ?> map, HashMap<String, Location> hashmap) {
        map.forEach((s, o) -> {
            int[] coordinates = new int[3];
            String[] strings = o.toString().split(";");
            for(int i = 0; i<strings.length; i++){
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                    Number number = format.parse(strings[i]);
                    coordinates[i] = number.intValue();
                } catch(ParseException ignored){
                }
            }
            Location newxyz = new Location(world, coordinates[0], coordinates[1], coordinates[2]);
            hashmap.put(s, newxyz);
        });
    }

    void addToCoords(Player pl, World.Environment type, String name, Location xyz){
        switch(type){
            case NORMAL:
                List<String> map = new ArrayList<>(earthmap.keySet());
                removeCCoding(map);
                if(!map.contains(removeCCString(name))) {
                    earthmap.put(name, xyz);
                    pl.sendMessage(String.format("Added %s to the list!", CustomColors.filterPlaceholder(name)));
                } else {
                    pl.sendMessage("This name already exists!");
                }
                break;
            case NETHER:
                map = new ArrayList<>(hellmap.keySet());
                removeCCoding(map);
                if(!map.contains(removeCCString(name))){
                    hellmap.put(name, xyz);
                    pl.sendMessage(String.format("Added %s to the list!", CustomColors.filterPlaceholder(name)));
                } else {
                    pl.sendMessage("This name already exists!");
                }
                break;
            case THE_END:
                map = new ArrayList<>(endmap.keySet());
                removeCCoding(map);
                if(!map.contains(removeCCString(name))){
                    endmap.put(name, xyz);
                    pl.sendMessage(String.format("Added %s to the list!", CustomColors.filterPlaceholder(name)));
                } else {
                    pl.sendMessage("This name already exists!");
                }
                break;
        }
        addtoConfig(type, name, xyz);
    }

    private void removeCCoding(List<String> map) {
        for (int i = 0; i < map.size(); i++) {
            for (Map.Entry<String, ChatColor> entry : CustomColors.getMap().entrySet()) {
                String s1 = entry.getKey();
                String nextstr = map.get(i).replaceAll(s1, "");
                map.set(i, nextstr);
            }
        }
    }

    private String removeCCString(String name){
        for (String entry : CustomColors.getMap().keySet()) {
            name = name.replaceAll(entry, "");
        }
        return name;
    }

    private void addtoConfig(World.Environment type, String name, Location xyz){
        switch(type){
            case NORMAL:
                earthmap.forEach((s, location) -> main.getConfig().set("Worlds.Overworld." + s, String.format("%d;%d;%d", (int)Math.round(location.getX()), (int)Math.round(location.getY()), (int)Math.round(location.getZ()))));
                break;
            case NETHER:
                hellmap.forEach((s, location) -> main.getConfig().set("Worlds.Nether." + s, String.format("%d;%d;%d", (int)Math.round(location.getX()), (int)Math.round(location.getY()), (int)Math.round(location.getZ()))));
                break;
            case THE_END:
                endmap.forEach((s, location) -> main.getConfig().set("Worlds.End." + s, String.format("%d;%d;%d", (int)Math.round(location.getX()), (int)Math.round(location.getY()), (int)Math.round(location.getZ()))));
                break;
        }
        main.saveConfig();
    }

    public HashMap<String, Location> getEarthmap() {
        return earthmap;
    }

    public HashMap<String, Location> getHellmap() {
        return hellmap;
    }

    public HashMap<String, Location> getEndmap() {
        return endmap;
    }
}
