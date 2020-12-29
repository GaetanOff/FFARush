package com.gaetan.ffarush.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.manager.managers.BlockManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class TntExplode extends BukkitRunnable {
    final DecimalFormat format;
    final BlockManager blockManager;

    /**
     * Constructor for the TntExplode class.
     *
     * @param ffaRushPlugin reference to te main class
     */
    public TntExplode(final FFARushPlugin ffaRushPlugin) {
        this.format = new DecimalFormat("0.0");
        this.blockManager = ffaRushPlugin.getManagerHandler().getBlockManager();
        this.runTaskTimer(ffaRushPlugin, 0L, 1L);
    }

    /**
     * BukkitRunnable to put a counter on the custom tnt.
     */
    @Override
    public void run() {
        this.blockManager.getTnt().forEach(tntPrimed -> {
            tntPrimed.setCustomName(Message.DARK_RED + format.format(tntPrimed.getFuseTicks() / 20.0));
            if (!tntPrimed.isValid() || tntPrimed.getFuseTicks() <= 0)
                this.blockManager.getTnt().remove(tntPrimed);
        });
    }
}
