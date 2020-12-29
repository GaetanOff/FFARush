package com.gaetan.ffarush;

import com.gaetan.api.plugin.GCore;
import com.gaetan.ffarush.command.FFACommand;
import com.gaetan.ffarush.command.StatsCommand;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.listener.BlockListener;
import com.gaetan.ffarush.listener.EntityListener;
import com.gaetan.ffarush.listener.PlayerListener;
import com.gaetan.ffarush.manager.ManagerHandler;
import com.gaetan.ffarush.runnable.CustomMoveEvent;
import com.gaetan.ffarush.runnable.TntExplode;
import com.gaetan.ffarush.scoreboard.Scoreboard;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class FFARushPlugin extends GCore {
    final Map<Player, PlayerData> players = Maps.newConcurrentMap();
    ManagerHandler managerHandler;

    /**
     * Same as the classic onEnable.
     */
    @Override
    protected void onPluginStart() {
        this.managerHandler = new ManagerHandler(this);
        this.registerCommands(
                new FFACommand(this),
                new StatsCommand(this)
        );
        this.registerScoreboard(new Scoreboard(this), 40L);
    }

    /**
     * Same as the classic onDisable.
     */
    @Override
    protected void onPluginStop() {
        this.managerHandler.getBlockManager().resetBlock();
    }

    /**
     * This is trigger when the server finished loading.
     */
    @Override
    protected void onPluginLoaded() {
        this.getServer().getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = new PlayerData(player, this);
            this.getPlayers().put(player, playerData);
            playerData.initialize();
            playerData.injectToLobby();
            player.updateInventory();
        });
    }

    /**
     * Register listener and task.
     */
    @Override
    protected void registerListener() {
        new BlockListener(this);
        new EntityListener(this);
        new PlayerListener(this);
        new CustomMoveEvent(this);
        new TntExplode(this);
    }

    /**
     * Get a PlayerData of a Player.
     *
     * @param player player
     */
    public PlayerData getPlayer(final Player player) {
        return this.players.get(player);
    }
}
