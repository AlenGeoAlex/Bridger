package io.github.alenalex.bridger.variables;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.util.ArrayList;
import java.util.List;

public final class Fireworks {

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

    private static final List<FireworkEffect.Type> FIREWORK_TYPE = new ArrayList<FireworkEffect.Type>(){{
        add(FireworkEffect.Type.BALL);
        add(FireworkEffect.Type.BURST);
        add(FireworkEffect.Type.STAR);
        add(FireworkEffect.Type.CREEPER);
        add(FireworkEffect.Type.BALL_LARGE);
    }};


}
