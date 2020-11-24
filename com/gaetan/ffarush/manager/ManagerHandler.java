package com.gaetan.ffarush.manager;

import com.gaetan.api.inventory.GuiManager;
import com.gaetan.ffarush.inventory.EditorInventory;
import com.gaetan.ffarush.manager.managers.BlockManager;
import com.gaetan.ffarush.manager.managers.ItemManager;
import com.gaetan.ffarush.manager.managers.LocationManager;
import com.gaetan.ffarush.manager.managers.SpawnKillManager;
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

    final GuiManager guiManager;

    public ManagerHandler(final FFARushPlugin ffaRushPlugin) {
        this.ffaRushPlugin = ffaRushPlugin;
        this.itemManager = new ItemManager(this);
        this.blockManager = new BlockManager(this);
        this.locationManager = new LocationManager(this);
        this.spawnKillManager = new SpawnKillManager(this);

        this.guiManager = new GuiManager(this.ffaRushPlugin);
        this.guiManager.addMenu(new EditorInventory(this.ffaRushPlugin));
    }
}
