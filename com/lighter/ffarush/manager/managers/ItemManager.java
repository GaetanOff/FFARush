package com.lighter.ffarush.manager.managers;

import com.gaetan.api.item.ItemBuilder;
import com.gaetan.api.message.Message;
import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import com.lighter.ffarush.object.PlayerData;
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

@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Setter
public final class ItemManager extends Manager {
    ItemStack[] mainContents;
    ItemStack[] armorContents;

    public ItemManager(final ManagerHandler handler) {
        super(handler);

        this.loadKit();
    }

    public void giveDefaultItems(final Player player) {
        final ItemStack epee = new ItemBuilder(Material.DIAMOND_SWORD).setName(Message.AQUA + "Join the arena").setUnbreakable().toItemStack();
        final ItemStack editkit = new ItemBuilder(Material.BOOK).setName(Message.AQUA + "Kit Editor").toItemStack();
        final ItemStack spectator = new ItemBuilder(Material.COMPASS).setName(Message.AQUA + "Spectator").toItemStack();

        this.clearInventory(player);

        player.getInventory().setItem(2, epee);
        player.getInventory().setItem(4, spectator);
        player.getInventory().setItem(6, editkit);
        player.getInventory().setHeldItemSlot(2);
    }

    public void giveFightItems(final Player player) {
        final PlayerData playerData = this.handler.getFfaRushPlugin().getPlayer(player);

        this.handler.getSpawnKillManager().run(player);

        this.clearInventory(player);
        player.getInventory().setArmorContents(this.armorContents);
        if (playerData.getCustomKit() != null)
            player.getInventory().setContents(playerData.getCustomKit());
        else
            player.getInventory().setContents(this.mainContents);

        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }

    public void giveSpectatorItem(final Player player) {
        final ItemStack leave = new ItemBuilder(Material.INK_SACK, 1, (short) 1).setName(Message.RED + "Leave spectator").toItemStack();

        this.clearInventory(player);

        player.getInventory().setItem(8, leave);
        player.getInventory().setHeldItemSlot(8);
    }

    public void giveKitEditorItem(final Player player) {
        this.clearInventory(player);

        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }

    private void clearInventory(final Player player) {
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setMaximumNoDamageTicks(20);
        player.setFoodLevel(20);
        player.setHealth(20.0);
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setMaximumNoDamageTicks(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
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
