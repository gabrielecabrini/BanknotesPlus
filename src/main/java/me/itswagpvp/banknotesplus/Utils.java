package me.itswagpvp.banknotesplus;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static BanknotesPlus plugin;

    // Create a banknotes with all custom entries
    public static ItemStack createBanknote(String creatorName, long amount) {

        if (creatorName.equals("CONSOLE")) {
            creatorName = plugin.getConfig().getString("Console-Name");
        }

        ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Material", "PAPER"), false), 1);

        ItemMeta meta = item.getItemMeta();

        List<String> formatLore = new ArrayList<>();
        for (String lore : BanknotesPlus.getInstance().getConfig().getStringList("Lore")) {
            formatLore.add(lore
                    .replace("%money%", "$" + amount)
                    .replace("%player%", creatorName)
                    .replaceAll("&", "§"));
        }

        meta.setLore(formatLore);
        meta.setDisplayName(BanknotesPlus.getInstance().getConfig().getString("Name"));
        item.setItemMeta(meta);

        // Writes the nbt tag
        NBTItem nbti = new NBTItem(item);
        nbti.setLong("banknote", amount);

        return nbti.getItem();
    }

    // Return if an item is a banknote
    public static boolean isBanknote(ItemStack item) {

        NBTItem nbti = new NBTItem(item);
        return nbti.hasKey("banknote");

    }

    // Get the value of a banknote
    public static long getBanknoteAmount(ItemStack item) {

        NBTItem nbti = new NBTItem(item);
        return nbti.getLong("banknote");

    }

}
