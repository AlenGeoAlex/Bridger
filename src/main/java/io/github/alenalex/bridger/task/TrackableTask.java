package io.github.alenalex.bridger.task;


import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractThreadTask;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.models.player.BridgerUserMatchCache;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TrackableTask extends AbstractThreadTask {

    public TrackableTask(@NotNull Bridger plugin) {
        super(plugin, "GameTracker", 1);
    }

    @Override
    protected Runnable callableTask() {
        return new Runnable() {
            @Override
            public void run() {
                if(Bridger.isPluginReloading())
                    return;

                if(getPlugin().gameHandler().getActiveBridges().isEmpty())
                    return;

                for(UUID uuid : getPlugin().gameHandler().getActiveBridges().keySet()){
                    try {
                        final BridgerUserData bridgerUserData = getPlugin().gameHandler().userManager().of(uuid);
                        if(bridgerUserData == null)
                            continue;

                        if(bridgerUserData.userMatchCache().getStatus() != BridgerUserMatchCache.Status.PLAYING)
                            continue;

                        final Player player = bridgerUserData.getOptionalPlayer().orElse(null);

                        if (player == null || !player.isOnline())
                            continue;

                        final long currentSec = System.currentTimeMillis() - bridgerUserData.userMatchCache().getStartTime();

                        String hms = String.format("%02d :%02d :%02d",
                                TimeUnit.MILLISECONDS.toMinutes(currentSec) % TimeUnit.HOURS.toMinutes(1),
                                TimeUnit.MILLISECONDS.toSeconds(currentSec) % TimeUnit.MINUTES.toSeconds(1),
                                TimeUnit.MILLISECONDS.toMillis(currentSec) % TimeUnit.SECONDS.toMillis(1));

                        getPlugin().messagingUtils().sendActionBar(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ACTION_BAR, MessagePlaceholder.of("%time%", hms)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    protected void prepareStop() {

    }

}
