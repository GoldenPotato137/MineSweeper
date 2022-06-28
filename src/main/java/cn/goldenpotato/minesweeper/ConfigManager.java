package cn.goldenpotato.minesweeper;

import org.bukkit.configuration.file.FileConfiguration;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ConfigManager
{
    static boolean init = false;
    static FileConfiguration configuration;
    static void Init()
    {
        MineSweeper.instance.saveDefaultConfig();
        init=true;
    }

    static public PlayerManagerConfig LoadPlayerManagerConfig()
    {
        if(!init) Init();
        MineSweeper.instance.reloadConfig();
        configuration = MineSweeper.instance.getConfig();
        PlayerManagerConfig result = new PlayerManagerConfig();
        //Command
        result.lossCommand = configuration.getString("Command.LossCommand");
        if(result.lossCommand!=null && result.lossCommand.equals("[null]"))
            result.lossCommand = null;
        result.winCommand = configuration.getString("Command.WinCommand");
        if(result.winCommand!=null && result.winCommand.equals("[null]"))
            result.winCommand = null;
        //Sound
        result.playWinSound = configuration.getBoolean("Sound.PlayWinSound",false);
        result.playLossSound = configuration.getBoolean("Sound.PlayLossSound",false);
        result.playClickSound = configuration.getBoolean("Sound.PlayClickSound",false);
        result.playMarkSound = configuration.getBoolean("Sound.PlayMarkSound",false);
        //Game
        result.MaxDiff = min(configuration.getInt("Game.MaxDifficulty",5),5);
        result.MinDiff = max(configuration.getInt("Game.MinDifficulty",1),1);
        result.MaxDiff = max(result.MaxDiff,result.MinDiff);
        result.MinWidth = max(configuration.getInt("Game.MinWidth",9),5);
        result.MinHeight = max(configuration.getInt("Game.MinHeight",9),5);
        return result;
    }
}
