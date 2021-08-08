package me.itswagpvp.banknotesplus.misc;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterUtil implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> listDefault = Collections.singletonList("");
        List<String> playerNames = Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        //MAIN COMMAND
        if (command.getName().equalsIgnoreCase("banknotesplus")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return Arrays.asList("give", "check", "reload");
                }
                case 2: {
                    if (args[0].equalsIgnoreCase("give")) {
                        return playerNames;
                    } else {
                        return listDefault;
                    }
                }
                case 3: {
                    if (args[0].equalsIgnoreCase("give")) {
                        return Arrays.asList("10", "100", "1000");
                    } else {
                        return listDefault;
                    }
                }
                default:
                    return listDefault;
            }
        }

        return listDefault;
    }
}
