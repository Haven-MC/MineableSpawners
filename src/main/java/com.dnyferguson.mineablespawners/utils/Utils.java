package com.dnyferguson.mineablespawners.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utils {

    //allows me to be lazy, and use colors cleanly. :)
    public String Color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    //logging
    public void LogInfo(String log){
        Bukkit.getLogger().info(log);
    }
    public void LogWarn(String log){
        Bukkit.getLogger().warning(log);
    }
    public void LogSevere(String log){
        Bukkit.getLogger().severe(log);
    }
}
