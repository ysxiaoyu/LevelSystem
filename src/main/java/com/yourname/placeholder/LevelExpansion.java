package com.yourname.placeholder;

import com.yourname.LevelSystem;
import com.yourname.data.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LevelExpansion extends PlaceholderExpansion {

    private final LevelSystem plugin;

    public LevelExpansion(LevelSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "levelsystem";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GuardTeam-ysxiaoyu";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // 持久化，不随重载消失
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        PlayerData data = plugin.getDataManager().getPlayerData(player);

        switch (params.toLowerCase()) {
            case "level":
            case "级":
                return String.valueOf(data.getLevel());

            case "tier":
            case "段":
                return String.valueOf(data.getTier());

            case "value":
            case "值":
                return String.valueOf(data.getValue());

            case "total":
            case "总等级":
                return String.valueOf(data.getTotalLevel());

            case "percent":
            case "百分比":
                int percent = (data.getValue() * 100) / plugin.getExpPerLevel();
                return String.valueOf(percent);

            case "progress":
            case "进度条":
                return getProgressBar(data.getValue(), plugin.getExpPerLevel());

            default:
                return null;
        }
    }

    private String getProgressBar(int current, int max) {
        int totalBars = 10;
        int filledBars = (current * totalBars) / max;
        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                bar.append("§a■"); // 绿色填充
            } else {
                bar.append("§7■"); // 灰色空槽
            }
        }

        return bar.toString();
    }
}