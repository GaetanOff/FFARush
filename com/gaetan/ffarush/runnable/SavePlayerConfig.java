package com.gaetan.ffarush.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.data.PlayerData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public final class SavePlayerConfig implements Runnable {
    final FFARushPlugin ffaRushPlugin;
    final PlayerData playerData;

    @Override
    public void run() {
        if (playerData.getCustomKit() != null) {
            final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            config.getConfig().set("custom.kit", playerData.getCustomKit());
            config.save();
        }
    }
}