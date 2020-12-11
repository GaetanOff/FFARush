package com.gaetan.ffarush.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.enums.PlayerState;
import com.gaetan.ffarush.data.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public final class CustomMoveEvent extends BukkitRunnable {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public void run() {
        this.ffaRushPlugin.getServer().getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

            if (playerData.getPlayerState() == PlayerState.FIGHTING) {
                if (player.getLocation().getY() < 29) {
                    playerData.inject();
                    if (playerData.getVoidPlayers() != null) {
                        for (final Player playerInList : playerData.getVoidPlayers()) {
                            final Player attacker = this.ffaRushPlugin.getServer().getPlayer(playerInList.getUniqueId());

                            Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + Lang.VOID_DEATH.getText() + Message.GREEN + attacker.getName() + Message.GRAY + ".");
                            attacker.setStatistic(Statistic.PLAYER_KILLS, attacker.getStatistic(Statistic.PLAYER_KILLS) + 1);
                            attacker.setHealth(20.0);
                            player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
                            playerData.getVoidPlayers().remove(playerInList);
                        }
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
