package com.lighter.ffarush.scoreboard;

import com.gaetan.api.sidebar.SidebarEntry;
import com.gaetan.api.sidebar.SidebarProvider;
import com.lighter.ffarush.FFARushPlugin;
import com.lighter.ffarush.enums.Lang;
import com.lighter.ffarush.object.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class Scoreboard extends SidebarProvider {
    private final FFARushPlugin ffaRushPlugin;

    @Override
    public String getTitle(final Player player) {
        return Lang.SB_TITLE.getText();
    }

    @Override
    public List<SidebarEntry> getLines(final Player player) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        final List<SidebarEntry> lines = new ArrayList<>();
        lines.add(new SidebarEntry(Lang.SB_BAR.getText()));
        lines.add(new SidebarEntry(Lang.SB_KILLS.getText() + playerData.getKills()));
        lines.add(new SidebarEntry(Lang.SB_DEATHS.getText() + playerData.getDeaths()));
        lines.add(new SidebarEntry(Lang.SB_RATIO.getText() + playerData.getRatio()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Lang.SB_PLAYERS.getText() + this.ffaRushPlugin.getServer().getOnlinePlayers().size()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Lang.SB_INFO.getText()));
        lines.add(new SidebarEntry(Lang.SB_BAR.getText()));
        return lines;
    }
}
