package io.github.alenalex.bridger.utils;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.models.setup.SetupSession;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

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
                    FOLDER.mkdirs();

                final File debugFile = new File(FOLDER.getAbsolutePath(), fileName);
                if(debugFile.exists())
                    debugFile.delete();

                try {
                    FileUtils.write(debugFile, Bridger.gsonInstance().toJson(instance.getDescription().getVersion())+"\n");
                    FileUtils.write(debugFile, "hooks: \n"+Bridger.gsonInstance().toJson(instance.pluginHookManager().getAllHooks())+"\n");
                    FileUtils.write(debugFile, "players:\n");
                    for(BridgerUserData data : instance.gameHandler().userManager().getValueCollection()){
                        FileUtils.write(debugFile, data.asJson()+"\n");
                    }
                    FileUtils.write(debugFile, "islands:\n");
                    for(BridgerIsland bridgerIsland : instance.gameHandler().islandManager().getValueCollection()){
                        FileUtils.write(debugFile, bridgerIsland.asJson()+"\n");
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
