package io.github.alenalex.bridger.variables;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandCompletions {

    public static class Keys{
        public static final String JOIN_PERMS = "joinPerms";
        public static final String ACTIVE_PLAYERS = "activePlayers";
        public static final String CONFIG_RELOAD = "configReload";
        public static final String ALL_PLAYERS = "allPlayers";
        public static final String ENABLED_LOCALE = "enabledLocale";
        public static final String PLAYERS = "players";
        public static final String VALUES_1_10 = "values10";
        public static final String VALUES_SETBACK = "valuesSetBack";
    }

    public static class Parameters{
        public static final List<String> CONFIG_RELOAD = Arrays.asList("config","locale","island", "scoreboard");
        public static final List<String> ISLAND_SESSION_PERMISSION = Collections.singletonList("bridger.join.");
        public static final List<String> VALUES_1_10 = Arrays.asList("1", "2","3", "4","5","6","7","8","9","10");
        public static final List<String> VALUES_SETBACK = Arrays.asList("1","5","10","30","60");
    }
}
