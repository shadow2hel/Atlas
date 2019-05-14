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
        if (commandSender instanceof Player) {
            Player pl = (Player) commandSender;
            List<World> worlds = new ArrayList<>(main.getServer().getWorlds());
            List<String> strWorlds = new ArrayList<>();
            for (World world : worlds) {
                strWorlds.add(world.getName());
            }
            if (arg.length != 0) {
                if (arg.length == 1) { // When just the location name is given and nothing else.
                    main.coords.addToCoords(pl, pl.getWorld(), arg[0], new Location(pl.getWorld(), pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                } else if (arg.length == 2) { // When a location name and world name is given, or the show command is used.
                    if (strWorlds.contains(arg[0].toLowerCase())) {
                        for (World world : worlds) {
                            if (world.getName().toLowerCase().equals(arg[0].toLowerCase())) {
                                main.coords.addToCoords(pl, world, arg[1],
                                        new Location(world,
                                                pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ()));
                            }
                        }
                    } else if (arg[0].toLowerCase().equals("show")) {
                        if (arg[1].toLowerCase().equals("all")) {
                            for (World world : main.getServer().getWorlds()) {
                                selectWorld(pl, world);
                            }
                        } else if (strWorlds.contains(arg[1])) {
                            for (World world : worlds) {
                                if (world.getName().toLowerCase().equals(arg[1])) {
                                    selectWorld(pl, world);
                                }
                            }
                        }
                    }
                } else if (arg.length <= 4) {
                    List<String> thingsToCheck = new ArrayList<>(strWorlds);
                    thingsToCheck.add("show");

                    if(!thingsToCheck.contains(arg[0].toLowerCase()) && arg.length == 4){
                        Location newloc = null;
                        try {
                            newloc = new Location(pl.getWorld(), Double.parseDouble(arg[1]),
                                    Double.parseDouble(arg[2]), Double.parseDouble(arg[3]));
                        } catch (IllegalArgumentException ignored) {
                            pl.sendMessage("Coordinates are incorrect!");
                        }
                        if (newloc != null) {
                            main.coords.addToCoords(pl, pl.getWorld(), arg[0], newloc);
                        }
                    } else {
                        pl.sendMessage("Coordinates are incorrect!");
                    }
                } else if (arg.length <= 5) {
                    if (strWorlds.contains(arg[0].toLowerCase())) {
                        for (World world : worlds) {
                            if (world.getName().toLowerCase().equals(arg[0].toLowerCase())) {
                                Location newloc = null;
                                try {
                                    newloc = new Location(world, Double.parseDouble(arg[2]),
                                            Double.parseDouble(arg[3]), Double.parseDouble(arg[4]));
                                } catch (IllegalArgumentException e) {
                                    pl.sendMessage("Coordinates are incorrect!");
                                }
                                if (newloc != null) {
                                    main.coords.addToCoords(pl, world, arg[1], newloc);
                                }
                            }
                        }

                    }
                } else {
                    pl.sendMessage("Too many parameters");
                    return false;
                }
            }

        }
        return true;
    }

    private void selectWorld(Player pl, World world) {
        pl.sendMessage(world.getName().substring(0, 1).toUpperCase() + world.getName().substring(1) + ": ");
        main.coords.getAllmaps().get(world).forEach((s1, location) -> pl.sendMessage(String.format("   %s: " +
                        "%s%d %s%d %s%d", CustomColors.filterPlaceholder(s1), ChatColor.RED, (int) Math.round(location.getX()),
                ChatColor.GREEN, (int) Math.round(location.getY()), ChatColor.BLUE,
                (int) Math.round(location.getZ()))));
        pl.sendMessage("");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] arg) {
        if (command.getLabel().equals("atlas")) {
            List<String> worlds = new ArrayList<>();
            main.getServer().getWorlds().forEach(world -> worlds.add(world.getName()));
            List<String> worldLists = new ArrayList<>(Arrays.asList("all"));
            worldLists.addAll(worlds);
            worldLists.add("all");
            List<String> all = new ArrayList<>(worlds);
            all.addAll(Arrays.asList("locationname", "show"));
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
                } else if (!all.contains(arg[0].toLowerCase()) || arg[0].toLowerCase().equals("locationname")) {
                    completions.add(coords);
                } else if (arg[0].toLowerCase().equals("show")) {
                    if (!arg[1].equals("")) {
                        for (String list : worldLists) {
                            if (list.startsWith(arg[1].toLowerCase())) {
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
