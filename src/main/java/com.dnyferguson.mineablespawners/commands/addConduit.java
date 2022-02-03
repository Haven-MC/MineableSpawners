package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;


public class addConduit implements CommandExecutor {
    Utils Utils = new Utils();

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        ItemStack spawnerConduit = new ItemStack(Material.EMERALD);
        ItemMeta meta = spawnerConduit.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.random());
        }
        assert meta != null;
        MineableSpawners plugin = new MineableSpawners();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfigurationHandler().getMessage("spawner-token", "name")));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', plugin.getConfigurationHandler().getMessage("spawner-token", "lore")));
        meta.setLore(lore);
        spawnerConduit.setItemMeta(meta);

        if(args.length < 1){
            sender.sendMessage(Utils.Color("&6Please specify a player"));
            return true;
        } else {
            Player p = (Player) Bukkit.getServer().getOfflinePlayer(args[0]);
            try{
                p.getInventory().addItem(spawnerConduit);
            } catch(Exception e){
                Utils.LogSevere("Could not give Spawner Conduit");
            }
        }
        return true;
    }
}