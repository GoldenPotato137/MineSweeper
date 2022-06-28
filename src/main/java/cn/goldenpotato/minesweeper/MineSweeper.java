package cn.goldenpotato.minesweeper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MineSweeper extends JavaPlugin
{
    public static JavaPlugin instance;

    public static void Log(String s)
    {
        instance.getLogger().info(s);
    }

    @Override
    public void onEnable()
    {
        instance = this;
        // Plugin startup logic
        //注册命令
//        getLogger().info("hello world!");
        Objects.requireNonNull(Bukkit.getPluginCommand("minesweeper")).setExecutor(new CommandManager());

        //注册事件监听器
        Bukkit.getPluginManager().registerEvents(new EventManager(),this);

        //初始化组件
        ConfigManager.Init();
        PlayerManager.Init();
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
