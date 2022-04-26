package io.github.alenalex.bridger.task;


import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TrackableTask  {

    private @NotNull final Bridger instance;
    private @NotNull final ScheduledExecutorService servicePool;

    public TrackableTask(@NotNull Bridger instance) {
        this.instance = instance;
        this.servicePool = new ScheduledThreadPoolExecutor(1);
    }

    public void startThread(){
        instance.getLogger().info("Starting player tracking on a new thread!");
        this.servicePool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                    if(instance.gameHandler().getActiveBridges().isEmpty())
                        return;

                    for(UUID uuid : instance.gameHandler().getActiveBridges().keySet()){
                            try {
                                final UserData userData = instance.gameHandler().userManager().of(uuid);
                                if(userData == null)
                                    continue;

                                if(userData.userMatchCache().getStatus() != UserMatchCache.Status.PLAYING)
                                    continue;

                                final Player player = userData.getPlayer().orElse(null);

                                if (player == null || !player.isOnline())
                                    continue;

                                final long currentSec = System.currentTimeMillis() - userData.userMatchCache().getStartTime();

                                String hms = String.format("%02d :%02d :%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(currentSec) % TimeUnit.HOURS.toMinutes(1),
                                        TimeUnit.MILLISECONDS.toSeconds(currentSec) % TimeUnit.MINUTES.toSeconds(1),
                                        TimeUnit.MILLISECONDS.toMillis(currentSec) % TimeUnit.SECONDS.toMillis(1));

                                instance.messagingUtils().sendActionBar(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ACTION_BAR, MessagePlaceholder.of("%time%", hms)));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                    }
            }
        },0, instance.configurationHandler().getConfigurationFile().getActionBarUpdateTime(), TimeUnit.MILLISECONDS);
    }

    public void stopThread(){
        this.servicePool.shutdown();
        instance.getLogger().info("Shutting down TrackableTask Thread!");
    }

}
