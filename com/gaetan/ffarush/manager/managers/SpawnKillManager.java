package com.gaetan.ffarush.manager.managers;

import com.gaetan.ffarush.manager.Manager;
import com.gaetan.ffarush.manager.ManagerHandler;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public final class SpawnKillManager extends Manager {
    private static WeakHashMap<Player, Long> cooldown;

    /**
     * Constructor for the SpawnKillManager.
     *
     * @param handler reference to the ManagerHandler class
     */
    public SpawnKillManager(final ManagerHandler handler) {
        super(handler);

        SpawnKillManager.cooldown = new WeakHashMap<>();
    }

    /**
     * Run the anti-spawnkill on a player.
     *
     * @param player player to run the anti-spawnkill
     */
    public void run(final Player player) {
        final PlayerData playerData = this.handler.getFfaRushPlugin().getPlayer(player);

        SpawnKillManager.cooldown.put(player, System.currentTimeMillis() + 3 * 1000);
        new BukkitRunnable() {
            public void run() {
                if (SpawnKillManager.isCooldownActive(player)) {
                    final int millisLeft = (int) (SpawnKillManager.cooldown.get(player) + 1000 - System.currentTimeMillis());
                    final float percentLeft = millisLeft / TimeUnit.SECONDS.toMillis(16L);
                    player.setExp(percentLeft);
                    player.setLevel(millisLeft / 1000);
                } else {
                    SpawnKillManager.removeCooldown(player);
                    playerData.setPlayerState(PlayerState.FIGHTING);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.handler.getFfaRushPlugin(), 0L, 2L);
    }

    /**
     * Get if the player has the anti-spawnkill.
     *
     * @param player player to get the anti-spawnkill activity
     */
    public static boolean isCooldownActive(final Player player) {
        return SpawnKillManager.cooldown.containsKey(player) && SpawnKillManager.cooldown.get(player) > System.currentTimeMillis();
    }

    /**
     * Remove the player anti-spawnkill.
     *
     * @param player player to remove the anti-spawnkill
     */
    public static void removeCooldown(final Player player) {
        if (SpawnKillManager.cooldown.containsKey(player)) {
            SpawnKillManager.cooldown.remove(player);
            player.setLevel(0);
            player.setExp(0.0f);
        }
    }
}
