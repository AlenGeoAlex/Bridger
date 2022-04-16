package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.configs.MessageConfiguration;
import io.github.alenalex.bridger.variables.Fireworks;
import org.bukkit.FireworkEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class UserSettings {

    public static final UserSettings DEFAULT;

    static {
        DEFAULT = new UserSettings("en", null, null, null ,true);
    }

    @NotNull private String language;
    private String material;
    private String particle;
    private String fireWork;
    private boolean scoreboardEnabled;
    private boolean setBackEnabled;

    public UserSettings(@NotNull String language, String material, String particle, String fireWork, boolean scoreboardEnabled) {
        this.language = language;
        this.material = material;
        this.particle = particle;
        this.fireWork = fireWork;
        this.scoreboardEnabled = scoreboardEnabled;
        this.setBackEnabled = false;
    }

    public UserSettings(String material, String particle, String fireWork, boolean scoreboardEnabled) {
        this.language = "en";
        this.material = material;
        this.particle = particle;
        this.scoreboardEnabled = scoreboardEnabled;
        this.fireWork = fireWork;
        this.setBackEnabled = false;
    }

    public String getLanguageAsString() {
        return language;
    }

    public MessageConfiguration getLanguage() {
        return Bridger.instance().localManager().getOrDefault(language);
    }

    public String getMaterialAsString() {
        return material;
    }

    public boolean hasMaterial() {
        return material != null;
    }

    public String getParticleAsString() {
        return particle;
    }

    public boolean hasFireWork() {
        return fireWork != null;
    }

    public String getFireWorkAsString() {
        return fireWork;
    }

    public Optional<FireworkEffect.Type> getFireWork() {
        return Fireworks.getFireworkTypeByName(fireWork);
    }

    public boolean hasParticle() {
        return particle != null;
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public void setFireWork(String fireWork) {
        this.fireWork = fireWork;
    }

    public void setScoreboardEnabled(boolean scoreboardEnabled) {
        this.scoreboardEnabled = scoreboardEnabled;
    }

    public boolean isSetBackEnabled() {
        return setBackEnabled;
    }

    public void setSetBackEnabled(boolean setBackEnabled) {
        this.setBackEnabled = setBackEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettings settings = (UserSettings) o;
        return scoreboardEnabled == settings.scoreboardEnabled && setBackEnabled == settings.setBackEnabled && Objects.equal(language, settings.language) && Objects.equal(material, settings.material) && Objects.equal(particle, settings.particle);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(language, material, particle, scoreboardEnabled, setBackEnabled);
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "language='" + language + '\'' +
                ", material='" + material + '\'' +
                ", particle='" + particle + '\'' +
                ", scoreboardEnabled=" + scoreboardEnabled +
                ", setBackEnabled=" + setBackEnabled +
                '}';
    }
}
