package com.gaetan.ffarush;

import com.gaetan.api.plugin.GCore;
import com.gaetan.ffarush.command.FFACommand;
import com.gaetan.ffarush.listener.BlockListener;
import com.gaetan.ffarush.listener.EntityListener;
import com.gaetan.ffarush.listener.PlayerListener;
import com.gaetan.ffarush.manager.ManagerHandler;
import com.gaetan.ffarush.object.PlayerData;
import com.gaetan.ffarush.runnable.CustomMoveEvent;
import com.gaetan.ffarush.scoreboard.Scoreboard;
import com.google.common.collect.Maps;
import com.gaetan.ffarush.command.StatsCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class FFARushPlugin extends GCore {
    final Map<UUID, PlayerData> players = Maps.newConcurrentMap();
    final Map<UUID, UUID> voidPlayers = new HashMap<>();
    ManagerHandler managerHandler;

    @Override
    protected void onPluginStart() {
        this.managerHandler = new ManagerHandler(this);
        this.registerCommands(
                new FFACommand(this),
                new StatsCommand(this)
        );
        this.registerScoreboard(new Scoreboard(this), 40L);
    }

    @Override
    protected void onPluginStop() {
        this.managerHandler.getBlockManager().resetBlock();
    }

    @Override
    protected void onPluginLoaded() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final PlayerData playerData = new PlayerData(player, this);
            this.getPlayers().put(player.getUniqueId(), playerData);
            playerData.initialize();
            playerData.inject();
        });
    }

    @Override
    protected void registerListener() {
        this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new CustomMoveEvent(this).runTaskTimer(this, 0L, 20L);
    }

    public PlayerData getPlayer(final Player player) {
        return this.players.get(player.getUniqueId());
    }
}
