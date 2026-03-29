package com.yourname;

import com.yourname.commands.LevelCommand;
import com.yourname.data.DataManager;
import com.yourname.listeners.ExperienceListener;
import com.yourname.placeholder.LevelExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelSystem extends JavaPlugin {

    private static LevelSystem instance;
    private DataManager dataManager;
    private LevelExpansion levelExpansion;

    @Override
    public void onEnable() {
        instance = this;

        // 保存默认配置
        saveDefaultConfig();

        // 初始化数据管理器
        this.dataManager = new DataManager(this);
        dataManager.loadAllData();

        // 注册监听器
        getServer().getPluginManager().registerEvents(new ExperienceListener(this), this);

        // 注册命令
        getCommand("level").setExecutor(new LevelCommand(this));

        // 注册PlaceholderAPI扩展
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.levelExpansion = new LevelExpansion(this);
            levelExpansion.register();
            getLogger().info("PlaceholderAPI 扩展已注册！");
            getLogger().info("可用变量: %levelsystem_level%, %levelsystem_tier%, %levelsystem_value%");
        } else {
            getLogger().warning("PlaceholderAPI 未找到，变量功能不可用");
        }

        // 启动自动保存
        startAutoSave();
        getLogger().info("=======================================");
        getLogger().info("等级系统插件已启用！作者: GuardTeam-ysxiaoyu");
        getLogger().info("=======================================");
    }

    @Override
    public void onDisable() {
        // 保存所有数据
        if (dataManager != null) {
            dataManager.saveAllData();
        }

        // 注销PlaceholderAPI扩展
        if (levelExpansion != null) {
            levelExpansion.unregister();
        }

        getLogger().info("等级系统插件已禁用！");
    }

    private void startAutoSave() {
        int interval = getConfig().getInt("data.auto-save-interval", 5) * 60 * 20; // 转换为ticks
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveAllData();
            if (getConfig().getBoolean("debug")) {
                getLogger().info("自动保存完成");
            }
        }, interval, interval);
    }

    public static LevelSystem getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public int getExpPerLevel() {
        return getConfig().getInt("exp-per-level", 100);
    }
}