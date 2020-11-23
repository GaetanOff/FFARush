package com.lighter.ffarush.runnable;

import com.gaetan.api.ConfigUtil;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.object.PlayerData;

public final class SavePlayerConfig implements Runnable {
    private final FFARushPlugin ffaRushPlugin;
    private final PlayerData playerData;

    public SavePlayerConfig(final FFARushPlugin ffaRushPlugin, final PlayerData playerData) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.playerData = playerData;
    }

    public void run() {
        if (playerData.getCustomKit() != null) {
            final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            config.getConfig().set("custom.kit", playerData.getCustomKit());
            config.save();
        }
    }
}