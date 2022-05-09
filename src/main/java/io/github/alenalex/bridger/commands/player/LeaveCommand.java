package io.github.alenalex.bridger.commands.player;

import dev.triumphteam.cmd.core.annotation.Default;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class LeaveCommand extends AbstractCommand {
    public LeaveCommand(CommandManager manager) {
        super(manager, "leave");
    }

    @Override
    public Map<String, String> getCommandDescriptionMap() {
        return null;
    }

    @Default
    public void onLeaveCommand(@NotNull Player player){
        player.performCommand("island leave");
    }
}
