package me.itswagpvp.banknotesplus;

import me.itswagpvp.banknotesplus.commands.AdminCommand;
import me.itswagpvp.banknotesplus.commands.Withdraw;
import me.itswagpvp.banknotesplus.events.RightClickClaimNote;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BanknotesPlus extends JavaPlugin implements Listener {

    protected static BanknotesPlus instance;

    private static Economy econ;

    // Plugin startup logic
    @Override
    public void onEnable() {

        instance = this;

        long before = System.currentTimeMillis();

        saveDefaultConfig();

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §cCould not find Vault! Disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin(instance);
        }

        if (getServer().getPluginManager().getPlugin("NBTAPI") == null) {
            Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §cCould not find NBTAPI! Disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin(instance);
        }

        getCommand("banknotesplus").setExecutor(new AdminCommand());
        getCommand("withdraw").setExecutor(new Withdraw());

        Bukkit.getPluginManager().registerEvents(new RightClickClaimNote(), instance);

        Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §aPlugin enabled in " + (System.currentTimeMillis() - before) + "ms");

    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static BanknotesPlus getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public String getMessage(String path) {
        if (!BanknotesPlus.instance.getConfig().isString(path)) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(BanknotesPlus.instance.getConfig().getString(path)));
    }

}
