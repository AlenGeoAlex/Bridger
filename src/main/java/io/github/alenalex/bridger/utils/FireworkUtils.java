package io.github.alenalex.bridger.utils;

import io.github.alenalex.bridger.Bridger;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class FireworkUtils {

    private static final List<Color> COLOR_LIST = new ArrayList<Color>(){{
        add(org.bukkit.Color.BLUE);
        add(org.bukkit.Color.LIME);
        add(org.bukkit.Color.OLIVE);
        add(org.bukkit.Color.WHITE);
        add(org.bukkit.Color.ORANGE);
        add(org.bukkit.Color.PURPLE);
        add(org.bukkit.Color.AQUA);
        add(org.bukkit.Color.BLACK);
        add(org.bukkit.Color.FUCHSIA);
        add(org.bukkit.Color.GRAY);
        add(org.bukkit.Color.GREEN);
        add(org.bukkit.Color.MAROON);
        add(org.bukkit.Color.NAVY);
        add(org.bukkit.Color.RED);
        add(org.bukkit.Color.SILVER);
        add(org.bukkit.Color.TEAL);
        add(org.bukkit.Color.YELLOW);
    }};

    public static Optional<FireworkEffect.Type> getFireworkTypeByName(String name) {
        if(EnumUtils.isValidEnum(FireworkEffect.Type.class, name)) {
            final FireworkEffect.Type type = EnumUtils.getEnum(FireworkEffect.Type.class, name);
            if(Bridger.instance().configurationHandler().getConfigurationFile().getEnabledFireWorkModels().containsKey(type)) {
                return Optional.of(type);
            }else return Optional.empty();
        }else return Optional.empty();
    }

    public static Color getRandomColor() {
        return COLOR_LIST.get(ThreadLocalRandom.current().nextInt(COLOR_LIST.size()));
    }

}
