package com.levelsystem.placeholder;


import com.levelsystem.LevelSystem;
import com.levelsystem.data.PlayerData;
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
        return "GuardTeam";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "0";
        }

        // 使用 getPlayerDataRaw 获取原始数据
        PlayerData data = plugin.getDataManager().getPlayerDataRaw(player.getUniqueId());
        if (data == null) {
            // 如果内存中没有，尝试加载
            data = plugin.getDataManager().getPlayerData(player);
        }

        // 关键：使用 plugin.getTitleManager() 获取称号
        String title = plugin.getTitleManager().getTitle(data.getTier(), data.getLevel());

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
                int expPerLevel = plugin.getExpPerLevel();
                if (expPerLevel == 0) return "0";
                int percent = (data.getValue() * 100) / expPerLevel;
                return String.valueOf(Math.min(percent, 100));

            case "progress":
            case "进度条":
                return getProgressBar(data.getValue(), plugin.getExpPerLevel());

            case "title":
            case "称号":
                return title.replace("&", "").replace("§", "");

            case "titlecolored":
            case "称号颜色":
            case "coloredtitle":
                return title.replace('&', '§');

            case "titleraw":
                return title;

            case "nexttitle":
            case "下一称号":
                return plugin.getTitleManager().getNextTitleInfo(data.getTotalLevel());

            default:
                return null;
        }
    }

    private String getProgressBar(int current, int max) {
        if (max <= 0) return "§7□□□□□□□□□□";
        int totalBars = 10;
        int filledBars = Math.min((current * totalBars) / max, totalBars);
        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                bar.append("§a■");
            } else {
                bar.append("§7■");
            }
        }

        return bar.toString();
    }
}