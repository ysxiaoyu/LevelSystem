package com.levelsystem.manager;


import com.levelsystem.LevelSystem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TitleManager {

    private final LevelSystem plugin;
    private final TreeMap<Integer, String> titles;

    public TitleManager(LevelSystem plugin) {
        this.plugin = plugin;
        this.titles = new TreeMap<>();
        loadTitles();
    }

    public void loadTitles() {
        titles.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("titles");

        if (section == null) {
            titles.put(0, "&7[&f萌新&7]");
            titles.put(10, "&7[&a学徒&7]");
            titles.put(20, "&7[&2勇士&7]");
            titles.put(30, "&7[&b精英&7]");
            titles.put(40, "&7[&9专家&7]");
            titles.put(50, "&7[&6大师&7]");
            titles.put(60, "&7[&c宗师&7]");
            titles.put(70, "&7[&4传说&7]");
            titles.put(80, "&7[&5神话&7]");
            titles.put(90, "&7[&e★王者★&7]");
            titles.put(100, "&7[&4&k|&c&k|&6☆至尊☆&c&k|&4&k|&7]");
            return;
        }

        for (String key : section.getKeys(false)) {
            try {
                String[] parts = key.split("-");
                int minLevel = Integer.parseInt(parts[0].trim());
                String title = section.getString(key, "&7[&f未知&7]");
                titles.put(minLevel, title);
                plugin.getLogger().info("加载称号: " + minLevel + "级+ = " + title);
            } catch (Exception e) {
                plugin.getLogger().warning("称号配置格式错误: " + key);
            }
        }
    }

    public String getTitle(int totalLevel) {
        Map.Entry<Integer, String> entry = titles.floorEntry(totalLevel);
        if (entry == null) {
            return "&7[&f萌新&7]";
        }
        return entry.getValue();
    }

    public String getTitle(int tier, int level) {
        int totalLevel = tier * 10 + level;
        return getTitle(totalLevel);
    }

    public String getNextTitleInfo(int totalLevel) {
        Map.Entry<Integer, String> higher = titles.higherEntry(totalLevel);
        if (higher == null) {
            return "已达到最高段位！";
        }
        int needLevel = higher.getKey() - totalLevel;
        return "距离 &r" + higher.getValue() + " &7还需 &e" + needLevel + "&7 级";
    }

    public void reload() {
        loadTitles();
    }

    public Map<Integer, String> getAllTitles() {
        return new HashMap<>(titles);
    }
}