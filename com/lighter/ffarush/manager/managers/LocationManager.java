package com.lighter.ffarush.manager.managers;

import com.gaetan.api.serializer.Serialize;
import com.lighter.ffarush.manager.Manager;
import com.lighter.ffarush.manager.ManagerHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Setter
public final class LocationManager extends Manager {
    int location;
    Location lobbyLocation, kitEditorLocation;
    Map<String, Location> locationMap = new HashMap<>();

    public LocationManager(final ManagerHandler handler) {
        super(handler);

        this.location = 0;
        this.lobbyLocation = this.handler.getFfaRushPlugin().getConfig().getString("lobby") == null ? null : Serialize.deserializeLocation(this.handler.getFfaRushPlugin().getConfig().getString("lobby"));
        this.kitEditorLocation = this.handler.getFfaRushPlugin().getConfig().getString("kiteditor") == null ? null : Serialize.deserializeLocation(this.handler.getFfaRushPlugin().getConfig().getString("kiteditor"));

        this.loadLocation();
    }

    public void setLobbyLocation(final Location location) {
        this.lobbyLocation = location;

        this.handler.getFfaRushPlugin().getConfig().set("lobby", Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    public void setKitEditorLocation(final Location location) {
        this.kitEditorLocation = location;

        this.handler.getFfaRushPlugin().getConfig().set("kiteditor", Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

    public void addLocation(final Location location, final String count) {
        this.locationMap.put(count, location);
        this.handler.getFfaRushPlugin().getConfig().set("location." + count, Serialize.serializeLocation(location));
        this.handler.getFfaRushPlugin().saveConfig();
    }

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

    public Location getLocation(final int count) {
        return this.locationMap.get(String.valueOf(count));
    }
}
