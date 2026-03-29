package com.levelsystem.listeners;


import com.levelsystem.LevelSystem;
import com.levelsystem.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;  // 添加
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ExperienceListener implements Listener {

    private final LevelSystem plugin;

    public ExperienceListener(LevelSystem plugin) {
        this.plugin = plugin;
    }

    // 使用最高优先级，确保最先处理经验变化
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        int amount = event.getAmount();

        if (amount <= 0) return;

        // 获取数据（必须存在）
        PlayerData data = plugin.getDataManager().getPlayerData(player);

        // 记录旧数据用于比较
        int oldTotal = data.getTotalLevel();
        String oldTitle = plugin.getTitleManager().getTitle(data.getTier(), data.getLevel());

        // 添加经验
        data.addValue(amount, plugin.getExpPerLevel());

        // 关键：立即保存数据，确保PlaceholderAPI能读取到最新值
        plugin.getDataManager().savePlayerData(player.getUniqueId());

        // 检查变化
        int newTotal = data.getTotalLevel();
        String newTitle = plugin.getTitleManager().getTitle(data.getTier(), data.getLevel());

        if (newTotal != oldTotal) {
            // 等级变化了
            if (!oldTitle.equals(newTitle)) {
                // 段位变化（升段）
                String msg = plugin.getConfig().getString("messages.tier-up",
                                "&6[&b等级系统&6] &b&l大突破！你升到了 %title% &e%tier%段 &b(每段10级)")
                        .replace("%title%", newTitle.replace('&', '§'))
                        .replace("%tier%", String.valueOf(data.getTier()))
                        .replace("%level%", String.valueOf(data.getLevel()));
                player.sendMessage(msg);

                player.sendMessage("§6§l>> 段位晋升！ §r你现在是 " + newTitle.replace('&', '§'));
            } else {
                // 只是升级
                String msg = plugin.getConfig().getString("messages.level-up",
                                "&6[&b等级系统&6] &a恭喜！你升级到了 %title% &e%level%级 %tier%段")
                        .replace("%title%", newTitle.replace('&', '§'))
                        .replace("%tier%", String.valueOf(data.getTier()))
                        .replace("%level%", String.valueOf(data.getLevel()));
                player.sendMessage(msg);
            }

            // 显示下一目标
            String nextInfo = plugin.getTitleManager().getNextTitleInfo(data.getTotalLevel());
            if (!nextInfo.contains("最高")) {
                player.sendMessage("§7" + nextInfo.replace('&', '§'));
            }
        }

        if (plugin.getConfig().getBoolean("debug")) {
            plugin.getLogger().info(player.getName() + " 获得 " + amount + " 经验，" +
                    "当前: " + data.getTier() + "段" + data.getLevel() + "级" +
                    " 值:" + data.getValue());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 强制加载数据
        PlayerData data = plugin.getDataManager().getPlayerData(player);
        String title = plugin.getTitleManager().getTitle(data.getTier(), data.getLevel()).replace('&', '§');

        player.sendMessage("§6========== §b欢迎回来 §6==========");
        player.sendMessage("§7当前段位: " + title);
        player.sendMessage("§7等级: §e" + data.getTotalLevel() + " §7(§f" + data.getTier() + "段" + data.getLevel() + "级§7)");

        String nextInfo = plugin.getTitleManager().getNextTitleInfo(data.getTotalLevel());
        if (!nextInfo.contains("最高")) {
            player.sendMessage("§7" + nextInfo.replace('&', '§'));
        }
        player.sendMessage("§6===============================");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 保存并清理
        plugin.getDataManager().removePlayerData(event.getPlayer().getUniqueId());
    }
}