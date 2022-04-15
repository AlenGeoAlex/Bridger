package io.github.alenalex.bridger.models.player;

import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UserCosmetics {

    public static final UserCosmetics DEFAULT;

    static {
        DEFAULT = new UserCosmetics(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private final List<String> fireWorkUnlocked;
    private final List<String> materialUnlocked;
    private final List<String> particleUnlocked;

    private boolean unlockAll;

    public UserCosmetics(List<String> fireWorkUnlocked, List<String> materialUnlocked, List<String> particleUnlocked) {
        this.fireWorkUnlocked = fireWorkUnlocked;
        this.materialUnlocked = materialUnlocked;
        this.particleUnlocked = particleUnlocked;
        this.unlockAll = false;
    }

    public boolean isFireworkUnlocked(@NotNull String fireworkName){
        if(unlockAll)
            return true;
        return fireWorkUnlocked.contains(fireworkName);
    }

    public boolean isMaterialUnlocked(@NotNull String materialName){
        if(unlockAll)
            return true;
        return materialUnlocked.contains(materialName);
    }

    public boolean isParticleUnlocked(@NotNull String particleName){
        if(unlockAll)
            return true;
        return particleUnlocked.contains(particleName);
    }

    public void resetFireworks(){
        fireWorkUnlocked.clear();
    }

    public void resetMaterials(){
        materialUnlocked.clear();
    }

    public void resetParticles(){
        particleUnlocked.clear();
    }

    public void unlockMaterial(@NotNull String name){
        materialUnlocked.add(name);
    }

    public void unlockParticle(@NotNull String particleName){
        particleUnlocked.add(particleName);
    }

    public void unlockFirework(@NotNull String fireWorkName){
        fireWorkUnlocked.add(fireWorkName);
    }

    public void resetPlayerUnlocks(){
        resetFireworks();
        resetMaterials();
        resetParticles();
    }

    public void setUnlockAll(boolean unlockAll) {
        this.unlockAll = unlockAll;
    }

    public boolean isUnlockAllEnabled(){
        return unlockAll;
    }

    public static List<String> deserialize(@NotNull String serialized){
        return new ArrayList<>(Arrays.asList(serialized.split("/")));
    }

    public static String serialize(@NotNull List<String> list){
        if(list.isEmpty())
            return "";
        final StringJoiner stringJoiner = new StringJoiner("/");
        for(String s : list){
            stringJoiner.add(s);
        }
        return stringJoiner.toString();
    }

    public List<String> getFireWorkUnlocked() {
        return fireWorkUnlocked;
    }

    public List<String> getMaterialUnlocked() {
        return materialUnlocked;
    }

    public List<String> getParticleUnlocked() {
        return particleUnlocked;
    }
}
