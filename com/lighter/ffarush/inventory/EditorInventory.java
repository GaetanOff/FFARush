package com.lighter.ffarush.inventory;

import com.gaetan.api.inventory.GuiBuilder;
import com.gaetan.api.item.ItemBuilder;
import com.gaetan.api.message.Message;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.command.Lang;
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
        inventory.setItem(4, new ItemBuilder(Material.ENDER_CHEST).setName(Lang.EDITOR_ITEM_SAVE.getText()).toItemStack());
        inventory.setItem(20, new ItemBuilder(Material.BOOK).setName(Lang.EDITOR_ITEM_LOAD.getText()).toItemStack());
        inventory.setItem(24, new ItemBuilder(Material.REDSTONE).setName(Lang.EDITOR_ITEM_RESET.getText()).toItemStack());
    }

    @Override
    public void onClick(final Player player, final Inventory inventory, final ItemStack itemStack, final int i) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);

        switch (itemStack.getType()) {
            case ENDER_CHEST: {
                playerData.setCustomKit(player.getInventory().getContents());
                Message.tell(player, Lang.EDITOR_SAVE.getText());
                break;
            }
            case BOOK: {
                player.getInventory().clear();
                player.updateInventory();
                if (playerData.getCustomKit() == null)
                    player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getMainContents());
                else
                    player.getInventory().setContents(playerData.getCustomKit());

                Message.tell(player, Lang.EDITOR_LOAD.getText());
                break;
            }
            case REDSTONE: {
                player.getInventory().clear();
                player.updateInventory();
                player.getInventory().setContents(this.ffaRushPlugin.getManagerHandler().getItemManager().getMainContents());
                playerData.setCustomKit(null);
                Message.tell(player, Lang.EDITOR_RESET.getText());
                break;
            }
        }
    }

    @Override
    public void onClose(final Player player, final Inventory inventory) {
    }
}
