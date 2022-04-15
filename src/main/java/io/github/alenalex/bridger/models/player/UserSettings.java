package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.configs.MessageConfiguration;
import org.jetbrains.annotations.NotNull;

public final class UserSettings {

    public static final UserSettings DEFAULT;

    static {
        DEFAULT = new UserSettings("en", null, null, true);
    }

    @NotNull private String language;
    private String material;
    private String particle;
    private boolean scoreboardEnabled;

    public UserSettings(@NotNull String language, String material, String particle, boolean scoreboardEnabled) {
        this.language = language;
        this.material = material;
        this.particle = particle;
        this.scoreboardEnabled = scoreboardEnabled;
    }

    public UserSettings(String material, String particle, boolean scoreboardEnabled) {
        this.language = "en";
        this.material = material;
        this.particle = particle;
        this.scoreboardEnabled = scoreboardEnabled;
    }

    public String getLanguageAsString() {
        return language;
    }

    public MessageConfiguration getLanguage() {
        return Bridger.instance().getLocaleManager().getOrDefault(language);
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

    public void setScoreboardEnabled(boolean scoreboardEnabled) {
        this.scoreboardEnabled = scoreboardEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettings that = (UserSettings) o;
        return scoreboardEnabled == that.scoreboardEnabled && Objects.equal(language, that.language) && Objects.equal(material, that.material) && Objects.equal(particle, that.particle);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(language, material, particle, scoreboardEnabled);
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "language='" + language + '\'' +
                ", material='" + material + '\'' +
                ", particle='" + particle + '\'' +
                ", scoreboardEnabled=" + scoreboardEnabled +
                '}';
    }
}
