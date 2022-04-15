package io.github.alenalex.bridger;

import io.github.alenalex.bridger.database.DataProvider;
import io.github.alenalex.bridger.exceptions.FailedLocaleLoading;
import io.github.alenalex.bridger.exceptions.IllegalInitializationException;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.handler.WorkloadHandler;
import io.github.alenalex.bridger.manager.LocaleManager;
import io.github.alenalex.bridger.utils.adventure.MessagingUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bridger extends JavaPlugin {

    private static Bridger instance = null;

    public static Bridger instance() {
        if(instance == null)
            throw new IllegalInitializationException("Bridger is not initialized!");
        return instance;
    }

    private ConfigurationHandler configurationHandler;
    private WorkloadHandler workloadHandler;
    private DataProvider dataProvider;
    private LocaleManager localeManager;
    private MessagingUtils messagingUtils;

    @Override
    public void onEnable() {
        instance = this;
        this.configurationHandler = new ConfigurationHandler(this);
        this.workloadHandler = new WorkloadHandler(this);
        this.messagingUtils = new MessagingUtils(this);
        this.dataProvider = new DataProvider(this);
        this.localeManager = new LocaleManager(this);

        if(!this.configurationHandler.initHandler()){
            getLogger().severe("Failed to load configurations!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.configurationHandler.prepareHandler();

        if(!this.dataProvider.initConnection()){
            getLogger().severe("Failed to connect to database!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(!this.workloadHandler.initHandler()){
            getLogger().severe("Failed to load workload threads!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.workloadHandler.prepareHandler();

        if( !this.dataProvider.getDatabaseProvider().prepareDatabase() ){
            getLogger().severe("Failed to prepare database!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.localeManager.initLocaleManager();
        try {
            this.localeManager.loadLocales();
        } catch (FailedLocaleLoading e) {
            e.printStackTrace();
            getLogger().severe("Failed to load locales!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


    }

    @Override
    public void onDisable() {

    }

    public ConfigurationHandler configurationHandler() {
        return configurationHandler;
    }

    public WorkloadHandler workloadHandler() {
        return workloadHandler;
    }

    public MessagingUtils messagingUtils() {
        return messagingUtils;
    }

    public DataProvider dataProvider() {
        return dataProvider;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }
}
