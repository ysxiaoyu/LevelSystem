package com.yourname.data;

import com.yourname.LevelSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final LevelSystem plugin;
    private final Map<UUID, PlayerData> playerDataMap;
    private final File dataFolder;

    public DataManager(LevelSystem plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), k -> loadPlayerData(player.getUniqueId()));
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, this::loadPlayerData);
    }

    public void setPlayerData(UUID uuid, PlayerData data) {
        playerDataMap.put(uuid, data);
    }

    private PlayerData loadPlayerData(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) {
            return new PlayerData();
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        int level = config.getInt("level", 0);
        int tier = config.getInt("tier", 0);
        int value = config.getInt("value", 0);

        return new PlayerData(level, tier, value);
    }

    public void savePlayerData(UUID uuid) {
        PlayerData data = playerDataMap.get(uuid);
        if (data == null) return;

        File file = new File(dataFolder, uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("level", data.getLevel());
        config.set("tier", data.getTier());
        config.set("value", data.getValue());

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存玩家数据: " + uuid);
            e.printStackTrace();
        }
    }

    public void saveAllData() {
        for (UUID uuid : playerDataMap.keySet()) {
            savePlayerData(uuid);
        }
    }

    public void loadAllData() {
        // 只加载在线玩家数据
        plugin.getServer().getOnlinePlayers().forEach(player ->
                getPlayerData(player.getUniqueId())
        );
    }

    public void removePlayerData(UUID uuid) {
        savePlayerData(uuid);
        playerDataMap.remove(uuid);
    }
}