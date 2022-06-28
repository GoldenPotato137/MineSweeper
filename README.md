# MineSweeper
A minecraft bukkit plugin to play minesweeper!

支持bukkit/spigot/paper **1.13+** (仅在1.17、1.18测试)

**前置:** 没有捏


## 介绍
在MC中玩扫雷吧！

![](https://i.bmp.ovh/imgs/2022/06/28/03a1c63e330ce1c7.png)

**特性：**
* 自由大小棋盘，最大可支持至100*100！
* 自定义奖励/惩罚命令！
* 扫雷时有带感的声音！
* 无需前置插件！
* ~~这是我的插件开发hello world程序~~


## 玩法
输入
```/minesweeper game 难度[2-5] 宽度[9-100] 高度[6-100]```开启一局新的游戏！
(或是简写为```/ms game 难度 宽度 高度```)

左键打开一个格子。

右键标记某一个格子为地雷。

对着一个打开的格子再次左键，如果这个格子九宫格内已经标记的雷数和格子显示的雷数相同的话就
会快速打开周围九个（未标记为雷的）格子。（相当于微软扫雷的双击）

按住SHIFT键点击四周（边上）的格子来移动视野！棋盘外的格子将会被显示为黑色羊毛。
![](https://i.bmp.ovh/imgs/2022/06/28/fcc2b13d3b14d664.png)

当你试图打开有地雷的格子（即踩到地雷）的时候你就输啦！

如果你能打开所有非雷的格子，你将取得本局游戏胜利！


## 配置
```config.yml```
```yaml
Sound:
  #是否在胜利时播放声音
  PlayWinSound: true
  #是否在失败时播放声音
  PlayLossSound: true
  #是否在点击格子（开雷场）时播放声音
  PlayClickSound: true
  #是否在标记雷时播放声音
  PlayMarkSound: true

#如果不需要命令，则在对应命令的地方输入[null]
#命令中所有的[player]子段将会被替换为玩家的名字，例如give [player] diamond 1 就是给予对应的玩家一颗钻石
Command:
#  WinCommand: give [player] diamond 1
  WinCommand: [null]
#  LossCommand: kill [player]
  LossCommand: [null]

Game:
  #允许的最低难度，不能超过5
  MinDifficulty: 1
  #允许的最高难度，不能超过5，不能低于最低难度
  MaxDifficulty: 5
  #允许的雷场最小宽度，不能小于5
  MinWidth: 9
  #允许的雷场最小高度，不能小于5
  MinHeight: 6
```


## 命令与权限
* ```/minesweeper game [难度] [宽度] [高度]``` —— 开始一局新游戏！ —— minesweeper.game
* ```/minesweeper reload``` —— 重新加载配置 —— minesweeper.reload
* ```/minesweeper help``` —— 帮助 —— 无权限要求


## 下载与编译
**下载链接**：https://github.com/GoldenPotato137/MineSweeper/releases

本项目使用**Maven**管理捏

java版本：1.8

编码：UTF-8

欢迎大家为本项目提交PR！