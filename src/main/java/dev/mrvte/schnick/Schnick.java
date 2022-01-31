package dev.mrvte.schnick;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Schnick extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new MeetVillagerListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
