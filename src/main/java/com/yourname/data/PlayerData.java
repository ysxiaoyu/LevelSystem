package com.yourname.data;

public class PlayerData {
    private int level;      // 级 (0-9循环)
    private int tier;       // 段 (每10级=1段)
    private int value;      // 当前值

    public PlayerData() {
        this.level = 0;
        this.tier = 0;
        this.value = 0;
    }

    public PlayerData(int level, int tier, int value) {
        this.level = level;
        this.tier = tier;
        this.value = value;
    }

    // Getters and Setters
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(0, level);
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = Math.max(0, tier);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = Math.max(0, value);
    }

    // 获取总等级 (用于显示或计算)
    public int getTotalLevel() {
        return tier * 10 + level;
    }

    // 设置总等级 (反向计算)
    public void setTotalLevel(int totalLevel) {
        this.tier = totalLevel / 10;
        this.level = totalLevel % 10;
    }

    // 增加经验值
    public void addValue(int amount, int expPerLevel) {
        this.value += amount;

        // 检查升级
        while (this.value >= expPerLevel) {
            this.value -= expPerLevel;
            levelUp();
        }
    }

    // 升级逻辑
    private void levelUp() {
        this.level++;
        if (this.level >= 10) {
            this.level = 0;
            this.tier++;
        }
    }

    @Override
    public String toString() {
        return "PlayerData{level=" + level + ", tier=" + tier + ", value=" + value + "}";
    }
}