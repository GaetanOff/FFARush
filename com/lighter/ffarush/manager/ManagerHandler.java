package com.lighter.ffarush.manager;

import com.gaetan.api.inventory.GuiManager;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.inventory.EditorInventory;
import com.lighter.ffarush.manager.managers.BlockManager;
import com.lighter.ffarush.manager.managers.ItemManager;
import com.lighter.ffarush.manager.managers.LocationManager;
import com.lighter.ffarush.manager.managers.SpawnKillManager;
import lombok.Getter;

@Getter
public class ManagerHandler {
    private final FFARushPlugin ffaRushPlugin;
    private final ItemManager itemManager;
    private final BlockManager blockManager;
    private final LocationManager locationManager;
    private final SpawnKillManager spawnKillManager;

    private final GuiManager guiManager;

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
