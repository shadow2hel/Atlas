package com.shadow2hel.atlas;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

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
                    } else if(arg[0].toLowerCase().equals("show")) {
                        switch(arg[1].toLowerCase()){
                            case "all":
                                pl.sendMessage(ChatColor.GREEN + "Overworld" + ChatColor.RESET + ":");
                                main.coords.getEarthmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                                "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));

                                pl.sendMessage(ChatColor.RED + "Nether" + ChatColor.RESET + ":");
                                main.coords.getHellmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                        "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));

                                pl.sendMessage("The End:");
                                main.coords.getEndmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                                "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));

                                break;
                            case "world":
                                pl.sendMessage(ChatColor.GREEN + "Overworld" + ChatColor.RESET + ":");
                                main.coords.getEarthmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                                "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));
                                break;
                            case "nether":
                                pl.sendMessage(ChatColor.RED + "Nether" + ChatColor.RESET + ":");
                                main.coords.getHellmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                                "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));
                                break;
                            case "end":
                                pl.sendMessage("The End:");
                                main.coords.getEndmap().forEach((sl, location) -> pl.sendMessage(String.format("   %s: " +
                                                "%s%d %s%d %s%d", CustomColors.filterPlaceholder(sl), ChatColor.RED, (int)Math.round(location.getX()),
                                        ChatColor.GREEN, (int)Math.round(location.getY()), ChatColor.BLUE,
                                        (int)Math.round(location.getZ()))));
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
        if (command.getLabel().equals("atlas")) {
            List<String> all = new ArrayList<>(Arrays.asList("world", "nether", "end", "locationname", "show"));
            List<String> worlds = new ArrayList<>(Arrays.asList("world", "nether", "end"));
            List<String> worldLists = new ArrayList<>(Arrays.asList("all", "world", "nether", "end"));
            String coords = String.format("%d %d %d", Math.round(((Player) commandSender).getLocation().getX()), Math.round(((Player) commandSender).getLocation().getY()), Math.round(((Player) commandSender).getLocation().getZ()));
            List<String> completions = new ArrayList<>();
            if (arg.length == 1) {
                if (arg[0].equals("")) {

                    completions.addAll(all);
                } else {
                    for (String sl : all) {
                        if (sl.startsWith(arg[0])) {
                            completions.add(sl);
                        }
                    }

                }
            } else if (arg.length == 2) {
                if (worlds.contains(arg[0].toLowerCase())) {
                    completions.add("locationname");
                } else if(!all.contains(arg[0].toLowerCase()) || arg[0].toLowerCase().equals("locationname")){
                    completions.add(coords);
                } else if(arg[0].toLowerCase().equals("show")){
                    if(!arg[1].equals("")){
                        for (String list : worldLists) {
                            if(list.startsWith(arg[1].toLowerCase())){
                                completions.add(list);
                            }
                        }
                    } else {
                        completions.addAll(worldLists);
                    }
                }
            } else if (arg.length == 3) {
                if (!arg[1].equals("") && !arg[0].equals("show")) {
                    completions.add(coords);
                }
            } else {
                completions.clear();
            }
            Collections.sort(completions);
            return completions;
        } else {
            return null;
        }
    }
}
