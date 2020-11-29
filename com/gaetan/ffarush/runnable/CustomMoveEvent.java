package com.gaetan.ffarush.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.enums.PlayerState;
import com.gaetan.ffarush.object.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public final class CustomMoveEvent extends BukkitRunnable {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

            if (playerData.getPlayerState() == PlayerState.FIGHTING) {
                if (player.getLocation().getY() < 29) {
                    playerData.inject();
                    if (this.ffaRushPlugin.getVoidPlayers().get(player.getUniqueId()) != null) {
                        final Player attacker = Bukkit.getPlayer(this.ffaRushPlugin.getVoidPlayers().get(player.getUniqueId()));

                        Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + Lang.VOID_DEATH.getText() + Message.GREEN + attacker.getName() + Message.GRAY + ".");
                        attacker.setStatistic(Statistic.PLAYER_KILLS, attacker.getStatistic(Statistic.PLAYER_KILLS) + 1);
                        attacker.setHealth(20.0);
                        player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
                        this.ffaRushPlugin.getVoidPlayers().remove(player.getUniqueId());
                        return;
                    }

                    Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + Lang.VOID_DEATH_ALONE.getText());
                }
            } else if (playerData.getPlayerState() == PlayerState.SPECTATING) {
                if (player.getLocation().getY() < 29)
                 player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation());
            }
        });
    }
}
