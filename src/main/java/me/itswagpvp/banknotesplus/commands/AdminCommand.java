package me.itswagpvp.banknotesplus.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.itswagpvp.banknotesplus.BanknotesPlus;
import me.itswagpvp.banknotesplus.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminCommand implements CommandExecutor {

    public static BanknotesPlus plugin = BanknotesPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banknotesplus")) {
            if (args.length == 0) {
                sender.sendMessage("§d§lBanknotesPlus §7v" + BanknotesPlus.getInstance().getDescription().getVersion() + " made by §d_ItsWagPvP");
                return true;
            }

            if (args[0].equalsIgnoreCase("check")) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(plugin.getMessage("NoConsole"));
                    return true;
                }

                if (!sender.hasPermission("banknotesplus.check")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    return true;
                }

                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();

                if (item == null || item.getType() == Material.AIR) {
                    sender.sendMessage(plugin.getMessage("Air"));
                    return true;
                }

                NBTItem nbti = new NBTItem(item);
                sender.sendMessage("§d§lBanknotesPlus §8- §7Check");
                sender.sendMessage("§7");
                sender.sendMessage("§dIs Banknote? §7" + nbti.hasKey("banknote"));
                if (nbti.hasKey("banknote")) sender.sendMessage("§dAmount: §7" + nbti.getLong("banknote"));

                return true;

            } if (args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission("banknotesplus.reload")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    return true;
                }
                
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                
                sender.sendMessage(plugin.getMessage("Reload"));
                    
                return true;

            } else if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
                if (!sender.hasPermission("banknotesplus.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                    return true;
                }

                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getMessage("InvalidNumber"));
                    return true;
                }

                if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                    sender.sendMessage(plugin.getMessage("InvalidNumber"));
                    return true;
                }

                if (target.getInventory().firstEmpty() == -1) {
                    sender.sendMessage(plugin.getMessage("NoSpaceOthers"));
                    return true;
                }

                // Banknote
                ItemStack banknote = Utils.createBanknote(sender.getName(), amount);
                target.getInventory().addItem(banknote);

                String senderName = sender instanceof ConsoleCommandSender ? plugin.getMessage("Console-Name") : sender.getName();

                target.sendMessage(plugin.getMessage("Received")
                        .replaceAll("%money%", "" + amount)
                        .replaceAll("%player%", "" + senderName));

                sender.sendMessage(plugin.getMessage("Given")
                        .replaceAll("%money%", "" + amount)
                        .replaceAll("%player%", "" + target.getName()));

            } else {
                sender.sendMessage(plugin.getMessage("AdminInvalidArgument"));
                return true;
            }
        }
        return true;
    }
}
