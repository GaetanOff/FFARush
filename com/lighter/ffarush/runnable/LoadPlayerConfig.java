package com.lighter.ffarush.runnable;

import com.gaetan.api.ConfigUtil;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.object.PlayerData;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public final class LoadPlayerConfig implements Runnable {
    private final FFARushPlugin ffaRushPlugin;
    private final PlayerData playerData;

    public LoadPlayerConfig(final FFARushPlugin ffaRushPlugin, final PlayerData playerData) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.playerData = playerData;
    }

    public void run() {
        if (new File(this.ffaRushPlugin.getDataFolder() + "/players", this.playerData.getPlayer().getUniqueId().toString() + ".yml").exists()) {
            final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            playerData.setCustomKit((ItemStack[]) ((List) config.getConfig().getConfigurationSection("custom").get("kit")).toArray(new ItemStack[0]));
            config.delete();
        }
    }
}
