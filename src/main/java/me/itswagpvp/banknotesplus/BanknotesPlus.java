package me.itswagpvp.banknotesplus;

import me.itswagpvp.banknotesplus.commands.AdminCommand;
import me.itswagpvp.banknotesplus.commands.Withdraw;
import me.itswagpvp.banknotesplus.events.RightClickClaimNote;
import me.itswagpvp.banknotesplus.misc.Metrics;
import me.itswagpvp.banknotesplus.misc.TabCompleterUtil;
import me.itswagpvp.banknotesplus.misc.updater.UpdateMessage;
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

    public static String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
    public static int ver = Integer.parseInt(split[1]);

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

        loadCommands();

        loadEvents();

        loadMetrics();

        if (ver < 12) {
            Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §cThe updater won't work under 1.12!");
        }

        if (ver >= 12) {
            new UpdateMessage().updater(95120);
        }

        Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §aPlugin enabled in " + (System.currentTimeMillis() - before) + "ms");

    }

    private void loadCommands() {

        Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §eLoading commands!");

        getCommand("banknotesplus").setExecutor(new AdminCommand());
        getCommand("banknotesplus").setTabCompleter(new TabCompleterUtil());

        getCommand("withdraw").setExecutor(new Withdraw());
        getCommand("withdraw").setTabCompleter(new TabCompleterUtil());

        getCommand("deposit").setExecutor(new Withdraw());
        getCommand("deposit").setTabCompleter(new TabCompleterUtil());
    }

    private void loadEvents() {

        Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §eLoading events!");

        Bukkit.getPluginManager().registerEvents(new RightClickClaimNote(), instance);
    }

    private void loadMetrics() {

        Bukkit.getConsoleSender().sendMessage("[BanknotesPlus] §eLoading metrics!");

        int pluginId = 12363;
        Metrics metrics = new Metrics(this, pluginId);
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
