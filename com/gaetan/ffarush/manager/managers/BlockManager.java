package com.gaetan.ffarush.manager.managers;

import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.ffarush.manager.Manager;
import com.gaetan.ffarush.manager.ManagerHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class BlockManager extends Manager {
    private final List<Block> blockList;

    /**
     * Constructor for the BlockManager.
     *
     * @param handler reference to the ManagerHandler class
     */
    public BlockManager(final ManagerHandler handler) {
        super(handler);

        this.blockList = new ArrayList<>();
    }

    /**
     * Add a block in the delete list.
     *
     * @param block block added
     */
    public void addBlock(final Block block) {
        this.getBlockList().add(block);
        TaskUtil.runLater(() -> {
            if (this.getBlockList().contains(block)) {
                block.setType(Material.AIR);
                this.getBlockList().remove(block);
            }
        }, 600L);
    }

    /**
     * Remove a block from the delete list.
     *
     * @param block block removed
     */
    public void removeBlock(final Block block) {
        this.getBlockList().remove(block);
    }

    /**
     * Reset all the block in the list.
     */
    public void resetBlock() {
        blockList.forEach(block -> block.setType(Material.AIR));
    }

    /**
     * Spawn an custom tnt.
     *
     * @param target real tnt replaced by the custom tnt
     */
    public void spawnTnt(final Block target) {
        final Location location = target.getLocation().add(0.5, 0.25, 0.5);
        final TNTPrimed tnt = (TNTPrimed) target.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        tnt.setVelocity(new Vector(0.0, 0.25, 0.0));
        tnt.setCustomNameVisible(true);
        tnt.teleport(location);

        //Add the counter on the tnt
        this.handler.getFfaRushPlugin().getTnt().add(tnt);
    }
}
