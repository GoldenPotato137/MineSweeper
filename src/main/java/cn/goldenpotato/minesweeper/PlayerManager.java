package cn.goldenpotato.minesweeper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Objects;

class PlayerManagerConfig
{
    public String winCommand;
    public String lossCommand;
    public boolean playWinSound;
    public boolean playLossSound;
    public boolean playClickSound;
    public boolean playMarkSound;
    public int MinDiff,MaxDiff;
    public int MinHeight,MinWidth;
}

public final class PlayerManager
{
    static HashMap<String, MineSweeperGame> map; //玩家->游戏的映射
    static PlayerManagerConfig config;
    static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public static void Init()
    {
        map = new HashMap<>();
        LoadConfig();
    }

    public static void LoadConfig()
    {
        config = ConfigManager.LoadPlayerManagerConfig();
    }

    public static void NewGame(Player player, int difficulty, int height, int width)
    {
        if(difficulty>config.MaxDiff || difficulty< config.MinDiff) return;
        if(height<config.MinHeight || width<config.MinWidth) return;
        Inventory inv = Bukkit.createInventory(player.getPlayer(), 6 * 9, "MineSweeper");
        Objects.requireNonNull(player.getPlayer()).openInventory(inv);
        map.put(player.getName(), new MineSweeperGame(player.getName(), difficulty, height, width));
        map.get(player.getName()).DrawBoard(inv);
    }

    //打开某个格子
    public static void Open(Player player, Inventory inventory, int x, int y)
    {
        var game = map.get(player.getName());
        var result = game.Click(x, y);
        if (result == MineSweeperGame.GameStatus.STOP)
            return;
        if (result == MineSweeperGame.GameStatus.FAILED)
        {
            if (config.playLossSound)
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
            if (config.lossCommand != null)
                Bukkit.getServer().dispatchCommand(console, config.lossCommand.replace("[player]", player.getName()));
            player.sendMessage("你输啦，再来一次吧！");
        }
        else if (result == MineSweeperGame.GameStatus.WIN)
        {
            if (config.playWinSound)
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            if (config.winCommand != null)
                Bukkit.getServer().dispatchCommand(console, config.winCommand.replace("[player]", player.getName()));
            player.sendMessage("恭喜胜利！");
        }
        else if (config.playClickSound)
            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5f, 1f);
        game.DrawBoard(inventory);
    }

    //标记某个格子
    public static void Mark(Player player, Inventory inventory, int x, int y)
    {
        var game = map.get(player.getName());
        var result = game.Mark(x, y);
        if (result != MineSweeperGame.GameStatus.IN_GAME)
            return;
        if (config.playMarkSound)
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 0.5f, 1f);
        game.DrawBoard(inventory);
    }

    //向某个方向移动区域
    public static void MoveBoard(Player player, Inventory inventory, MineSweeperGame.Directions direction)
    {
        var game = map.get(player.getName());
        game.MoveBoard(direction);
        game.DrawBoard(inventory);
    }
}
