package com.shadow2hel.atlas;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandA implements TabExecutor {
    private Atlas main;

    public CommandA(Atlas main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arg) {
        if(commandSender instanceof Player){
            Player pl = (Player)commandSender;
            List<String> strWorlds = new ArrayList<>(Arrays.asList("nether", "world", "end"));
            if(arg.length != 0) {
                if(arg.length == 1) {
                    main.coords.addToCoords(pl, pl.getWorld().getEnvironment(), arg[0], new Location(pl.getWorld(), pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                } else if(arg.length==2){

                    if(strWorlds.contains(arg[0].toLowerCase())){
                        switch(arg[0]){
                            case "world":
                                main.coords.addToCoords(pl, World.Environment.NORMAL, arg[1],
                                        new Location(main.getAppropriateWorld(World.Environment.NORMAL),
                                                pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                                break;
                            case "end":
                                main.coords.addToCoords(pl, World.Environment.THE_END, arg[1],
                                        new Location(main.getAppropriateWorld(World.Environment.THE_END),
                                                pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                                break;
                            case "nether":
                                main.coords.addToCoords(pl, World.Environment.NETHER, arg[1],
                                        new Location(main.getAppropriateWorld(World.Environment.NETHER),
                                                pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                                break;
                        }
                    }
                } else if(arg.length <= 4){
                    Location newloc = null;
                    try {
                        newloc = new Location(pl.getWorld(), Double.parseDouble(arg[1]),
                                Double.parseDouble(arg[2]), Double.parseDouble(arg[3]));
                    } catch(IllegalArgumentException e){
                        pl.sendMessage("Coordinates are incorrect!");
                    }
                    if(newloc != null){
                        main.coords.addToCoords(pl, pl.getWorld().getEnvironment(), arg[0], newloc);
                    }

                } else if(arg.length <= 5){
                    if(strWorlds.contains(arg[0].toLowerCase())){
                        switch(arg[0]){
                            case "world":
                                Location newloc = null;
                                try {
                                    newloc = new Location(main.getAppropriateWorld(World.Environment.NORMAL), Double.parseDouble(arg[2]),
                                            Double.parseDouble(arg[3]), Double.parseDouble(arg[4]));
                                } catch(IllegalArgumentException e){
                                    pl.sendMessage("Coordinates are incorrect!");
                                }
                                if(newloc != null){
                                    main.coords.addToCoords(pl, World.Environment.NORMAL, arg[1], newloc);
                                }
                                break;
                            case "end":
                                newloc = null;
                                try {
                                    newloc = new Location(main.getAppropriateWorld(World.Environment.THE_END), Double.parseDouble(arg[2]),
                                            Double.parseDouble(arg[3]), Double.parseDouble(arg[4]));
                                } catch(IllegalArgumentException e){
                                    pl.sendMessage("Coordinates are incorrect!");
                                }
                                if(newloc != null){
                                    main.coords.addToCoords(pl, World.Environment.THE_END, arg[1], newloc);
                                }
                                break;
                            case "nether":
                                newloc = null;
                                try {
                                    newloc = new Location(main.getAppropriateWorld(World.Environment.NETHER), Double.parseDouble(arg[2]),
                                            Double.parseDouble(arg[3]), Double.parseDouble(arg[4]));
                                } catch(IllegalArgumentException e){
                                    pl.sendMessage("Coordinates are incorrect!");
                                }
                                if(newloc != null){
                                    main.coords.addToCoords(pl, World.Environment.NETHER, arg[1], newloc);
                                }
                                break;
                        }
                    }

                }else {
                    pl.sendMessage("Too many parameters!");
                }
            } else {
                pl.sendMessage("You didn't enter an option!");
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] arg) {
        final String[] COMMANDS = {"name", "world", "nether", "end"};
        List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        if(arg.length>1){
            completions.clear();
        } else {
            StringUtil.copyPartialMatches(arg[0], Arrays.asList(COMMANDS), completions);
        }
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
