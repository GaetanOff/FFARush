package com.lighter.ffarush.listener;

import com.gaetan.api.EntityHider;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.inventory.EditorInventory;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.object.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import spg.lgdev.handler.MovementHandler;

@Getter
@AllArgsConstructor
public class PlayerListener implements Listener, MovementHandler {
    private final FFARushPlugin ffaRushPlugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = new PlayerData(player, this.ffaRushPlugin);
        this.getFfaRushPlugin().getPlayers().put(player.getUniqueId(), playerData);

        playerData.initialize();
        playerData.inject();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.getFfaRushPlugin().getPlayers().remove(player.getUniqueId()).save();
        this.getFfaRushPlugin().getVoidPlayers().remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        final ItemStack itemStack = event.getItem();
        final Block target = event.getClickedBlock();

        if (itemStack != null) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) && (playerData.getPlayerState() == PlayerState.LOBBY || playerData.getPlayerState() == PlayerState.SPECTATING)) {
                switch (itemStack.getType()) {
                    case DIAMOND_SWORD: {
                        playerData.teleportToFight();
                        break;
                    }
                    case BOOK: {
                        playerData.teleportToKitEditor();
                        break;
                    }
                    case COMPASS: {
                        playerData.spectator();
                        break;
                    }
                    case INK_SACK: {
                        EntityHider.showPlayerOnly(player);
                        player.setAllowFlight(false);
                        playerData.inject();
                        break;
                    }
                }
            }
            if (itemStack.getType() == Material.FLINT_AND_STEEL && event.getAction() == Action.RIGHT_CLICK_BLOCK && target != null && target.getType() == Material.TNT) {
                target.setType(Material.AIR);
                event.setCancelled(true);
                this.ffaRushPlugin.getManagerHandler().getBlockManager().spawnTnt(target);
            }
        }
        if (target != null && playerData.getPlayerState() == PlayerState.EDITING) {
            event.setCancelled(true);
            switch (target.getType()) {
                case ANVIL: {
                    this.getFfaRushPlugin().getManagerHandler().getGuiManager().open(player, EditorInventory.class);
                    break;
                }
                case WOODEN_DOOR: {
                    EntityHider.showPlayerToEveryone(player);
                    playerData.inject();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final PlayerData playerData = this.getFfaRushPlugin().getPlayer((Player) event.getWhoClicked());

        if (playerData.getPlayerState() == PlayerState.LOBBY || playerData.getPlayerState() == PlayerState.SPECTATING)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        event.setDeathMessage(Message.RED + event.getEntity().getName() + Message.GRAY + " was killed" + ((event.getEntity().getKiller() != null) ? (" by ") + Message.GREEN + event.getEntity().getKiller().getName() : "") + Message.GRAY + ".");
        if (event.getEntity().getKiller() != null)
            event.getEntity().getKiller().setHealth(20.0);

        if (this.getFfaRushPlugin().getVoidPlayers().get(player.getUniqueId()) != null)
            this.getFfaRushPlugin().getVoidPlayers().remove(player.getUniqueId());

        event.getDrops().clear();
        TaskUtil.runLater(() -> {
            player.spigot().respawn();
            this.ffaRushPlugin.getPlayer(player).inject();
        }, 5L);
    }

    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

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
