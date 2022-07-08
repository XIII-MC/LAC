package com.xiii.libertycity;

import com.xiii.libertycity.command.Command;
import com.xiii.libertycity.data.Data;
import com.xiii.libertycity.listener.Event;
import com.xiii.libertycity.listener.PacketListener;
import com.xiii.libertycity.utils.ConfigUtils;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LAC extends JavaPlugin {

    public static LAC instance;
    public PacketListener listener;
    public ConfigUtils configUtils;

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_7_10)
                .compatInjector(false)
                .checkForUpdates(false);
        PacketEvents.get().load();
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        instance = this;
        listener = new PacketListener();
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eInitialing...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Instances (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eClearing DataBase (0%)...");
        Data.data.clearDataBase();
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eClearing DataBase (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Events (0%)...");
        Bukkit.getPluginManager().registerEvents(new Event(), this);
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Events (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Commands (0%)...");
        Bukkit.getPluginCommand("lac").setExecutor(new Command());
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Commands (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Configs (0%)...");
        configUtils = new ConfigUtils(this);
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading Configs (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading PacketEvents (0%)...");
        PacketEvents.get().init();
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading PacketEvents (25%)...");
        PacketEvents.get().registerListener(listener);
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading PacketEvents (50%)...");
        PacketEvents.get().getInjector().eject();
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading PacketEvents (75%)...");
        PacketEvents.get().getInjector().inject();
        for(Player p : Bukkit.getOnlinePlayers()) {
            PacketEvents.get().getInjector().injectPlayer(p);
        }
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eLoading PacketEvents (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §aPlugin activé !");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eDisabling...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eTerminating PacketEvents (0%)...");
        PacketEvents.get().terminate();
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eTerminating PacketEvents (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eTerminating BukkitTasks (0%)...");
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §eTerminating BukkitTasks (100%)...");
        Bukkit.getConsoleSender().sendMessage("§8[§2§lL§A§LAC§8] §cPlugin désactivé !");
    }
}
