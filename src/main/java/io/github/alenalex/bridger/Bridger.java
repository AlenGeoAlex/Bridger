package io.github.alenalex.bridger;

import io.github.alenalex.bridger.database.DataProvider;
import io.github.alenalex.bridger.exceptions.FailedLocaleLoading;
import io.github.alenalex.bridger.exceptions.IllegalInitializationException;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.handler.GameHandler;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.handler.WorkloadHandler;
import io.github.alenalex.bridger.listener.ConnectionListener;
import io.github.alenalex.bridger.manager.HookManager;
import io.github.alenalex.bridger.manager.LocaleManager;
import io.github.alenalex.bridger.utils.adventure.MessagingUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Bridger extends JavaPlugin {

    private static final Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    public static Random randomInstance() {
        return random;
    }

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
    private GameHandler gameHandler;
    private UIHandler uiHandler;
    private HookManager pluginHookManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configurationHandler = new ConfigurationHandler(this);
        this.workloadHandler = new WorkloadHandler(this);
        this.messagingUtils = new MessagingUtils(this);
        this.dataProvider = new DataProvider(this);
        this.localeManager = new LocaleManager(this);
        this.gameHandler = new GameHandler(this);
        this.uiHandler = new UIHandler(this);
        this.pluginHookManager = new HookManager(this);

        if(!pluginHookManager.validateMinHookRequirements()) {
            getServer().getPluginManager().isPluginEnabled(this);
            return;
        }

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

        this.gameHandler.islandManager().loadAllIslands();

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

        //Load UI's after loading Locales
        //Actually it won't matter, as we will be loading the static ui's with Hardcoded values and dynamic gui's would fetch the locale from the locale manager
        if(!this.uiHandler.initHandler()){
            getLogger().severe("Failed to load UI's!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //Register all the plugin listener
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
    }

    @Override
    public void onDisable() {
        if(dataProvider != null && dataProvider.getDatabaseProvider().isConnectionOpen()) {
            dataProvider.getDatabaseProvider().saveAllUserSync(gameHandler.userManager().getModifiableValueList());
            dataProvider.closeConnection();
        }

        if(workloadHandler != null){
            workloadHandler.disableHandler();
        }
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

    public LocaleManager localManager() {
        return localeManager;
    }

    public GameHandler gameHandler(){
        return gameHandler;
    }

    public UIHandler uiHandler(){
        return uiHandler;
    }

    public HookManager pluginHookManager(){
        return pluginHookManager;
    }
}
