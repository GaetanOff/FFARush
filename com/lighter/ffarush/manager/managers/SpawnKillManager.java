package com.lighter.ffarush.manager.managers;

import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.enums.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public final class SpawnKillManager extends Manager {
    private static WeakHashMap<Player, Long> cooldown;

    public SpawnKillManager(final ManagerHandler handler) {
        super(handler);

        SpawnKillManager.cooldown = new WeakHashMap<>();
    }

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

    public static boolean isCooldownActive(final Player player) {
        return SpawnKillManager.cooldown.containsKey(player) && SpawnKillManager.cooldown.get(player) > System.currentTimeMillis();
    }

    public static void removeCooldown(final Player player) {
        if (SpawnKillManager.cooldown.containsKey(player)) {
            SpawnKillManager.cooldown.remove(player);
            player.setLevel(0);
            player.setExp(0.0f);
        }
    }
}
