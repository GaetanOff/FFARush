package com.gaetan.ffarush;

import com.gaetan.api.plugin.GCore;
import com.gaetan.ffarush.command.FFACommand;
import com.gaetan.ffarush.listener.BlockListener;
import com.gaetan.ffarush.listener.EntityListener;
import com.gaetan.ffarush.listener.PlayerListener;
import com.gaetan.ffarush.manager.ManagerHandler;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.runnable.CustomMoveEvent;
import com.gaetan.ffarush.runnable.TntExplode;
import com.gaetan.ffarush.scoreboard.Scoreboard;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gaetan.ffarush.command.StatsCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class FFARushPlugin extends GCore {
    final Map<UUID, PlayerData> players = Maps.newConcurrentMap();
    final List<TNTPrimed> tnt = Lists.newLinkedList();
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
            this.getPlayers().put(player.getUniqueId(), playerData);
            playerData.initialize();
            playerData.injectToLobby();
        });
    }

    /**
     * Register listener and task.
     */
    @Override
    protected void registerListener() {
        this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new CustomMoveEvent(this).runTaskTimer(this, 0L, 40L);
        new TntExplode(this).runTaskTimer(this, 0L, 1L);
    }

    /**
     * Get a PlayerData of a Player.
     *
     * @param player player
     */
    public PlayerData getPlayer(final Player player) {
        return this.players.get(player.getUniqueId());
    }
}
