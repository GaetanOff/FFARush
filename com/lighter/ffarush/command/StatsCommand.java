package com.lighter.ffarush.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.enums.Lang;
import com.lighter.ffarush.object.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class StatsCommand {
    private final FFARushPlugin ffaRushPlugin;

    /**
     * Show the stats of a player
     *
     * @param context command argument
     */
    @Command(name = "stats", aliases = "elo", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        if (context.getArgs().length == 0) {
            this.statsMessage(player, this.ffaRushPlugin.getPlayer(player));
        } else if (context.getArgs().length == 1) {
            final Player target = player.getServer().getPlayer(context.getArgs()[0]);
            if (target == null) {
                Message.tell(player, Lang.PLAYER_NULL.getText());
                return;
            }
            this.statsMessage(player, this.ffaRushPlugin.getPlayer(target));
        }
    }

    private void statsMessage(final Player player, final PlayerData playerData) {
        Message.tell(player, new String[]{
                "",
                Lang.STATS_OF.getText() + playerData.getPlayer().getName(),
                "",
                Lang.KILLS.getText() + playerData.getKills(),
                Lang.DEATHS.getText() + playerData.getDeaths(),
                Lang.RATIO.getText() + playerData.getRatio(),
                ""
        });
    }
}
