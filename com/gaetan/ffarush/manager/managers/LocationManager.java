package com.gaetan.ffarush.manager.managers;

import com.gaetan.api.serializer.Serialize;
import com.gaetan.ffarush.manager.Manager;
import com.gaetan.ffarush.manager.ManagerHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public final class LocationManager extends Manager {
    int location;
    Location lobbyLocation, kitEditorLocation, spectatorLocation;
    Map<String, Location> locationMap = new HashMap<>();

    /**
     * Constructor for the LocationManager.
     *
     * @param handler reference to the ManagerHandler class
     */
    public LocationManager(final ManagerHandler handler) {
        super(handler);

        this.location = 0;
        this.lobbyLocation = this.handler.getFfaRushPlugin().getConfig().getString("lobby") == null ? null : Serialize.deserializeLocation(this.handler.getFfaRushPlugin().getConfig().getString("lobby"));
        this.kitEditorLocation = this.handler.getFfaRushPlugin().getConfig().getString("kiteditor") == null ? null : Serialize.deserializeLocation(this.handler.getFfaRushPlugin().getConfig().getString("kiteditor"));
        this.spectatorLocation = this.handler.getFfaRushPlugin().getConfig().getString("spectator") == null ? null : Serialize.deserializeLocation(this.handler.getFfaRushPlugin().getConfig().getString("spectator"));

        this.loadLocation();
    }

    /**
     * Set the lobby location.
     *
     * @param location new location
     */
    public void setLobbyLocation(final Location location) {
        this.lobbyLocation = location;

        this.handler.getFfaRushPlugin().getConfig().set("lobby", Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    /**
     * Set the kit editor location.
     *
     * @param location new location
     */
    public void setKitEditorLocation(final Location location) {
        this.kitEditorLocation = location;

        this.handler.getFfaRushPlugin().getConfig().set("kiteditor", Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    /**
     * Set the spectator location.
     *
     * @param location new location
     */
    public void setSpectatorLocation(final Location location) {
        this.spectatorLocation = location;

        this.handler.getFfaRushPlugin().getConfig().set("spectator", Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    /**
     * Add a location to the fight arena.
     *
     * @param location new location
     */
    public void addLocation(final Location location, final String count) {
        this.locationMap.put(count, location);
        this.handler.getFfaRushPlugin().getConfig().set("location." + count, Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    /**
     * Load and cache all fight arena location from the config.
     */
    private void loadLocation() {
        final FileConfiguration fileConfig = this.handler.getFfaRushPlugin().getConfig();
        final ConfigurationSection section = fileConfig.getConfigurationSection("location");
        if (section != null) {
            for (final String locationID : section.getKeys(false)) {
                final Location location = Serialize.deserializeLocation(section.getString(locationID));

                this.locationMap.put(locationID, location);
                this.location++;
            }
        }
    }

    /**
     * Get a location with his number.
     */
    public Location getLocation(final int count) {
        return this.locationMap.get(String.valueOf(count));
    }
}
