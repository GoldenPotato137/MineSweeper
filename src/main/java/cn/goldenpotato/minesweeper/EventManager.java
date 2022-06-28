package cn.goldenpotato.minesweeper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class EventManager implements Listener
{
    @EventHandler
    public void OnClickOnBoard(InventoryClickEvent e)
    {
        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();
        if(inv.getTitle().equals("MineSweeper")) //保护GUI
            e.setCancelled(true);
        else
            return;

        if(e.getRawSlot() >= 6*9) //在点背包的格子
            return;

        int x = e.getSlot()/9+1, y = e.getSlot()%9+1;
//        MineSweeper.instance.getLogger().info(x + " " + y + " slot:"+e.getSlot());
        if(e.getClick().isShiftClick())
        {
//            MineSweeper.Log("Mark");
            if(x==1)
                PlayerManager.MoveBoard(player,e.getClickedInventory(),MineSweeperGame.Directions.Up);
            else if(x==6)
                PlayerManager.MoveBoard(player,e.getClickedInventory(),MineSweeperGame.Directions.Down);
            if(y==1)
                PlayerManager.MoveBoard(player,e.getClickedInventory(),MineSweeperGame.Directions.Left);
            else if(y==9)
                PlayerManager.MoveBoard(player,e.getClickedInventory(),MineSweeperGame.Directions.Right);
        }
        else if(e.isRightClick())
            PlayerManager.Mark(player,e.getClickedInventory(),x,y);
        else
            PlayerManager.Open(player,e.getClickedInventory(),x,y);
    }
}
