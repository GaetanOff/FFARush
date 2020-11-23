package com.lighter.ffarush.manager.managers;

import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class BlockManager extends Manager {
    private final List<Block> blockList;

    public BlockManager(final ManagerHandler handler) {
        super(handler);

        this.blockList = new ArrayList<>();
    }

    public void addBlock(final Block block) {
        this.getBlockList().add(block);
        TaskUtil.runLater(() -> {
            if (this.getBlockList().contains(block)) {
                block.setType(Material.AIR);
                this.getBlockList().remove(block);
            }
        },600L);
    }

    public void removeBlock(final Block block) {
        this.getBlockList().remove(block);
    }

    public void resetBlock() {
        blockList.forEach(block -> block.setType(Material.AIR));
    }

    public void spawnTnt(final Block target) {
        final Location location = target.getLocation().add(0.5, 0.25, 0.5);
        final TNTPrimed tnt = (TNTPrimed) target.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        tnt.setVelocity(new Vector(0.0, 0.25, 0.0));
        tnt.setCustomNameVisible(true);
        tnt.teleport(location);

        new BukkitRunnable() {
            final DecimalFormat format = new DecimalFormat("0.0");

            @Override
            public void run() {
                tnt.setCustomName(Message.DARK_RED + format.format(tnt.getFuseTicks() / 20.0));
                if (!tnt.isValid() || tnt.getFuseTicks() <= 0)
                    cancel();
            }
        }.runTaskTimer(this.handler.getFfaRushPlugin(), 0L, 1L);
    }
}
