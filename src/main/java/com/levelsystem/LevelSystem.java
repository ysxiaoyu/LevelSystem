package com.levelsystem;


import com.levelsystem.commands.LevelCommand;
import com.levelsystem.data.DataManager;
import com.levelsystem.listeners.ExperienceListener;
import com.levelsystem.manager.TitleManager;
import com.levelsystem.placeholder.LevelExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelSystem extends JavaPlugin {

    private static LevelSystem instance;
    private DataManager dataManager;
    private TitleManager titleManager;  // 确保声明
    private LevelExpansion levelExpansion;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.dataManager = new DataManager(this);
        this.titleManager = new TitleManager(this);  // 确保初始化
        dataManager.loadAllData();

        getServer().getPluginManager().registerEvents(new ExperienceListener(this), this);

        getCommand("level").setExecutor(new LevelCommand(this));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.levelExpansion = new LevelExpansion(this);
            levelExpansion.register();
            getLogger().info("PlaceholderAPI 扩展已注册！");
            getLogger().info("可用变量: %levelsystem_level%, %levelsystem_tier%, %levelsystem_value%");
            getLogger().info("称号变量: %levelsystem_title%, %levelsystem_titlecolored%");
        } else {
            getLogger().warning("PlaceholderAPI 未找到，变量功能不可用");
        }

        startAutoSave();

        getLogger().info("等级系统插件已启用！已加载 " + titleManager.getAllTitles().size() + " 个段位称号");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAllData();
        }
        if (levelExpansion != null) {
            levelExpansion.unregister();
        }
        getLogger().info("等级系统插件已禁用！");
    }

    private void startAutoSave() {
        int interval = getConfig().getInt("data.auto-save-interval", 5) * 60 * 20;
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

    // 关键：确保这个方法存在！
    public TitleManager getTitleManager() {
        return titleManager;
    }

    public int getExpPerLevel() {
        return getConfig().getInt("exp-per-level", 100);
    }
}