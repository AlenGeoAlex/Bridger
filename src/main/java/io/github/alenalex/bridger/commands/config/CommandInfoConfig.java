package io.github.alenalex.bridger.commands.config;

import de.leonhard.storage.sections.FlatFileSection;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandInfoConfig {

    private final String commandName;
    private final List<String> aliases;
    private final boolean enabled;

    private CommandInfoConfig(String commandName, List<String> aliases, boolean enabled) {
        this.commandName = commandName;
        this.aliases = aliases;
        this.enabled = enabled;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static CommandInfoConfig buildFrom(@NotNull FlatFileSection section){
        final boolean enabled = section.getBoolean("enabled");
        final String name = section.getString("name");
        final List<String> aliases = section.getStringList("aliases");

        if(enabled && StringUtils.isBlank(name))
            throw new IllegalArgumentException("You haven't provided a valid command name for "+section.getPathPrefix());

        return new CommandInfoConfig(name, aliases, enabled);
    }
}
