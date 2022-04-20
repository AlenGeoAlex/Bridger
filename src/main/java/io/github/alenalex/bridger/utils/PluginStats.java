package io.github.alenalex.bridger.utils;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.setup.SetupSession;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PluginStats {

    public static final File FOLDER;

    static {
        FOLDER = new File(Bridger.instance().getDataFolder().getAbsolutePath()+File.separator+"debug");
    }


    private final Bridger instance;

    public PluginStats(Bridger instance) {
        this.instance = instance;
    }

    public void writeAsync(@NotNull String fileName){
        instance.getServer().getScheduler().runTaskAsynchronously(instance, new Runnable() {
            @Override
            public void run() {
                if(!FOLDER.exists())
                    FOLDER.exists();

                final File debugFile = new File(fileName, FOLDER.getAbsolutePath());
                if(debugFile.exists())
                    debugFile.delete();

                try {
                    FileUtils.write(debugFile, "version: "+instance.getDescription().getVersion()+"\n");
                    FileUtils.write(debugFile, "hooks: "+Bridger.gsonInstance().toJson(instance.pluginHookManager().getAllHooks())+"\n");
                    FileUtils.write(debugFile, "players:\n");
                    for(UserData data : instance.gameHandler().userManager().getValueCollection()){
                        FileUtils.write(debugFile, data.asJson()+"\n");
                    }
                    FileUtils.write(debugFile, "islands:\n");
                    for(Island island : instance.gameHandler().islandManager().getValueCollection()){
                        FileUtils.write(debugFile, island.asJson()+"\n");
                    }
                    FileUtils.write(debugFile, "sessions:\n");
                    for(SetupSession setupSession : instance.setupSessionManager().getValueCollection()){
                        FileUtils.write(debugFile, setupSession.asJson()+"\n");
                    }
                    FileUtils.write(debugFile, "active-islands: "+instance.gameHandler().asJson());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
