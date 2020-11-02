package com.lighter.ffarush.manager.managers;

import com.gaetan.api.item.ItemBuilder;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import com.lighter.ffarush.object.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Getter
@Setter
public final class ItemManager extends Manager {
    private ItemStack[] mainContents;
    private ItemStack[] armorContents;

    public ItemManager(final ManagerHandler handler) {
        super(handler);

        this.loadKit();
    }

    public void giveDefaultItems(final Player player) {
        final ItemStack epee = new ItemBuilder(Material.DIAMOND_SWORD).setName(Message.AQUA + "Join the arena").setUnbreakable().toItemStack();
        final ItemStack editkit = new ItemBuilder(Material.BOOK).setName(Message.AQUA + "Kit Editor").setUnbreakable().toItemStack();
        final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(Message.GRAY + Message.ITALIC + "www.prathen.eu").toItemStack();

        this.clearInventory(player);

        for (int i = 0; i < 9; i++) player.getInventory().setItem(i, glass);

        player.getInventory().setItem(3, epee);
        player.getInventory().setItem(5, editkit);
        player.getInventory().setHeldItemSlot(3);
    }

    public void giveFightItems(final Player player) {
        final PlayerData playerData = this.handler.getFfaRushPlugin().getPlayer(player);

        this.handler.getSpawnKillManager().run(player);
        TaskUtil.run(() -> {

            this.clearInventory(player);
            player.getInventory().setArmorContents(this.armorContents);
            if (playerData.getCustomKit() != null)
                player.getInventory().setContents(playerData.getCustomKit());
            else
                player.getInventory().setContents(this.mainContents);

            player.getInventory().setHeldItemSlot(0);
            player.updateInventory();

        });
    }

    public void giveKitEditorItem(final Player player) {
        this.clearInventory(player);

        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }

    private void clearInventory(final Player player) {
        for (final PotionEffect potion : player.getActivePotionEffects()) player.removePotionEffect(potion.getType());
        player.setGameMode(GameMode.SURVIVAL);
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
