package com.gaetan.ffarush.manager.managers;

import com.gaetan.api.PlayerUtil;
import com.gaetan.api.item.ItemBuilder;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.manager.Manager;
import com.gaetan.ffarush.manager.ManagerHandler;
import com.gaetan.ffarush.data.PlayerData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public final class ItemManager extends Manager {
    ItemStack[] defaultItems;
    ItemStack[] spectatorItems;
    ItemStack[] mainContents;
    ItemStack[] armorContents;

    public ItemManager(final ManagerHandler handler) {
        super(handler);

        this.loadKit();
        this.loadSpawnItems();
        this.loadSpectatorItems();
    }

    private void loadSpawnItems() {
        this.defaultItems = new ItemStack[]{
                null,
                null,
                new ItemBuilder(Material.DIAMOND_SWORD).setName(Lang.ITEM_JOIN.getText()).setUnbreakable().toItemStack(),
                null,
                new ItemBuilder(Material.COMPASS).setName(Lang.ITEM_SPECTATOR.getText()).toItemStack(),
                null,
                new ItemBuilder(Material.BOOK).setName(Lang.ITEM_EDITOR.getText()).toItemStack(),
                null,
                null
        };
    }

    private void loadSpectatorItems() {
        this.spectatorItems = new ItemStack[]{
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new ItemBuilder(Material.INK_SACK, 1, (short) 1).setName(Lang.ITEM_LEAVE.getText()).toItemStack()
        };
    }

    public void giveFightItems(final Player player) {
        final PlayerData playerData = this.handler.getFfaRushPlugin().getPlayer(player);

        this.handler.getSpawnKillManager().run(player);

        PlayerUtil.clearInventory(player, true);
        player.getInventory().setArmorContents(this.armorContents);

        TaskUtil.run(() -> {
            if (playerData.getCustomKit() != null)
                player.getInventory().setContents(playerData.getCustomKit());
            else
                player.getInventory().setContents(this.mainContents);

            player.getInventory().setHeldItemSlot(0);
            player.updateInventory();
        });
    }

    public void giveKitEditorItem(final Player player) {
        PlayerUtil.clearInventory(player, true);

        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }

    public void setKit(final ItemStack[] main, final ItemStack[] armor) {
        this.mainContents = main;
        this.armorContents = armor;

        this.handler.getFfaRushPlugin().getConfig().set("inventory.main", main);
        this.handler.getFfaRushPlugin().getConfig().set("inventory.armor", armor);
        this.handler.getFfaRushPlugin().saveConfig();
    }

    private void loadKit() {
        final FileConfiguration fileConfig = this.handler.getFfaRushPlugin().getConfig();
        final ConfigurationSection arenaSection = fileConfig.getConfigurationSection("inventory");
        this.mainContents = (ItemStack[]) ((List) arenaSection.get("main")).toArray(new ItemStack[0]);
        this.armorContents = (ItemStack[]) ((List) arenaSection.get("armor")).toArray(new ItemStack[0]);
    }
}
