package com.lighter.ffarush.manager.managers;

import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;

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
        }, this.handler.getFfaRushPlugin(), 600L);
    }

    public void removeBlock(final Block block) {
        this.getBlockList().remove(block);
    }

    public void resetBlock() {
        blockList.forEach(block -> block.setType(Material.AIR));
    }
}
