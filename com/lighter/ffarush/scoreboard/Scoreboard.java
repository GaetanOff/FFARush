package com.lighter.ffarush.scoreboard;

import com.gaetan.api.message.Message;
import com.gaetan.api.sidebar.SidebarEntry;
import com.gaetan.api.sidebar.SidebarProvider;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.object.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class Scoreboard extends SidebarProvider {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public String getTitle(final Player player) {
        return Message.DARK_AQUA + "Prathen" + Message.AQUA + " FFARush";
    }

    @Override
    public List<SidebarEntry> getLines(final Player player) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        final List<SidebarEntry> lines = new ArrayList<>();
        lines.add(new SidebarEntry(Message.GRAY + Message.STRIKE_THROUGH + "--------------------"));
        lines.add(new SidebarEntry(Message.AQUA + "Kills" + Message.GRAY + " » " + Message.WHITE + playerData.getKills()));
        lines.add(new SidebarEntry(Message.AQUA + "Deaths" + Message.GRAY + " » " + Message.WHITE + playerData.getDeaths()));
        lines.add(new SidebarEntry(Message.AQUA + "Ratio" + Message.GRAY + " » " + Message.WHITE + playerData.getRatio()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Message.AQUA + "Players" + Message.GRAY + " » " + Message.WHITE + Bukkit.getOnlinePlayers().size()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Message.GRAY + Message.ITALIC + "www.prathen.eu"));
        lines.add(new SidebarEntry(Message.GRAY + Message.STRIKE_THROUGH + "--------------------"));
        return lines;
    }
}
