package com.gaetan.ffarush.scoreboard;

import com.gaetan.api.sidebar.SidebarEntry;
import com.gaetan.api.sidebar.SidebarProvider;
import com.gaetan.ffarush.FFARushPlugin;
import com.gaetan.ffarush.enums.Lang;
import com.gaetan.ffarush.data.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class Scoreboard extends SidebarProvider {
    private final FFARushPlugin ffaRushPlugin;

    /**
     * Set the title of the sidebar.
     *
     * @param player player who see the sidebar
     */
    @Override
    public String title(final Player player) {
        return Lang.SB_TITLE.getText();
    }

    /**
     * Set the lines of the sidebar.
     *
     * @param player player who see the sidebar
     */
    @Override
    public List<SidebarEntry> lines(final Player player) {
        final PlayerData playerData = this.ffaRushPlugin.getPlayer(player);
        final List<SidebarEntry> lines = new ArrayList<>();
        lines.add(new SidebarEntry(Lang.SB_BAR.getText()));
        lines.add(new SidebarEntry(Lang.KILLS.getText() + playerData.getKills()));
        lines.add(new SidebarEntry(Lang.DEATHS.getText() + playerData.getDeaths()));
        lines.add(new SidebarEntry(Lang.RATIO.getText() + playerData.getRatio()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Lang.SB_PLAYERS.getText() + this.ffaRushPlugin.getServer().getOnlinePlayers().size()));
        lines.add(new SidebarEntry(""));
        lines.add(new SidebarEntry(Lang.SB_INFO.getText()));
        lines.add(new SidebarEntry(Lang.SB_BAR.getText()));
        return lines;
    }
}
