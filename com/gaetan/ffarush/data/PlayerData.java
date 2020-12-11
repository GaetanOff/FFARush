package com.gaetan.ffarush.data;

import com.gaetan.api.EntityHider;
import com.gaetan.api.PlayerUtil;
import com.gaetan.api.RandomUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.ffarush.enums.PlayerState;
import com.gaetan.ffarush.manager.managers.LocationManager;
import com.gaetan.ffarush.runnable.SavePlayerConfig;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.runnable.LoadPlayerConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public final class PlayerData {
    FFARushPlugin ffaRushPlugin;
    Player player;
    List<Player> voidPlayers;
    PlayerState playerState;
    ItemStack[] customKit;

    public PlayerData(final Player player, final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;

        this.player = player;
        this.voidPlayers = new ArrayList<>();
        this.playerState = PlayerState.LOBBY;
        this.customKit = null;
    }

    public void inject() {
        PlayerUtil.clearInventory(player, true);

        this.playerState = PlayerState.LOBBY;
        this.player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getDefaultItems());
        this.player.getInventory().setHeldItemSlot(2);
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
            Message.tell(this.player, Lang.NO_LOCATION.getText());
            return;
        }

        this.playerState = PlayerState.SPAWNING;
        this.ffaRushPlugin.getManagerHandler().getItemManager().giveFightItems(this.player);
        this.ffaRushPlugin.getManagerHandler().getSpawnKillManager().run(this.player);
        this.player.teleport(locationManager.getLocation(RandomUtil.nextBetween(0, locationManager.getLocation() - 1)));
        this.player.setGameMode(GameMode.SURVIVAL);

        Message.tell(this.player, Lang.ARENA_JOINED.getText());
    }

    public void teleportToKitEditor() {
        if (this.ffaRushPlugin.getManagerHandler().getLocationManager().getKitEditorLocation() == null) {
            Message.tell(this.player, Lang.NO_EDITOR.getText());
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
            Message.tell(this.player, Lang.NO_SPECTATOR.getText());
            return;
        }

        PlayerUtil.clearInventory(player, true);

        this.playerState = PlayerState.SPECTATING;
        this.player.teleport(this.ffaRushPlugin.getManagerHandler().getLocationManager().getSpectatorLocation());
        this.player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getSpectatorItems());
        this.player.getInventory().setHeldItemSlot(8);
        EntityHider.hidePlayerOnly(this.player);
        TaskUtil.run(() -> this.player.setAllowFlight(true));
    }

    public int getDeaths() {
        return this.player.getStatistic(Statistic.DEATHS);
    }

    public int getKills() {
        return this.player.getStatistic(Statistic.PLAYER_KILLS);
    }

    public Serializable getRatio() {
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (this.getDeaths() != 0 && this.getKills() != 0)
            return df.format((double) this.getKills() / this.getDeaths());

        if (this.getDeaths() == 0)
            return String.valueOf(this.getKills());

        return "0";
    }

    public void save() {
        this.ffaRushPlugin.getServer().getScheduler().runTaskAsynchronously(this.ffaRushPlugin, new SavePlayerConfig(this.ffaRushPlugin, this));
    }

    public void initialize() {
        this.ffaRushPlugin.getServer().getScheduler().runTaskAsynchronously(this.ffaRushPlugin, new LoadPlayerConfig(this.ffaRushPlugin, this));
    }
}
