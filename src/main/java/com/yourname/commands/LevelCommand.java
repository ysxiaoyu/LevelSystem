package com.yourname.commands;

import com.yourname.LevelSystem;
import com.yourname.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    private final LevelSystem plugin;

    public LevelCommand(LevelSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // 显示自己的信息
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c控制台请使用: /level info <玩家>");
                return true;
            }
            showInfo((Player) sender, (Player) sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info":
                if (args.length > 1) {
                    if (!sender.hasPermission("levelsystem.admin")) {
                        sender.sendMessage("§c你没有权限查看他人信息");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§c玩家不在线");
                        return true;
                    }
                    showInfo(sender, target);
                } else {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§c请指定玩家");
                        return true;
                    }
                    showInfo(sender, (Player) sender);
                }
                break;

            case "set":
                if (!sender.hasPermission("levelsystem.admin")) {
                    sender.sendMessage("§c没有权限");
                    return true;
                }
                if (args.length < 5) {
                    sender.sendMessage("§c用法: /level set <玩家> <级> <段> <值>");
                    return true;
                }
                setData(sender, args);
                break;

            case "reload":
                if (!sender.hasPermission("levelsystem.admin")) {
                    sender.sendMessage("§c没有权限");
                    return true;
                }
                plugin.reloadConfig();
                plugin.getDataManager().saveAllData();
                sender.sendMessage("§a配置已重载");
                break;

            case "help":
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void showInfo(CommandSender sender, Player target) {
        PlayerData data = plugin.getDataManager().getPlayerData(target);
        sender.sendMessage("§6========== §b等级信息 §6==========");
        sender.sendMessage("§e玩家: §f" + target.getName());
        sender.sendMessage("§e当前段数: §f" + data.getTier() + "段");
        sender.sendMessage("§e当前等级: §f" + data.getLevel() + "级");
        sender.sendMessage("§e当前值数: §f" + data.getValue() + "/" + plugin.getExpPerLevel());
        sender.sendMessage("§e总等级: §f" + data.getTotalLevel());
        sender.sendMessage("§e进度: §f" + (data.getValue() * 100 / plugin.getExpPerLevel()) + "%");
        sender.sendMessage("§6==============================");
    }

    private void setData(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c玩家不在线");
            return;
        }

        try {
            int level = Integer.parseInt(args[2]);
            int tier = Integer.parseInt(args[3]);
            int value = Integer.parseInt(args[4]);

            if (level < 0 || level > 9 || tier < 0 || value < 0 || value >= plugin.getExpPerLevel()) {
                sender.sendMessage("§c数值错误: 级(0-9), 段(≥0), 值(0-" + (plugin.getExpPerLevel()-1) + ")");
                return;
            }

            PlayerData data = new PlayerData(level, tier, value);
            plugin.getDataManager().setPlayerData(target.getUniqueId(), data);

            sender.sendMessage("§a已设置 " + target.getName() + " 的数据:");
            sender.sendMessage("§e级: " + level + ", 段: " + tier + ", 值: " + value);

        } catch (NumberFormatException e) {
            sender.sendMessage("§c请输入有效的数字");
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6========== §b等级系统帮助 §6==========");
        sender.sendMessage("§e/level §f- 查看自己的等级信息");
        sender.sendMessage("§e/level info [玩家] §f- 查看等级信息");
        if (sender.hasPermission("levelsystem.admin")) {
            sender.sendMessage("§e/level set <玩家> <级> <段> <值> §f- 设置玩家数据");
            sender.sendMessage("§e/level reload §f- 重载配置");
        }
        sender.sendMessage("§6===================================");
    }
}