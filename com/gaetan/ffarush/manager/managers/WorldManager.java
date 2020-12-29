package com.gaetan.ffarush.manager.managers;

import com.gaetan.ffarush.manager.Manager;
import com.gaetan.ffarush.manager.ManagerHandler;
import org.bukkit.Difficulty;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public final class WorldManager extends Manager {

    /**
     * Constructor for the WorldManager.
     *
     * @param handler reference to the ManagerHandler class
     */
    public WorldManager(final ManagerHandler handler) {
        super(handler);

        this.setupWorld();
    }

    /**
     * Setup all worlds for better optimization.
     */
    private void setupWorld() {
        this.handler.getFfaRushPlugin().getServer().getWorlds().forEach(world -> {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawn", "false");
            world.setGameRuleValue("doFireTick", "false");
            world.setDifficulty(Difficulty.HARD);
            world.getEntities().stream()
                    .filter(entity -> entity instanceof Item)
                    .forEach(Entity::remove);
        });
    }
}
