package dev.mrvte.schnick;

import dev.mrvte.schnick.commands.TimerCommand;
import dev.mrvte.schnick.listener.JoinListener;
import dev.mrvte.schnick.listener.MeetVillagerListener;
import dev.mrvte.schnick.listener.QuitListener;
import dev.mrvte.schnick.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Schnick extends JavaPlugin {
    public static Schnick instance;
    private Timer timer;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new MeetVillagerListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        timer = new Timer(false, 0);
        getCommand("timer").setExecutor(new TimerCommand());

    }

    @Override
    public void onDisable() {
  
    }

    public Timer getTimer() {
        return timer;
    }

    public static Schnick getInstance() {
        return instance;
    }
}
