package com.shadow2hel.atlas;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class CoordsMap {
    private HashMap<World, HashMap<String, Location>> allmaps;
    private Atlas main;

    CoordsMap(Atlas main) { //test
        this.main = main;
        this.allmaps = new HashMap<>();
        for(World world : main.getServer().getWorlds()){
            allmaps.put(world, new HashMap<>());
        }
        loadConfig();
    }

    private void loadConfig(){
        if(main.getConfig().getConfigurationSection("Worlds") != null){
            allmaps.keySet().forEach(world -> {
                if (main.getConfig().getConfigurationSection("Worlds." + world.getName()) != null) {
                    Map<String, ?> map = main.getConfig().getConfigurationSection("Worlds." + world.getName()).getValues(true);
                    addToMap(world, map, allmaps.get(world));
                } else {
                    main.getConfig().createSection("Worlds." + world.getName());
                }
            });
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

    void addToCoords(Player pl, World world, String name, Location xyz){
        List<String> map = new ArrayList<>(allmaps.get(world).keySet());
        removeCCoding(map);
        if(!map.contains(removeCCString(name))) {
            allmaps.get(world).put(name, xyz);
            pl.sendMessage(String.format("Added %s to the list!", CustomColors.filterPlaceholder(name)));
        } else {
            pl.sendMessage("This name already exists!");
        }
        addtoConfig(world);
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

    private void addtoConfig(World world){
        allmaps.get(world).forEach((s, location) -> main.getConfig().set("Worlds." + world.getName() + "." + s, String.format("%d;%d;%d", (int)Math.round(location.getX()), (int)Math.round(location.getY()), (int)Math.round(location.getZ()))));
        main.saveConfig();
    }

    public HashMap<World, HashMap<String, Location>> getAllmaps() {
        return allmaps;
    }
}
