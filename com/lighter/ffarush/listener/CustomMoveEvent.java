package com.lighter.ffarush.listener;

import com.gaetan.api.message.Message;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.object.PlayerState;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import spg.lgdev.handler.MovementHandler;

@AllArgsConstructor
public class CustomMoveEvent implements MovementHandler {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public void handleUpdateLocation(final Player player, final Location location, final Location location1, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        if (playerData.getPlayerState() == PlayerState.FIGHTING) {
            if (player.getLocation().getY() < 29) {
                playerData.inject();
                if (this.ffaRushPlugin.getVoidPlayers().get(player.getUniqueId()) != null) {
                    final Player attacker = Bukkit.getPlayer(this.ffaRushPlugin.getVoidPlayers().get(player.getUniqueId()));

                    Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + " was void-killed by " + Message.GREEN + attacker.getName() + Message.GRAY + ".");
                    attacker.setStatistic(Statistic.PLAYER_KILLS, attacker.getStatistic(Statistic.PLAYER_KILLS) + 1);
                    attacker.setHealth(20.0);
                    player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
                    this.ffaRushPlugin.getVoidPlayers().remove(player.getUniqueId());
                    return;
                }

                Message.tellToEveryone(Message.RED + player.getName() + Message.GRAY + " fell into the void.");
            }
        } else if (playerData.getPlayerState() == PlayerState.SPECTATING) {
            if (player.getLocation().getY() < 29)
                player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation());
        }
    }

    @Override
    public void handleUpdateRotation(final Player player, final Location location, final Location location1, final PacketPlayInFlying packetPlayInFlying) {

    }
}
