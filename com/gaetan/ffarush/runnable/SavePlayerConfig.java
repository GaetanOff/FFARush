package com.gaetan.ffarush.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.object.PlayerData;

public final class SavePlayerConfig implements Runnable {
    private final FFARushPlugin ffaRushPlugin;
    private final PlayerData playerData;

    public SavePlayerConfig(final FFARushPlugin ffaRushPlugin, final PlayerData playerData) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        if (playerData.getCustomKit() != null) {
            final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            config.getConfig().set("custom.kit", playerData.getCustomKit());
            config.save();
        }
    }
}