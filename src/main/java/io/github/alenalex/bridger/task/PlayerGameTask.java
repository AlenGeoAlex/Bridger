package io.github.alenalex.bridger.task;


import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractThreadTask;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerGameTask extends AbstractThreadTask {

    public PlayerGameTask(@NotNull Bridger plugin) {
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
                        final UserData userData = getPlugin().gameHandler().userManager().of(uuid);
                        if(userData == null)
                            continue;

                        if(userData.userMatchCache().getStatus() != UserMatchCache.Status.PLAYING)
                            continue;

                        final Player player = userData.getOptionalPlayer().orElse(null);

                        if (player == null || !player.isOnline())
                            continue;

                        final long currentSec = System.currentTimeMillis() - userData.userMatchCache().getStartTime();

                        if(userData.userSettings().isSetBackEnabled()){
                            if(currentSec >= TimeUnit.SECONDS.toMillis(userData.userSettings().getSetBack())){
                                getPlugin().gameHandler().playerFailedGame(player);
                                getPlugin().messagingUtils().sendActionBar(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_REACHED));
                                continue;
                            }

                        }

                        String hms = String.format("%02d :%02d :%02d",
                                TimeUnit.MILLISECONDS.toMinutes(currentSec) % TimeUnit.HOURS.toMinutes(1),
                                TimeUnit.MILLISECONDS.toSeconds(currentSec) % TimeUnit.MINUTES.toSeconds(1),
                                TimeUnit.MILLISECONDS.toMillis(currentSec) % TimeUnit.SECONDS.toMillis(1));

                        getPlugin().messagingUtils().sendActionBar(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ACTION_BAR, MessagePlaceholder.of("%time%", hms)));
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
