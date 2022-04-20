package io.github.alenalex.bridger.variables;

public class Permissions {

    public static class Commands{

        public static class Sessions{
            public static final String DEFAULT = "bridger.command.sessions";
            public static final String CREATE = "bridger.command.sessions.create";
        }

        public static class Island{
            public static final String GUI = "bridger.command.island.gui";
            public static final String BY_NAME = "bridger.command.island.byname";
        }

        public static class Debug{
            public static final String CREATE = "bridger.command.debug.create";
            public static final String CLEAR = "bridger.command.debug.clear";
        }
    }

    public static class Admin{
        public static final String ADMIN = "bridger.admin";
        public static final String ADMIN_BUILD = "bridger.admin.build";
        public static final String ADMIN_CRAFTING = "bridger.admin.crafting";
        public static final String ADMIN_DROP_ITEM = "bridger.admin.dropitem";
        public static final String ADMIN_PICKUP_ITEM = "bridger.admin.pickupitem";
    }

    public static class Spectator{
        public static final String BLOCK_PLACE = "bridger.spectator.placeblocks";
    }



}
