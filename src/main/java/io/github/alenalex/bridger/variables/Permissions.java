package io.github.alenalex.bridger.variables;

public class Permissions {

    public static class Commands{

        public static class Sessions{
            public static final String DEFAULT = "bridger.sessions";
            public static final String CREATE = "bridger.sessions.create";
        }
    }

    public static class Admin{
        public static final String ADMIN = "bridger.admin";
        public static final String ADMIN_BUILD = "bridger.admin.build";
    }

    public static class Spectator{
        public static final String BLOCK_PLACE = "bridger.spectator.placeblocks";
    }

}
