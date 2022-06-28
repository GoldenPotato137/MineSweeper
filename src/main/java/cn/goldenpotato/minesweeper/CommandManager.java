package cn.goldenpotato.minesweeper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements org.bukkit.command.CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage("输入/minesweeper help获取帮助捏");
            return true;
        }
        switch (args[0])
        {
            case "game":
                if (!(sender instanceof Player)) //控制台不能开游戏
                {
                    sender.sendMessage("控制台可没法玩游戏捏∑(ﾟДﾟノ)ノ");
                    return true;
                }
                if(!sender.hasPermission("minesweeper.game"))
                {
                    sender.sendMessage("你没有权限玩游戏捏");
                    return true;
                }
                if (args.length != 4)
                {
                    sender.sendMessage(String.format("/minesweeper game <难度(%d-%d)> <宽度(%d-100)> <高度(%d-100)>：开始游戏",
                            PlayerManager.config.MinDiff, PlayerManager.config.MaxDiff, PlayerManager.config.MinWidth, PlayerManager.config.MinHeight));
                    return true;
                }
                int difficulty, width, height;
                try
                {
                    difficulty = Integer.parseInt(args[1]);
                    width = Integer.parseInt(args[2]);
                    height = Integer.parseInt(args[3]);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage("参数错误");
                    return true;
                }
                if (difficulty < PlayerManager.config.MinDiff || difficulty > PlayerManager.config.MaxDiff)
                {
                    sender.sendMessage("难度参数错误");
                    return true;
                }
                if (width < PlayerManager.config.MinWidth || height < PlayerManager.config.MinHeight || width > 100 || height > 100)
                {
                    sender.sendMessage("宽度或高度参数错误");
                    return true;
                }
                PlayerManager.NewGame((Player) sender, difficulty, height, width);
                return true;
            case "reload":
                if(sender instanceof Player && !sender.hasPermission("minesweeper.reload"))
                {
                    sender.sendMessage("你没有权限重载配置文件捏");
                    return true;
                }
                PlayerManager.LoadConfig();
                sender.sendMessage("加载成功(￣∀￣)");
                return true;
            case "help":
                sender.sendMessage(String.format("/minesweeper game <难度(%d-%d)> <宽度(%d-100)> <高度(%d-100)>：开始游戏",
                        PlayerManager.config.MinDiff, PlayerManager.config.MaxDiff, PlayerManager.config.MinWidth, PlayerManager.config.MinHeight));
                sender.sendMessage("/minesweeper help：显示帮助");
                sender.sendMessage("/minesweeper reload:重载配置文件");
                return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        ArrayList<String> result = new ArrayList<>();
        if (args.length == 1)
        {
            if (sender instanceof Player)//控制台不补全game
                result.add("game");
            result.add("help");
            result.add("reload");
        }
        else
        {
            switch (args[0])
            {
                case "game":
                    if (args.length == 2) //difficulty
                        result.add("2");
                    else if (args.length == 3) //width
                        result.add("9");
                    else if (args.length == 4) //length
                        result.add("6");
                    break;
                case "help":
                    result.add("game");
                    result.add("set");
                    break;
            }
        }
        return result;
    }
}
