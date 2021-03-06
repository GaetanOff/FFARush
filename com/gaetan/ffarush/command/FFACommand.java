package com.gaetan.ffarush.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.gaetan.ffarush.manager.managers.LocationManager;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.PlayerState;
import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class FFACommand {
    /**
     * Constructor for the FFACommand class.
     *
     * @param ffaRushPlugin reference to te main class
     */
    private final FFARushPlugin ffaRushPlugin;

    /**
     * Show the global usage message.
     *
     * @param context command argument
     */
    @Command(name = "ffarush", permission = "ffarush.admin", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        this.usage((Player) context.getSender());
    }

    /**
     * Add new a location to the FFARush.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.addloc")
    public void handleSubCommand_AddLoc(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final LocationManager locationManager = this.ffaRushPlugin.getManagerHandler().getLocationManager();
        Message.tell(player, Lang.ADD_POS.getText().replace("%position%", String.valueOf(locationManager.getLocation())));
        locationManager.addLocation(player.getLocation(), String.valueOf(locationManager.getLocation()));
        locationManager.setLocation(locationManager.getLocation() + 1);
    }

    /**
     * Set the global lobby.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.set.lobby")
    public void handleSubCommand_Set_Lobby(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        this.ffaRushPlugin.getManagerHandler().getLocationManager().setLobbyLocation(player.getLocation());
        Message.tell(player, Lang.LOBBY_SET.getText());
    }

    /**
     * Set the spectator location.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.set.spectator")
    public void handleSubCommand_Set_Spectator(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        this.ffaRushPlugin.getManagerHandler().getLocationManager().setSpectatorLocation(player.getLocation());
        Message.tell(player, Lang.LOBBY_SET.getText());
    }

    /**
     * Set the kit editor.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.set.editor")
    public void handleSubCommand_Set_Editor(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        this.ffaRushPlugin.getManagerHandler().getLocationManager().setKitEditorLocation(player.getLocation());
        Message.tell(player, Lang.EDITOR_SET.getText());
    }

    /**
     * Set the kit inventory.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.set.inventory")
    public void handleSubCommand_Set_Inventory(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        if (playerData.getPlayerState() != PlayerState.BUILDING) {
            Message.tell(player, Lang.NEED_BUILDER.getText());
            return;
        }

        this.ffaRushPlugin.getManagerHandler().getItemManager().setKit(player.getInventory().getContents(), player.getInventory().getArmorContents());
        Message.tell(player, Lang.INV_UPDATE.getText());
    }

    /**
     * Enable or Disable the builder mode.
     *
     * @param context command argument
     */
    @Command(name = "ffarush.builder")
    public void handleSubCommand_Builder(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        if (playerData.getPlayerState() == PlayerState.BUILDING) {
            playerData.injectToLobby();
            Message.tell(player, Lang.BUILD_OFF.getText());
        } else {
            this.ffaRushPlugin.getPlayer(player).setPlayerState(PlayerState.BUILDING);
            player.setGameMode(GameMode.CREATIVE);
            Message.tell(player, Lang.BUILD_ON.getText());
        }
    }

    /**
     * Send the help message.
     *
     * @param player player to send the help message
     */
    private void usage(final Player player) {
        Message.tell(player, new String[]{
                "",
                Message.GOLD + Message.BOLD + "FFARush" + Message.GRAY + Message.ITALIC + " (Gaetan#7171)",
                "",
                Message.GRAY + Message.ITALIC + "Admin Command:",
                Message.YELLOW + "/ffarush addloc" + Message.GRAY + " - Add a location.",
                Message.YELLOW + "/ffarush set lobby" + Message.GRAY + " - Set the lobby.",
                Message.YELLOW + "/ffarush set spectator" + Message.GRAY + " - Set the spectator.",
                Message.YELLOW + "/ffarush set editor" + Message.GRAY + " - Set the kit editor.",
                Message.YELLOW + "/ffarush set inventory" + Message.GRAY + " - Modify the kit.",
                Message.YELLOW + "/ffarush builder" + Message.GRAY + " - Enable builder mode.",
                "",
                Message.GRAY + Message.ITALIC + "Normal Command:",
                ""
        });
    }
}
