package com.gaetan.ffarush.manager;

import com.gaetan.api.inventory.GuiManager;
import com.gaetan.ffarush.inventory.EditorInventory;
import com.gaetan.ffarush.manager.managers.*;
import com.gaetan.ffarush.FFARushPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class ManagerHandler {
    final FFARushPlugin ffaRushPlugin;
    final ItemManager itemManager;
    final BlockManager blockManager;
    final LocationManager locationManager;
    final SpawnKillManager spawnKillManager;
    final WorldManager worldManager;

    final GuiManager guiManager;

    /**
     * Register all the manager.
     *
     * @param ffaRushPlugin reference to the main class
     */
    public ManagerHandler(final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.itemManager = new ItemManager(this);
        this.blockManager = new BlockManager(this);
        this.locationManager = new LocationManager(this);
        this.spawnKillManager = new SpawnKillManager(this);
        this.worldManager = new WorldManager(this);

        this.guiManager = new GuiManager(this.ffaRushPlugin);
        this.guiManager.addMenu(new EditorInventory(this.ffaRushPlugin));
    }
}
