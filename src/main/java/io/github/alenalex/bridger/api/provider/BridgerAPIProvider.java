package io.github.alenalex.bridger.api.provider;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.BridgerAPI;
import io.github.alenalex.bridger.exceptions.DuplicateAPIRequest;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BridgerAPIProvider {

    private final static List<JavaPlugin> OTHER_PLUGINS;

    static {
        OTHER_PLUGINS = new ArrayList<>();
    }

    public static BridgerAPIProvider provider(){
        return new BridgerAPIProvider();
    }

    public static int hookedPluginsCount(){
        return OTHER_PLUGINS.size();
    }

    public static List<JavaPlugin> getAllHookedPlugins(){
        return new ArrayList<>(OTHER_PLUGINS);
    }


    private BridgerAPIProvider() {

    }

    /**
     * Register your api on the plugin and get the api instance of Bridger
     * @param yourPlugin Your plugin instance
     * @return {@link BridgerAPI} instance
     * @throws DuplicateAPIRequest if your plugin is already registered
     * @throws IllegalStateException if the api isn't initialized
     * @throws IllegalArgumentException if the provided {@link JavaPlugin} is of Bridger's
     * @throws NullPointerException if the provided plugin is null
     */
    public BridgerAPI getApi(@NotNull JavaPlugin yourPlugin) throws NullPointerException, DuplicateAPIRequest, IllegalStateException, IllegalArgumentException {
        if(yourPlugin == null)
            throw new NullPointerException("The provided plugin is null");

        if(yourPlugin.equals(Bridger.instance()))
            throw new IllegalArgumentException("You have provided the bridger instance!");

        if(OTHER_PLUGINS.contains(yourPlugin))
            throw new DuplicateAPIRequest(yourPlugin.getName()+" is already registered!");

        OTHER_PLUGINS.add(yourPlugin);
        return Bridger.instance().getAPIInstance();
    }
}
