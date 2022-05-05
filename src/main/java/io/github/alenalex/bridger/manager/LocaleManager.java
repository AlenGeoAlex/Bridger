package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.configs.MessageConfiguration;
import io.github.alenalex.bridger.exceptions.FailedLocaleLoading;

import java.io.File;
import java.util.Objects;

public class LocaleManager extends AbstractRegistry<String, MessageConfiguration> {

   final File langFolder;

    public LocaleManager(Bridger plugin) {
        super(plugin);
        this.langFolder = new File(plugin.getDataFolder(), "lang");
    }

    public boolean initLocaleManager() {
        try {
            if(!this.langFolder.exists())
                this.langFolder.mkdirs();

            File langFile = new File(this.langFolder, "en.yml");
            if(!langFile.exists()) {
                plugin.saveResource("lang/en.yml", false);
            }
            setDefaultKey("en");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void loadLocales() throws FailedLocaleLoading {
        if(this.langFolder.listFiles() == null)
            throw new FailedLocaleLoading("Failed to load locales. There is no files to load, The plugin should atleast have the default locale.");

        for (File file : Objects.requireNonNull(this.langFolder.listFiles())) {
            if(file.getName().endsWith(".yml")) {
                String lang = file.getName().replace(".yml", "");
                try {
                    final MessageConfiguration messageConfiguration = new MessageConfiguration(plugin, file);
                    this.register(lang, messageConfiguration);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        plugin.getLogger().info("Successfully loaded " + this.sizeOf() + " locales.");
    }

    public Bridger getPlugin() {
        return plugin;
    }

    public boolean reloadLocaleManager() {
        flushRegistry();
        initLocaleManager();
        try {
            loadLocales();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
