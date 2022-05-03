package io.github.alenalex.bridger.task;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractThreadTask;
import io.github.alenalex.bridger.exceptions.IllegalThreadOperation;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardTask extends AbstractThreadTask {

    public ScoreboardTask(Bridger plugin) {
        super(plugin, "scoreboardTask", 1);
    }

    @Override
    protected Runnable callableTask() {
        return new Runnable() {
            @Override
            public void run() {
                if(Bridger.isPluginReloading())
                    return;

                if(getPlugin().gameHandler().userManager().isEmpty())
                    return;

                try {
                    for(UserData eachUser : getPlugin().gameHandler().userManager().getValueCollection()){
                        if(eachUser == null || eachUser.getPlayer() == null || (!eachUser.getPlayer().isOnline()))
                            continue;

                        if(!eachUser.userSettings().isScoreboardEnabled() || eachUser.userMatchCache().getScoreBoard() == null)
                            continue;


                        switch (eachUser.userMatchCache().getStatus()){
                            case PLAYING:
                            case IDLE:
                            {
                                if(getPlugin().pluginHookManager().isHookEnabled("PlaceholderAPI")) {
                                    final List<String> stringList = new ArrayList<>();
                                    for (String eachLine : getPlugin().configurationHandler().getScoreboardConfiguration().getMatchConfig().getLines()) {
                                        stringList.add(MessageFormatter.colorizeLegacy(PlaceholderAPI.setPlaceholders(eachUser.getPlayer(), eachLine)));
                                    }
                                    eachUser.userMatchCache().getScoreBoard().updateLines(stringList);
                                }else {
                                    eachUser.userMatchCache().getScoreBoard().updateLines(getPlugin().configurationHandler().getScoreboardConfiguration().getMatchConfig().getLines());
                                }
                            }
                            break;
                            case LOBBY:
                            {
                                if(getPlugin().pluginHookManager().isHookEnabled("PlaceholderAPI")) {
                                    final List<String> stringList = new ArrayList<>();
                                    for (String eachLine : getPlugin().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getLines()) {
                                        stringList.add(MessageFormatter.colorizeLegacy(PlaceholderAPI.setPlaceholders(eachUser.getPlayer(), eachLine)));
                                    }
                                    eachUser.userMatchCache().getScoreBoard().updateLines(stringList);
                                }else {
                                    eachUser.userMatchCache().getScoreBoard().updateLines(getPlugin().configurationHandler().getScoreboardConfiguration().getMatchConfig().getLines());
                                }
                            }
                            break;
                            case SPECTATING:
                            {
                                if(getPlugin().pluginHookManager().isHookEnabled("PlaceholderAPI")) {
                                    final List<String> stringList = new ArrayList<>();
                                    for (String eachLine : getPlugin().configurationHandler().getScoreboardConfiguration().getSpectateConfig().getLines()) {
                                        stringList.add(MessageFormatter.colorizeLegacy(PlaceholderAPI.setPlaceholders(eachUser.getPlayer(), eachLine)));
                                    }
                                    eachUser.userMatchCache().getScoreBoard().updateLines(stringList);
                                }else {
                                    eachUser.userMatchCache().getScoreBoard().updateLines(getPlugin().configurationHandler().getScoreboardConfiguration().getMatchConfig().getLines());
                                }
                            }
                            break;
                            default:
                        }
                    }
                }catch (Exception e){
                    throw new IllegalThreadOperation(e.getLocalizedMessage());
                }

            }
        };
    }

    @Override
    protected void prepareStop() {

    }
}
