package io.github.alenalex.bridger.variables;

public final class PluginResponses {

    public static class SetupSession {
        public static final String SESSION_WITH_NAME_EXISTS = "<red>A session with this name is currently on setup queue, Please try another name!";
        public static final String USER_ALREADY_HAS_AN_ACTIVE_SESSION = "<red>Sorry, you already have an active setup session. You can't create a new one!";
        public static final String CREATED_SETUP_SESSION = "<green>A new setup session with name %session-name% has been created successfully!, You can now start editing!";

        public static final String USER_DOES_NOT_HAVE_AN_ACTIVE_SESSION = "<red>There isn't an active session on your behalf!";
        public static final String PURGED_SESSION = "<green>Deleted your old session - %session-name%";
        public static final String PURGE_CONFIRM = "<gold>Rerun the command once again to remove your current setup session";
    }

}
