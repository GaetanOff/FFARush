package com.lighter.ffarush.inventory;

import com.gaetan.api.inventory.GuiBuilder;
import com.gaetan.api.item.ItemBuilder;
import com.gaetan.api.message.Message;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.object.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public final class EditorInventory implements GuiBuilder {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public String name() {
        return Message.GRAY + "Kit Editor";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void contents(final Player player, final Inventory inventory) {
        final ItemStack save = new ItemBuilder(Material.ENDER_CHEST).setName(Message.GREEN + "Save Kit").toItemStack();
        final ItemStack load = new ItemBuilder(Material.BOOK).setName(Message.AQUA + "Load Kit").toItemStack();
        final ItemStack reset = new ItemBuilder(Material.REDSTONE).setName(Message.RED + "Reset Kit").toItemStack();

        inventory.setItem(4, save);
        inventory.setItem(20, load);
        inventory.setItem(24, reset);
    }

    @Override
    public void onClick(final Player player, final Inventory inventory, final ItemStack itemStack, final int i) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        switch (itemStack.getType()) {
            case ENDER_CHEST: {
                playerData.setCustomKit(player.getInventory().getContents());
                Message.tell(player, Message.GREEN + "Saved successfully.");
                break;
            }
            case BOOK: {
                player.getInventory().clear();
                player.updateInventory();
                if (playerData.getCustomKit() == null)
                    player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getMainContents());
                else
                    player.getInventory().setContents(playerData.getCustomKit());

                Message.tell(player, Message.GREEN + "Loaded successfully.");
                break;
            }
            case REDSTONE: {
                player.getInventory().clear();
                player.updateInventory();
                player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getMainContents());
                playerData.setCustomKit(null);
                Message.tell(player, Message.RED + "Reset successfully.");
                break;
            }
        }
    }
}
