package com.gaetan.ffarush.listener;

import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.PlayerState;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

@AllArgsConstructor
public class BlockListener implements Listener {
    private final FFARushPlugin ffaRushPlugin;

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
}
