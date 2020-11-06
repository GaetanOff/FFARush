package com.lighter.ffarush.object;

import com.gaetan.api.ConfigUtil;
import com.gaetan.api.EntityHider;
import com.gaetan.api.RandomUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.manager.managers.LocationManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Setter
public final class PlayerData {
    FFARushPlugin ffaRushPlugin;
    Player player;
    PlayerState playerState;
    ItemStack[] customKit;

    public PlayerData(final Player player, final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;

        this.player = player;
        this.playerState = null;
        this.customKit = null;
    }

    public void inject() {
        this.playerState = PlayerState.LOBBY;
        this.ffaRushPlugin.getManagerHandler().getItemManager().giveDefaultItems(player);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.teleportToLobby();
    }

    public void teleportToLobby() {
        if (this.ffaRushPlugin.getManagerHandler().getLocationManager().getLobbyLocation() != null)
            this.player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getLobbyLocation());
    }

    public void teleportToFight() {
        final LocationManager locationManager = this.ffaRushPlugin.getManagerHandler().getLocationManager();

        if (locationManager.getLocationMap().isEmpty()) {
            Message.tell(this.player, Message.RED + "There are no location available.");
            return;
        }

        final int random = RandomUtil.nextBetween(0, locationManager.getLocation() - 1);

        this.playerState = PlayerState.SPAWNING;
        this.ffaRushPlugin.getManagerHandler().getItemManager().giveFightItems(this.player);
        this.ffaRushPlugin.getManagerHandler().getSpawnKillManager().run(this.player);
        this.player.teleport(locationManager.getLocation(random));
        this.player.setGameMode(GameMode.SURVIVAL);

        Message.tell(this.player, Message.GREEN + "You have joined the arena, good luck !");
    }

    public void teleportToKitEditor() {
        if (this.ffaRushPlugin.getManagerHandler().getLocationManager().getKitEditorLocation() == null) {
            Message.tell(this.player, Message.RED + "There is no kit editor available");
            return;
        }

        this.playerState = PlayerState.EDITING;

        this.player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getKitEditorLocation());
        this.player.setGameMode(GameMode.SURVIVAL);
        this.ffaRushPlugin.getManagerHandler().getItemManager().giveKitEditorItem(this.player);
        EntityHider.hidePlayerToEveryone(this.player);
    }

    public void spectator() {
        if (this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation() == null) {
            Message.tell(this.player, Message.RED + "There is no spectator location");
            return;
        }

        this.playerState = PlayerState.SPECTATING;

        this.player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation());
        this.player.setAllowFlight(true);
        this.ffaRushPlugin.getManagerHandler().getItemManager().giveSpectatorItem(this.player);
        EntityHider.hidePlayerOnly(this.player);
    }

    public int getDeaths() {
        return this.player.getStatistic(Statistic.DEATHS);
    }

    public int getKills() {
        return this.player.getStatistic(Statistic.PLAYER_KILLS);
    }

    public Serializable getRatio() {
        final double ratio = (double) this.getKills() / this.getDeaths();
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (this.getDeaths() != 0 && this.getKills() != 0)
            return df.format(ratio);

        if (this.getDeaths() == 0)
            return String.valueOf(this.getKills());

        return "0";
    }

    public void save() {
        if (this.customKit != null) {
            TaskUtil.runAsync(() -> {
                final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.player.getUniqueId().toString());
                config.getConfig().set("custom.kit", this.customKit);
                config.save();
            });
        }
    }

    public void initialize() {
        TaskUtil.runAsync(() -> {
            if (new File(this.ffaRushPlugin.getDataFolder() + "/players", this.player.getUniqueId().toString() + ".yml").exists()) {
                final ConfigUtil config = new ConfigUtil(this.ffaRushPlugin, "/players", this.player.getUniqueId().toString());
                this.customKit = (ItemStack[]) ((List) config.getConfig().getConfigurationSection("custom").get("kit")).toArray(new ItemStack[0]);
                config.delete();
            }
        });
    }
}
