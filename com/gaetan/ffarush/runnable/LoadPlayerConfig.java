package com.gaetan.ffarush.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.ffarush.object.PlayerData;
import com.gaetan.ffarush.FFARushPlugin;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class LoadPlayerConfig implements Runnable {
    final FFARushPlugin ffaRushPlugin;
    final PlayerData playerData;

    public LoadPlayerConfig(final FFARushPlugin ffaRushPlugin, final PlayerData playerData) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        if (new File(this.ffaRushPlugin.getDataFolder() + "/players", this.playerData.getPlayer().getUniqueId().toString() + ".yml").exists()) {
            final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            playerData.setCustomKit((ItemStack[]) ((List) config.getConfig().getConfigurationSection("custom").get("kit")).toArray(new ItemStack[0]));
            config.delete();
        }
    }
}
