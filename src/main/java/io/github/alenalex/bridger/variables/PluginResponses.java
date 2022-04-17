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

    public static class Commands{

        public static class CommandHelpLayout {
            public static final String HEADER = "<green>--------<aqua>Bridger<green>--------";
            public static final String HELP_COMMAND = "<red>%command% <white>  - <aqua>%description%";
            public static final String FOOTER = "<green>-------------------------------------";
        }

        public static class Sessions{
            public static final String NO_SESSION_NAME_PROVIDED = "<red>You have not provided a valid session name!";
            public static final String DOES_NOT_HAVE_AN_ACTIVE_SESSION = "<red>There isn't an active session on your behalf!";
            public static final String SUCCESSFULLY_SET_LOCATION = "<green>You have successfully set <gold>%key%<green>Y to your current location";
            public static final String SUCCESSFULLY_SET_VALUE = "<green>You have successfully set <gold>%key%<green>Y to <gold>%value%";
            public static final String NOT_PROVIDED_VALID_VALUE = "<red>The value provided is invalid!";
        }

    }

}
