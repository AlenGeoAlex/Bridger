package io.github.alenalex.bridger;

import com.google.gson.Gson;
import io.github.alenalex.bridger.database.DataProvider;
import io.github.alenalex.bridger.exceptions.FailedLocaleLoading;
import io.github.alenalex.bridger.exceptions.IllegalInitializationException;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.handler.GameHandler;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.handler.WorkloadHandler;
import io.github.alenalex.bridger.listener.*;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.manager.HookManager;
import io.github.alenalex.bridger.manager.LocaleManager;
import io.github.alenalex.bridger.manager.SetupSessionManager;
import io.github.alenalex.bridger.task.ScoreboardTask;
import io.github.alenalex.bridger.task.TrackableTask;
import io.github.alenalex.bridger.utils.adventure.MessagingUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Bridger extends JavaPlugin {

    private static boolean PLUGIN_RELOADING = false;

    public static boolean isPluginReloading() {
        return PLUGIN_RELOADING;
    }

    private static final Random RANDOM;
    private static final Gson GSON;

    static {
        RANDOM = new Random(System.currentTimeMillis());
        GSON = new Gson();
    }

    public static Random randomInstance() {
        return RANDOM;
    }

    public static Gson gsonInstance() { return GSON; }

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
    private CommandManager commandManager;
    private SetupSessionManager setupSessionManager;
    private TrackableTask trackableTask;
    private ScoreboardTask scoreboardTask;

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
        this.commandManager = new CommandManager(this);
        this.setupSessionManager = new SetupSessionManager(this);
        this.trackableTask = new TrackableTask(this);
        this.scoreboardTask = new ScoreboardTask(this);

        if(!pluginHookManager.validateMinHookRequirements()) {
            getServer().getPluginManager().disablePlugin(this);
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

        if(!this.localeManager.initLocaleManager()){
            getLogger().severe("Unable to create a base locale file! This is necessary for the plugin to work!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

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
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCraftingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMiscListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        //Not needed to listen to command event, if the list is empty!
        if(!configurationHandler().getConfigurationFile().getCommandToBlock().isEmpty()){
            getServer().getPluginManager().registerEvents(new PlayerCommandListener(this), this);
        }

        this.pluginHookManager.registerHooks();

        this.commandManager.registerCompletions();
        this.commandManager.registerMessages();
        this.commandManager.registerCommands();

        this.trackableTask.setThreadCallPeriod(this.configurationHandler.getConfigurationFile().getActionBarUpdateTime());
        if(!this.trackableTask.startThread()) {
            getLogger().warning("Failed to initialize thread pool for Game monitor");
        }

        if(this.configurationHandler.getScoreboardConfiguration().isScoreboardEnabled()) {
            this.scoreboardTask.setThreadCallPeriod(this.configurationHandler.getScoreboardConfiguration().getScoreboardUpdateTime());
            if (!this.scoreboardTask.startThread()) {
                getLogger().warning("Failed to initialize thread pool for Scoreboards");
            }
        }
    }

    @Override
    public void onDisable() {
        if(dataProvider != null && dataProvider.getDatabaseProvider() != null && dataProvider.getDatabaseProvider().isConnectionOpen()) {
            dataProvider.getDatabaseProvider().saveAllUserSync(gameHandler.userManager().getModifiableValueList());
            dataProvider.closeConnection();
        }

        if(workloadHandler != null){
            workloadHandler.disableHandler();
        }

        if(this.trackableTask != null)
            this.trackableTask.stopThread();

        if(this.scoreboardTask != null)
            this.scoreboardTask.stopThread();
    }

    public void prepareReloadTask(){
        PLUGIN_RELOADING = true;
        this.configurationHandler.reloadHandler();
        if(!this.localeManager.reloadLocaleManager()){
            getLogger().severe("Failed to load locales!");
            getLogger().severe("The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
        }

        this.trackableTask.stopThread();
        this.trackableTask.setThreadCallPeriod(this.configurationHandler.getConfigurationFile().getActionBarUpdateTime());
        if(!this.trackableTask.startThread()) {
            getLogger().warning("Failed to initialize thread pool for Game monitor");
        }

        if(this.configurationHandler.getScoreboardConfiguration().isScoreboardEnabled()) {
            this.scoreboardTask.setThreadCallPeriod(this.configurationHandler.getScoreboardConfiguration().getScoreboardUpdateTime());
            if (!this.scoreboardTask.startThread()) {
                getLogger().warning("Failed to initialize thread pool for Game monitor");
            }
        }
        PLUGIN_RELOADING = false;
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

    public LocaleManager localeManager() {
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

    public SetupSessionManager setupSessionManager(){
        return setupSessionManager;
    }

    public TrackableTask trackableTask(){
        return trackableTask;
    }

    public ScoreboardTask scoreboardTask(){
        return scoreboardTask;
    }
}
