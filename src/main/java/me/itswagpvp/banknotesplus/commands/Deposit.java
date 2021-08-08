package me.itswagpvp.banknotesplus.commands;

import me.itswagpvp.banknotesplus.BanknotesPlus;
import me.itswagpvp.banknotesplus.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Deposit implements CommandExecutor {

    private static BanknotesPlus plugin = BanknotesPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("NoConsole"));
            return true;
        }

        if (!sender.hasPermission("banknotesplus.deposit")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        // Verify that this is a real banknote
        if (!Utils.isBanknote(item)) {
            return true;
        }

        long amount = Utils.getBanknoteAmount(item);

        // Negative banknotes are not allowed
        if (Double.compare(amount, 0) < 0) {
            return true;
        }

        BanknotesPlus.getEconomy().depositPlayer(player, (double) amount);

        // Deposit the money
        player.sendMessage(plugin.getMessage("Redeemed")
                .replace("%money%", "" + amount));

        // Remove the item
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (item.getAmount() == 1) {
                player.getInventory().removeItem(item);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
            return;
        });

        return true;
    }
}
