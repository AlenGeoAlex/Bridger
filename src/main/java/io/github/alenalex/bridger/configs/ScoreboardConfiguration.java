package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardConfiguration extends AbstractFileSettings {

    private long scoreboardUpdateTime;
    private boolean scoreboardEnabled;
    private ScoreboardConfig lobbyConfig, matchConfig, spectateConfig;

    public ScoreboardConfiguration(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.scoreboardUpdateTime = this.file.getLong("update-time");
        this.scoreboardUpdateTime = this.scoreboardUpdateTime <= 99 ? 100 : scoreboardUpdateTime;
        this.lobbyConfig = new ScoreboardConfig(
                MessageFormatter.colorizeLegacy(MessageFormatter.colorizeLegacy(this.file.getString("lobby-scoreboard.title"))),
                this.file.getStringList("lobby-scoreboard.lines")
        );

        this.matchConfig = new ScoreboardConfig(
                MessageFormatter.colorizeLegacy(MessageFormatter.colorizeLegacy(this.file.getString("match-scoreboard.title"))),
                this.file.getStringList("match-scoreboard.lines")
        );

        this.spectateConfig = new ScoreboardConfig(
                MessageFormatter.colorizeLegacy(MessageFormatter.colorizeLegacy(this.file.getString("spectate-scoreboard.title"))),
                this.file.getStringList("spectate-scoreboard.lines")
        );
        this.scoreboardEnabled = this.file.getBoolean("scoreboard-enabled");
    }

    @Override
    public void prepareReload() {
        this.lobbyConfig = null;
        this.spectateConfig = null;
        this.matchConfig = null;

    }



    public long getScoreboardUpdateTime() {
        return scoreboardUpdateTime;
    }

    public ScoreboardConfig getLobbyConfig() {
        return lobbyConfig;
    }

    public ScoreboardConfig getMatchConfig() {
        return matchConfig;
    }

    public ScoreboardConfig getSpectateConfig() {
        return spectateConfig;
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public class ScoreboardConfig{
        private final String translatedTitle;
        private final List<String> lines;

        public ScoreboardConfig(String translatedTitle, List<String> lines) {
            this.translatedTitle = translatedTitle;
            this.lines = lines.stream().map(MessageFormatter::colorizeLegacy).collect(Collectors.toList());
        }

        public ScoreboardConfig(String translatedTitle) {
            this.translatedTitle = translatedTitle;
            this.lines = new ArrayList<>();
        }

        public String getTranslatedTitle() {
            return translatedTitle;
        }

        public List<String> getLines() {
            return lines;
        }
    }
}
