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
    }

    public static class Parameters{
        public static final List<String> CONFIG_RELOAD = Arrays.asList("config","locale","island");
        public static final List<String> ISLAND_SESSION_PERMISSION = Collections.singletonList("bridger.join.");
    }
}
