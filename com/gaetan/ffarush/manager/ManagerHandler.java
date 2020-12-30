package com.gaetan.ffarush.manager;

import com.gaetan.api.inventory.GuiManager;
import com.gaetan.ffarush.inventory.EditorInventory;
import com.gaetan.ffarush.manager.managers.*;
import com.gaetan.ffarush.FFARushPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ManagerHandler {
    FFARushPlugin ffaRushPlugin;
    ItemManager itemManager;
    BlockManager blockManager;
    LocationManager locationManager;
    WorldManager worldManager;

    GuiManager guiManager;

    /**
     * Constructor for the ManagerHandler class.
     * Register all the manager.
     *
     * @param ffaRushPlugin reference to the main class
     */
    public ManagerHandler(final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.itemManager = new ItemManager(this);
        this.blockManager = new BlockManager(this);
        this.locationManager = new LocationManager(this);
        this.worldManager = new WorldManager(this);

        this.guiManager = new GuiManager(this.ffaRushPlugin);
        this.guiManager.addMenu(new EditorInventory(this.ffaRushPlugin));
    }
}
