package me.itswagpvp.banknotesplus.commands;

import me.itswagpvp.banknotesplus.BanknotesPlus;
import me.itswagpvp.banknotesplus.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Withdraw implements CommandExecutor {

    public static BanknotesPlus plugin = BanknotesPlus.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("NoConsole"));
            return true;
        }

        if (!sender.hasPermission("banknotesplus.withdraw")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            return true;
        }

        Player player = (Player) sender;
        long amount;


        if (args.length <= 0) {
            sender.sendMessage(plugin.getMessage("InvalidNumber"));
            return true;
        }

        try {
            amount = args[0].equalsIgnoreCase("all") ? (long) BanknotesPlus.getEconomy().getBalance(player) : Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessage("InvalidNumber"));
            return true;
        }

        if (args[0].contains("-")) {
            sender.sendMessage(plugin.getMessage("InvalidNumber"));
            return true;
        }

        if (Double.isInfinite(amount) || amount <= 0) {
            sender.sendMessage(plugin.getMessage("InvalidNumber"));
            return true;
        }

        if (BanknotesPlus.getEconomy().getBalance(player) < 0) {
            sender.sendMessage(plugin.getMessage("NoMoney"));
            return true;
        }

        if (amount > BanknotesPlus.getEconomy().getBalance(player)) {
            sender.sendMessage(plugin.getMessage("NoMoney"));
            return true;
        }

        if (player.getInventory().firstEmpty() == -1) {
            sender.sendMessage(plugin.getMessage("NoSpace"));
            return true;
        }

        ItemStack banknote = Utils.createBanknote(player.getName(), amount);
        BanknotesPlus.getEconomy().withdrawPlayer(player, amount);

        player.getInventory().addItem(banknote);

        player.sendMessage(plugin.getMessage("Created")
                .replaceAll("%money%", "" + amount));

        return true;
    }
}
