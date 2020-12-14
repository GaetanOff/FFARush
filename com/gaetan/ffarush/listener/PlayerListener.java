package com.gaetan.ffarush.listener;

import com.gaetan.api.EntityHider;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.enums.PlayerState;
import com.gaetan.ffarush.inventory.EditorInventory;
import com.gaetan.ffarush.data.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PlayerListener implements Listener {
    private final FFARushPlugin ffaRushPlugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = new PlayerData(player, this.ffaRushPlugin);
        this.ffaRushPlugin.getPlayers().put(player.getUniqueId(), playerData);

        playerData.initialize();
        playerData.inject();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.ffaRushPlugin.getPlayers().remove(event.getPlayer().getUniqueId()).save();
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
                    this.ffaRushPlugin.getManagerHandler().getGuiManager().open(player, EditorInventory.class);
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
        final PlayerData playerData = this.ffaRushPlugin.getPlayer((Player) event.getWhoClicked());

        if (playerData.getPlayerState() == PlayerState.LOBBY || playerData.getPlayerState() == PlayerState.SPECTATING)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        event.setDeathMessage(Message.RED + event.getEntity().getName() + Message.GRAY + Lang.PLAYER_DEATH.getText() + ((event.getEntity().getKiller() != null) ? (" by ") + Message.GREEN + event.getEntity().getKiller().getName() : "") + Message.GRAY + ".");
        if (event.getEntity().getKiller() != null)
            event.getEntity().getKiller().setHealth(20.0);

        if (playerData.getVoidPlayers() != null)
            playerData.getVoidPlayers().remove(player);

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
}
