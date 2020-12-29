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

    /**
     * BukkitRunnable to replace the Bukkit Move Event.
     */
    @Override
    public void run() {
        this.ffaRushPlugin.getServer().getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

            for (final Player playerInList : playerData.getVoidPlayers()) {
                final PlayerData playerInListData = this.ffaRushPlugin.getPlayer(playerInList);

                if (playerInListData.getPlayerState() == PlayerState.FIGHTING) {
                    if (playerInList.getLocation().getY() < 29) {
                        playerData.getVoidPlayers().remove(playerInList);
                        playerInListData.injectToLobby();
                        Message.tellToEveryone(Message.RED + playerInList.getName() + Message.GRAY + Lang.VOID_DEATH.getText() + Message.GREEN + player.getName() + Message.GRAY + ".");
                        player.setStatistic(Statistic.PLAYER_KILLS, player.getStatistic(Statistic.PLAYER_KILLS) + 1);
                        player.setHealth(20.0);
                        playerInList.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
                    }
                }
            }

            if (playerData.getPlayerState() == PlayerState.SPECTATING) {
                if (player.getLocation().getY() < 29)
                    player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation());
            }

            if (playerData.getPlayerState() == PlayerState.FIGHTING) {
                if (player.getLocation().getY() < 29) {
                    playerData.injectToLobby();
                    Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + Lang.VOID_DEATH_ALONE.getText());
                }
            }
        });
    }
}
