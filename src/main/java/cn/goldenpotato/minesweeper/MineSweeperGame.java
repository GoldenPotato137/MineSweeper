package cn.goldenpotato.minesweeper;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class MineSweeperGame
{
    String playerName;
    boolean[][] board;
    boolean[][] vis;//某个格子是否有被打开过
    public int[][] show;//显示给玩家的雷场，-1表示尚未打开，-2表示标记为雷
    public int difficulty, width, height;
    int cntToOpen;//还剩多少个格子要打开，0时胜利
    int cameraX,cameraY;//当前显示界面左上角坐标
    boolean firstClick;
    public GameStatus gameStatus;

    enum GameStatus
    {
        FAILED, WIN, IN_GAME, STOP
    }

    enum Directions
    {
        Up, Down, Left, Right
    }

    public MineSweeperGame(String playerName,int difficulty,int height,int width)
    {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.height = height;
        this.width = width;
        board = new boolean[height+2][width+2];
        vis = new boolean[height+2][width+2];
        show = new int[height+2][width+2];
        NewGame();
    }

    private void NewGame()
    {
        Random rnd = new Random();
        firstClick = true;
        cameraX = cameraY = 0;
        for(int i=1;i<=height;i++)
        {
            Arrays.fill(board[i],false);
            Arrays.fill(vis[i],false);
            Arrays.fill(show[i],-1);
        }
        //随机生成雷场
        cntToOpen = width*height-(int) (width*height * difficulty *0.1);
        for(int mineCount=width*height-cntToOpen;mineCount>0;mineCount--)
        {
            int x = rnd.nextInt(height)+1, y = rnd.nextInt(width)+1;
            if(board[x][y])
            {
                mineCount++;
                continue;
            }
            board[x][y]=true;
        }
        gameStatus = GameStatus.IN_GAME;
    }

    //检查坐标是否合法，不合法返回true
    private boolean CheckN(int x, int y)
    {
        return x <= 0 || x > height || y <= 0 || y > width;
    }

    //获取某一坐标周围的雷数
    private int GetMineCnt(int x,int y)
    {
        int cntMine=0;
        for(int i=-1;i<=1;i++)
            for(int j=-1;j<=1;j++)
                if(board[x+i][y+j])
                    cntMine++;
        return cntMine;
    }

    //打开格子，返回周围雷数
    private void Open(int x,int y)
    {
        if(CheckN(x, y) || vis[x][y]) return;
        vis[x][y]=true;
        if(board[x][y])
        {
            gameStatus = GameStatus.FAILED;
            return;
        }
        cntToOpen--;
        int cntMine = GetMineCnt(x,y);
        show[x][y]=cntMine;
        if(cntMine==0)
            for(int i=-1;i<=1;i++)
                for(int j=-1;j<=1;j++)
                    if(!vis[x+i][y+j])
                        Open(x+i,y+j);
    }

    //双击打开确认区
    private void DoubleClick(int x,int y)
    {
        if(gameStatus!=GameStatus.IN_GAME) return ;
        if(CheckN(x,y)) return ;
        if(!vis[x][y]) return ; //当前还没开
        int cnt=0;
        for(int dx=-1;dx<=1;dx++)
            for(int dy=-1;dy<=1;dy++)
                if(show[x+dx][y+dy]==-2)
                    cnt++;
        if(cnt == GetMineCnt(x,y))
            for(int dx=-1;dx<=1;dx++)
                for(int dy=-1;dy<=1;dy++)
                    if(show[x+dx][y+dy]!=-2)
                        Open(x+dx,y+dy);
    }

    //标记某格为雷
    public GameStatus Mark(int x,int y)
    {
        x+=cameraX;
        y+=cameraY;

        if(gameStatus != GameStatus.IN_GAME)  return gameStatus;
        if(CheckN(x, y)) return gameStatus;
        if(!vis[x][y])
            show[x][y]=(show[x][y]==-2)?-1:-2;
        return gameStatus;
    }

    //玩家选择了某格子
    public GameStatus Click(int x,int y)
    {
        x+=cameraX;
        y+=cameraY;

        if(gameStatus!=GameStatus.IN_GAME) //游戏已经结束
            return gameStatus=GameStatus.STOP;
        if(CheckN(x,y))
            return gameStatus;
        if(show[x][y]==-2) //标记为雷了
            return gameStatus;

        //第一次开格子，强制点出一片区域（当前所选格子雷数为0）
        if(firstClick)
        {
            if(GetMineCnt(x,y)!=0)
            {
                int cnt = 0;
                for(int dx=-1;dx<=1;dx++)
                    for(int dy=-1;dy<=1;dy++)
                        if(board[x+dx][y+dy])
                        {
                            cnt++;
                            board[x+dx][y+dy]=false;
                        }
                for(;cnt>0;cnt--)
                {
                    Random rnd = new Random();
                    int tempX = rnd.nextInt(height)+1, tempY = rnd.nextInt(width)+1;
                    while(board[tempX][tempY] || (tempX>=x-1 && tempX<=x+1 && tempY>=y-1 && tempY<=y+1))
                    {
                        tempX = rnd.nextInt(height) + 1;
                        tempY = rnd.nextInt(width) + 1;
                    }
                    board[tempX][tempY]=true;
                }
            }
            firstClick = false;
        }

        if(!vis[x][y])
            Open(x,y);
        else
            DoubleClick(x,y);

        if(cntToOpen==0 && gameStatus==GameStatus.IN_GAME) //打开了全部格子，胜利
            return gameStatus=GameStatus.WIN;
        return gameStatus;
    }

    private int index(int x,int y)
    {
        return (x-1)*9+y-1;
    }

    public void DrawBoard(Inventory inventory)
    {
        if(inventory==null) return;
        for(int i=1;i<=6;i++)
            for(int j=1;j<=9;j++)
            {
                if(CheckN(i+cameraX,j+cameraY)) //坐标不合法（外部）
                {
                    inventory.setItem(index(i, j), new ItemStack(Material.BLACK_WOOL, 1));
                    continue;
                }
                else if(show[i+cameraX][j+cameraY]==-1) //尚未点击，蓝色羊毛
                    inventory.setItem(index(i,j),new ItemStack(Material.LIGHT_BLUE_WOOL,1));
                else if(show[i+cameraX][j+cameraY]==0) //没有雷
                    inventory.setItem(index(i,j),new ItemStack(Material.AIR));
                else if(show[i+cameraX][j+cameraY]==-2) //标记
                    inventory.setItem(index(i,j),new ItemStack(Material.RED_WOOL,1));
                else //雷数
                    inventory.setItem(index(i,j),new ItemStack(Material.GREEN_WOOL,show[i+cameraX][j+cameraY]));
                //被打开的雷，特殊标记为骷髅
                if(board[i+cameraX][j+cameraY] && vis[i+cameraX][j+cameraY])
                    inventory.setItem(index(i,j),new ItemStack(Material.SKELETON_SKULL));
            }
    }

    public void MoveBoard(Directions direction)
    {
        if(direction==Directions.Up)
            cameraX--;
        else if(direction==Directions.Down)
            cameraX++;
        else if(direction==Directions.Left)
            cameraY--;
        else
            cameraY++;
    }
}
