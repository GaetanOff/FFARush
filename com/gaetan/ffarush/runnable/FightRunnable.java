package com.gaetan.ffarush.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.PlayerState;
import com.gaetan.ffarush.manager.managers.BlockManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FightRunnable extends BukkitRunnable {
    final FFARushPlugin ffaRushPlugin;
    final DecimalFormat format;
    final BlockManager blockManager;

    /**
     * Constructor for the TntExplode class.
     *
     * @param ffaRushPlugin reference to te main class
     */
    public FightRunnable(final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.format = new DecimalFormat("0.0");
        this.blockManager = ffaRushPlugin.getManagerHandler().getBlockManager();
        this.runTaskTimer(ffaRushPlugin, 0L, 2L);
    }

    /**
     * BukkitRunnable to put a counter on the custom tnt and for the anti spawn-kill.
     */
    @Override
    public void run() {
        this.blockManager.getTnt().forEach(tntPrimed -> {
            tntPrimed.setCustomName(Message.DARK_RED + format.format(tntPrimed.getFuseTicks() / 20.0));
            if (!tntPrimed.isValid() || tntPrimed.getFuseTicks() <= 0)
                this.blockManager.getTnt().remove(tntPrimed);
        });

        this.ffaRushPlugin.getServer().getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

            if (playerData.isSpawnKill()) {
                if (playerData.isAntiSpawnKill()) {
                    final int millisLeft = (int) (playerData.getSpawnKillLong() + 1000 - System.currentTimeMillis());
                    final float percentLeft = millisLeft / TimeUnit.SECONDS.toMillis(16L);
                    player.setExp(percentLeft);
                    player.setLevel(millisLeft / 1000);
                } else {
                    playerData.removeAntiSpawnKill();
                    playerData.setPlayerState(PlayerState.FIGHTING);
                }
            }
        });
    }
}
