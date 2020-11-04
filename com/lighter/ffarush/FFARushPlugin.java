package com.lighter.ffarush;

import com.gaetan.api.plugin.GCore;
import com.google.common.collect.Maps;
import com.lighter.ffarush.command.SpawnCommand;
import com.lighter.ffarush.listener.CustomMoveEvent;
import com.lighter.ffarush.listener.PlayerListener;
import com.lighter.ffarush.manager.ManagerHandler;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.scoreboard.Scoreboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import spg.lgdev.iSpigot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
public class FFARushPlugin extends GCore {
    final Map<UUID, PlayerData> players = Maps.newConcurrentMap();
    final Map<UUID, UUID> voidPlayers = new HashMap<>();
    ManagerHandler managerHandler;

    @Override
    protected void onPluginStart() {
        this.managerHandler = new ManagerHandler(this);
        this.registerCommands(new SpawnCommand(this));
        this.registerScoreboard(new Scoreboard(this));
        iSpigot.INSTANCE.addMovementHandler(new CustomMoveEvent(this));
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
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public PlayerData getPlayer(final Player player) {
        return this.players.get(player.getUniqueId());
    }
}
