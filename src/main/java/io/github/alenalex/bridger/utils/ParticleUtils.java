package io.github.alenalex.bridger.utils;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleEffect;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ParticleUtils {

    private static final Map<String, ParticleEffect> EFFECT_MAP;

    static {
        EFFECT_MAP = new HashMap<>();
    }

    public static void registerParticle(@NotNull ParticleEffect particleEffect){
        EFFECT_MAP.put(particleEffect.getFieldName(), particleEffect);
    }

    public static void clearParticleMap(){
        EFFECT_MAP.clear();
    }

    public static Optional<ParticleEffect> getParticleByName(@NotNull String particleName){

        if(EFFECT_MAP.containsKey(particleName))
            return Optional.of(EFFECT_MAP.get(particleName));
        else return Optional.empty();
    }



}
