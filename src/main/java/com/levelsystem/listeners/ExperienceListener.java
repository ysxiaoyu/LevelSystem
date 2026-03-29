package com.levelsystem.listeners;

import com.levelsystem.LevelSystem;
import com.levelsystem.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ExperienceListener implements Listener {

    private final LevelSystem plugin;

    public ExperienceListener(LevelSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        int amount = event.getAmount();

        if (amount <= 0) return; // 只处理获得经验，不处理消耗

        PlayerData data = plugin.getDataManager().getPlayerData(player);
        int oldLevel = data.getLevel();
        int oldTier = data.getTier();

        // 增加值 (获得多少加多少，消耗不减)
        data.addValue(amount, plugin.getExpPerLevel());

        // 检查是否升级
        boolean levelChanged = data.getLevel() != oldLevel;
        boolean tierChanged = data.getTier() != oldTier;

        if (levelChanged || tierChanged) {
            // 发送升级消息
            if (tierChanged) {
                // 升段了
                String msg = plugin.getConfig().getString("messages.tier-up",
                                "&6[&b等级系统&6] &b&l大突破！你升到了 &e%tier%段 &b(每段10级)")
                        .replace("%tier%", String.valueOf(data.getTier()))
                        .replace("%level%", String.valueOf(data.getLevel()));
                player.sendMessage(msg.replace('&', '§'));
            } else {
                // 只是升级
                String msg = plugin.getConfig().getString("messages.level-up",
                                "&6[&b等级系统&6] &a恭喜！你升级到了 &e%level%级 %tier%段")
                        .replace("%tier%", String.valueOf(data.getTier()))
                        .replace("%level%", String.valueOf(data.getLevel()));
                player.sendMessage(msg.replace('&', '§'));
            }
        }

        if (plugin.getConfig().getBoolean("debug")) {
            plugin.getLogger().info(player.getName() + " 获得 " + amount + " 经验，当前: " + data);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 玩家退出时保存并清理内存
        plugin.getDataManager().removePlayerData(event.getPlayer().getUniqueId());
    }
}