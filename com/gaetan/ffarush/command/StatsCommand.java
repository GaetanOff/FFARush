package com.gaetan.ffarush.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.data.PlayerData;
import com.gaetan.ffarush.enums.Lang;
import lombok.AllArgsConstructor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class StatsCommand {
    private final FFARushPlugin ffaRushPlugin;

    /**
     * Show the stats of a player.
     *
     * @param context command argument
     */
    @Command(name = "stats", aliases = {"elo", "stat", "statistic"}, target = CommandTarget.PLAYER)
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

    /**
     * Send the stats of a specific player.
     *
     * @param player player to send the stats
     * @param playerData target who stats requested
     */
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
