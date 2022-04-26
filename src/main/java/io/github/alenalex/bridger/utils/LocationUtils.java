package io.github.alenalex.bridger.utils;

import org.bukkit.Location;

public class LocationUtils {

    public static boolean isSameLocation(Location location1, Location location2) {
        return location1.getBlockX() == location2.getBlockX()
                && location1.getBlockY() == location2.getBlockY()
                && location1.getBlockZ() == location2.getBlockZ();
    }

}
