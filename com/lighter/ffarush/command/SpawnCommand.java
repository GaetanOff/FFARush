package com.lighter.ffarush.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.manager.managers.LocationManager;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.object.PlayerState;
import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class SpawnCommand {
    private final FFARushPlugin ffaRushPlugin;

    @Command(name = "ffarush", permission = "ffarush.admin", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        this.usage((Player) context.getSender());
    }

    @Command(name = "ffarush.addloc")
    public void handleCommandChild(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final LocationManager locationManager = this.ffaRushPlugin.getManagerHandler().getLocationManager();
        Message.tell(player, Message.GREEN + "Position " + locationManager.getLocation() + " added.");
        locationManager.addLocation(player.getLocation(), String.valueOf(locationManager.getLocation()));
        locationManager.setLocation(locationManager.getLocation() + 1);
    }

    @Command(name = "ffarush.setlobby")
    public void handleCommandChild2(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        this.ffaRushPlugin.getManagerHandler().getLocationManager().setLobbyLocation(player.getLocation());
        Message.tell(player, Message.GREEN + "Lobby sucessfully set.");
    }

    @Command(name = "ffarush.setkiteditor")
    public void handleCommandChild3(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        this.ffaRushPlugin.getManagerHandler().getLocationManager().setKitEditorLocation(player.getLocation());
        Message.tell(player, Message.GREEN + "Kit Editor sucessfully set.");
    }

    @Command(name = "ffarush.setinventory")
    public void handleCommandChild4(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        if (playerData.getPlayerState() != PlayerState.BUILDING) {
            Message.tell(player, Message.RED + "You have to be in builder mode.");
            return;
        }

        this.ffaRushPlugin.getManagerHandler().getItemManager().setKit(player.getInventory().getContents(), player.getInventory().getArmorContents());
        Message.tell(player, Message.GREEN + "Inventory updated.");
    }

    @Command(name = "ffarush.builder")
    public void handleCommandChild5(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        if (playerData.getPlayerState() == PlayerState.BUILDING) {
            playerData.inject();
            Message.tell(player, Message.RED + "Builder disabled.");
        } else {
            this.ffaRushPlugin.getPlayer(player).setPlayerState(PlayerState.BUILDING);
            player.setGameMode(GameMode.CREATIVE);
            Message.tell(player, Message.GREEN + "Builder enabled.");
        }
    }

    private void usage(final Player player) {
        Message.tell(player, new String[]{
                "",
                Message.GOLD + Message.BOLD + "FFARush" + Message.GRAY + Message.ITALIC + " (Gaetan#7171)",
                "",
                Message.GRAY + Message.ITALIC + "Admin Command:",
                Message.YELLOW + "/ffarush addloc" + Message.GRAY + " - Add a location.",
                Message.YELLOW + "/ffarush setlobby" + Message.GRAY + " - Set the lobby.",
                Message.YELLOW + "/ffarush setkiteditor" + Message.GRAY + " - Set the kit editor.",
                Message.YELLOW + "/ffarush setinventory" + Message.GRAY + " - Modify the kit.",
                Message.YELLOW + "/ffarush builder" + Message.GRAY + " - Enable builder mode.",
                "",
                Message.GRAY + Message.ITALIC + "Normal Command:",
                ""
        });
    }
}
