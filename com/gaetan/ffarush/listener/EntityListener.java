package com.gaetan.ffarush.listener;

import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.manager.managers.SpawnKillManager;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.PlayerState;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

@AllArgsConstructor
public class EntityListener implements Listener {
    private final FFARushPlugin ffaRushPlugin;

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (this.ffaRushPlugin.getPlayer((Player) event.getEntity()).getPlayerState() != PlayerState.FIGHTING || event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                event.setCancelled(true);

            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                event.setCancelled(true);
                event.setDamage(4);
                event.getEntity().setVelocity(event.getEntity().getLocation().getDirection().multiply(1.9d).setY(3));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            final PlayerData damagerData = this.ffaRushPlugin.getPlayer(damager);

            if (damagerData.getPlayerState() == PlayerState.FIGHTING)
                this.ffaRushPlugin.getVoidPlayers().put(player.getUniqueId(), damager.getUniqueId());

            if (SpawnKillManager.isCooldownActive(damager) || SpawnKillManager.isCooldownActive(player)) {
                event.setCancelled(true);
                Message.tell(damager, Lang.ANTI_SPAWNKILL.getText());
            }

            if (damagerData.getPlayerState() != PlayerState.FIGHTING)
                event.setCancelled(true);

        }
    }

    @EventHandler
    public void onEntityExplodeEvent(final EntityExplodeEvent event) {
        event.blockList().clear();
    }
}
