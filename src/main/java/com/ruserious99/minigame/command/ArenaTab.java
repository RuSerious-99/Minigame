package com.ruserious99.minigame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArenaTab implements TabCompleter {

    List<String> menu = new ArrayList<>();
    List<String> arguments = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        Player player = (Player) sender;

        List<String> resultNames = new ArrayList<>();

        if (menu.isEmpty()) {
            if (player.isOp()) {
                menu.add("removeNpc");
                menu.add("createNpc");
                menu.add("reload");
            }
            menu.add("list");
            menu.add("join");
            menu.add("leave");
            menu.add("help");

        }

        if (arguments.isEmpty()) {
            arguments.add("DeadSpace");
            arguments.add("Dungeon");
            arguments.add("Stronghold");
            arguments.add("Block");
            arguments.add("Wak_A_Block");
            arguments.add("Pvp");
            arguments.add("0");
            arguments.add("1");
            arguments.add("2");
            arguments.add("3");

        }
        if(args.length == 1) {
        return menu;
    }

        if(args.length == 2) {
            if(args[0].equals("join")){
                resultNames.add(arguments.get(4));
                resultNames.add(arguments.get(5));
                resultNames.add(arguments.get(6));
                resultNames.add(arguments.get(7));
            } else if (args[0].equals("createNpc") || args[0].equals("removeNpc")) {
                resultNames.add(arguments.get(0));
                resultNames.add(arguments.get(1));
                resultNames.add(arguments.get(2));
                resultNames.add(arguments.get(3));
                resultNames.add(arguments.get(4));
                resultNames.add(arguments.get(5));
            }

        arguments.clear();
        return resultNames;
    }
        menu.clear();
        return null;
}

}

