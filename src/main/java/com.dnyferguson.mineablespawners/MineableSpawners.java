package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.commands.MineableSpawnersCommand;
import com.dnyferguson.mineablespawners.commands.addConduit;
import com.dnyferguson.mineablespawners.listeners.AnvilRenameListener;
import com.dnyferguson.mineablespawners.listeners.EggChangeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerExplodeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerMineListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerPlaceListener;
import com.dnyferguson.mineablespawners.listeners.WitherBreakSpawnerListener;
import com.dnyferguson.mineablespawners.metrics.Metrics;
import com.dnyferguson.mineablespawners.utils.ConfigurationHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class MineableSpawners extends JavaPlugin {
    private ConfigurationHandler configurationHandler;
    private Economy econ;
    private static API api;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        registerCommands();

        configurationHandler = new ConfigurationHandler(this);

        if (!setupEconomy()) {
            getLogger().info("vault not found, economy features disabled.");
        }

        getCommand("mineablespawners").setExecutor(new MineableSpawnersCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SpawnerMineListener(this), this);
        pm.registerEvents(new SpawnerPlaceListener(this), this);
        pm.registerEvents(new EggChangeListener(this), this);
        pm.registerEvents(new AnvilRenameListener(this), this);
        pm.registerEvents(new SpawnerExplodeListener(this), this);
        pm.registerEvents(new WitherBreakSpawnerListener(this), this);

        if (getConfigurationHandler().getBoolean("global", "show-available")) {
            StringBuilder str = new StringBuilder("Available mob types: \n");
            for (EntityType type : EntityType.values()) {
                str.append("- ");
                str.append(type.name());
                str.append("\n");
            }
            getLogger().info(str.toString());
        }

        api = new API(this);
        int pluginId = 7354;
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
        return econ != null;
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    public Economy getEcon() {
        return econ;
    }

    public static API getApi() {
        return api;
    }
    public ItemStack conduit(NamespacedKey key) {
        ItemStack spawnerConduit = new ItemStack(Material.EMERALD);
        ItemMeta meta = spawnerConduit.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.random());
        }
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfigurationHandler().getMessage("spawner-token", "name")));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', getConfigurationHandler().getMessage("spawner-token", "lore")));
        meta.setLore(lore);
        spawnerConduit.setItemMeta(meta);
        return spawnerConduit;
    }


        private void registerCommands(){
        getCommand("giveConduit").setExecutor(new addConduit());
    }
}
