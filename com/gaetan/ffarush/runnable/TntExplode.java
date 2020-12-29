package com.gaetan.ffarush.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public final class TntExplode extends BukkitRunnable {
    final FFARushPlugin ffaRushPlugin;
    final DecimalFormat format = new DecimalFormat("0.0");

    /**
     * BukkitRunnable to put a counter on the custom tnt.
     */
    @Override
    public void run() {
        this.ffaRushPlugin.getTnt().forEach(tntPrimed -> {
            tntPrimed.setCustomName(Message.DARK_RED + format.format(tntPrimed.getFuseTicks() / 20.0));
            if (!tntPrimed.isValid() || tntPrimed.getFuseTicks() <= 0)
                this.ffaRushPlugin.getTnt().remove(tntPrimed);
        });
    }
}
