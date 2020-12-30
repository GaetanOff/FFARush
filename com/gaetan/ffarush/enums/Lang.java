package com.gaetan.ffarush.enums;

import com.gaetan.api.message.Message;
import lombok.Getter;

@Getter
public enum Lang {
    ADD_POS(Message.GREEN + "Position %position% added."),
    LOBBY_SET(Message.GREEN + "Lobby sucessfully set."),
    SPEC_SET(Message.GREEN + "Spectator sucessfully set."),
    EDITOR_SET(Message.GREEN + "Kit Editor sucessfully set."),
    NEED_BUILDER(Message.RED + "You have to be in builder mode."),
    INV_UPDATE(Message.GREEN + "Inventory updated."),
    BUILD_ON(Message.GREEN + "Builder enabled."),
    BUILD_OFF(Message.RED + "Builder disabled."),
    ITEM_JOIN(Message.AQUA + Message.BOLD + "Join" + Message.GRAY + " ♦ Right Click"),
    ITEM_SPECTATOR(Message.AQUA + Message.BOLD + "Spectator" + Message.GRAY + " ♦ Right Click"),
    ITEM_EDITOR(Message.AQUA + Message.BOLD + "Kit Editor" + Message.GRAY + " ♦ Right Click"),
    ITEM_LEAVE(Message.RED + Message.BOLD + "Leave" + Message.GRAY + " ♦ Right Click"),
    EDITOR_ITEM_SAVE(Message.GREEN + "Save Kit"),
    EDITOR_ITEM_LOAD(Message.AQUA + "Load Kit"),
    EDITOR_ITEM_RESET(Message.RED + "Reset Kit"),
    EDITOR_SAVE(Message.GREEN + "Saved successfully."),
    EDITOR_LOAD(Message.GREEN + "Loaded successfully."),
    EDITOR_RESET(Message.RED + "Reset successfully."),
    ANTI_SPAWNKILL(Message.RED + "You have to wait the end of Anti SpawnKill time."),
    PLAYER_DEATH(" was killed"),
    VOID_DEATH(" was void-killed by "),
    VOID_DEATH_ALONE(" fell into the void."),
    NO_LOCATION(Message.RED + "There are no location available."),
    NO_EDITOR(Message.RED + "There is no kit editor available."),
    NO_SPECTATOR(Message.RED + "There is no spectator location."),
    PLAYER_NULL(Message.RED + "This player is offline."),
    ARENA_JOINED(Message.GREEN + "You have joined the arena, good luck !"),
    STATS_OF(Message.AQUA + "Statistics of" + Message.GRAY + " » " + Message.WHITE),
    KILLS(Message.AQUA + "Kills" + Message.GRAY + " » " + Message.WHITE),
    DEATHS(Message.AQUA + "Deaths" + Message.GRAY + " » " + Message.WHITE),
    RATIO(Message.AQUA + "Ratio" + Message.GRAY + " » " + Message.WHITE),
    SB_TITLE(Message.DARK_AQUA + "FFARush"),
    SB_BAR(Message.GRAY + Message.STRIKE_THROUGH + "--------------------"),
    SB_PLAYERS(Message.AQUA + "Players" + Message.GRAY + " » " + Message.WHITE),
    SB_INFO(Message.GRAY + Message.ITALIC + "www.ffarush.eu");

    private final String text;

    Lang(final String text) {
        this.text = text;
    }
}
