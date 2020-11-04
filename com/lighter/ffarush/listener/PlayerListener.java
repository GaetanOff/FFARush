package com.lighter.ffarush.listener;

import com.gaetan.api.EntityHider;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.inventory.EditorInventory;
import com.lighter.ffarush.manager.managers.SpawnKillManager;
import com.lighter.ffarush.object.PlayerData;
import com.lighter.ffarush.object.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class PlayerListener implements Listener {
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
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) && playerData.getPlayerState() == PlayerState.LOBBY) {
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
                        playerData.teleportToKitEditor();
                        break;
                    }
                }
            }
            if (itemStack.getType() == Material.FLINT_AND_STEEL && event.getAction() == Action.RIGHT_CLICK_BLOCK && target != null && target.getType() == Material.TNT) {
                target.setType(Material.AIR);
                event.setCancelled(true);
                final Location location = target.getLocation().add(0.5, 0.25, 0.5);
                final TNTPrimed tnt = (TNTPrimed) target.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
                tnt.setVelocity(new Vector(0.0, 0.25, 0.0));
                tnt.setCustomNameVisible(true);
                tnt.setCustomName(Message.DARK_AQUA + "Prathen " + Message.AQUA + "TNT");
                tnt.teleport(location);
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
                    EntityHider.showPlayer(player);
                    playerData.inject();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (this.getFfaRushPlugin().getPlayer((Player) event.getWhoClicked()).getPlayerState() == PlayerState.LOBBY)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (this.getFfaRushPlugin().getPlayer((Player) event.getEntity()).getPlayerState() != PlayerState.FIGHTING || event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                event.setCancelled(true);

            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                event.setCancelled(true);
                event.setDamage(4);
                event.getEntity().setVelocity(event.getEntity().getLocation().getDirection().multiply(1.9d).setY(2));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            if (this.getFfaRushPlugin().getPlayer(damager).getPlayerState() == PlayerState.FIGHTING)
                this.getFfaRushPlugin().getVoidPlayers().put(player.getUniqueId(), damager.getUniqueId());

            if (SpawnKillManager.isCooldownActive(damager) || SpawnKillManager.isCooldownActive(player)) {
                event.setCancelled(true);
                Message.tell(damager, Message.RED + "You have to wait the end of Anti SpawnKill time.");
            }
        }
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        event.setDeathMessage(Message.RED + event.getEntity().getName() + Message.GRAY + " was killed" + ((event.getEntity().getKiller() != null) ? (" by ") + Message.GREEN + event.getEntity().getKiller().getName() : "") + Message.GRAY + ".");
        if (event.getEntity().getKiller() != null)
            event.getEntity().getKiller().setHealth(20.0);

        if (this.getFfaRushPlugin().getVoidPlayers().get(player.getUniqueId()) != null)
            this.getFfaRushPlugin().getVoidPlayers().remove(player.getUniqueId());

        event.getDrops().clear();
        TaskUtil.runLater(() -> {
            try {
                final Object nmsPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke(player);
                final Object con = nmsPlayer.getClass().getDeclaredField("playerConnection").get(nmsPlayer);
                final Class EntityPlayer2 = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EntityPlayer");
                final Field minecraftServer = con.getClass().getDeclaredField("minecraftServer");
                minecraftServer.setAccessible(true);
                final Object mcserver = minecraftServer.get(con);
                final Object playerlist = mcserver.getClass().getDeclaredMethod("getPlayerList", new Class[0]).invoke(mcserver);
                final Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", EntityPlayer2, Integer.TYPE, Boolean.TYPE);
                moveToWorld.invoke(playerlist, nmsPlayer, 0, false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            playerData.inject();
        }, this.ffaRushPlugin, 5L);
    }

    @EventHandler
    public void onEntityExplodeEvent(final EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        if (playerData.getPlayerState() == PlayerState.BUILDING)
            return;

        if ((playerData.getPlayerState() == PlayerState.FIGHTING || playerData.getPlayerState() == PlayerState.SPAWNING) && (block.getType() == Material.SANDSTONE || block.getType() == Material.TNT) && player.getLocation().getY() < 80) {
            this.ffaRushPlugin.getManagerHandler().getBlockManager().addBlock(block);
            if (block.getType() == Material.SANDSTONE)
                player.getInventory().getItemInHand().setAmount(64);

            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        if (playerData.getPlayerState() == PlayerState.BUILDING)
            return;

        if ((playerData.getPlayerState() == PlayerState.FIGHTING || playerData.getPlayerState() == PlayerState.SPAWNING) && (block.getType() == Material.TNT || block.getType() == Material.SANDSTONE)) {
            this.ffaRushPlugin.getManagerHandler().getBlockManager().removeBlock(block);
            if (block.getType() == Material.SANDSTONE) {
                event.setCancelled(true);
                block.setType(Material.AIR);
            }

            return;
        }

        event.setCancelled(true);
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
