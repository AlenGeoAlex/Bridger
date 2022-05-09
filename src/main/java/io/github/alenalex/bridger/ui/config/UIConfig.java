package io.github.alenalex.bridger.ui.config;

import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UIConfig {

    private static final Bridger plugin;

    static {
        plugin = Bridger.instance();
    }

    private String title;
    private int rows;

    private final List<UIFiller> fillers;

    private UIConfig(String title, int rows, List<UIFiller> fillers) {
        this.title = title;
        this.rows = rows;
        this.fillers = fillers;
    }

    private UIConfig(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.fillers = new ArrayList<>();
    }

    public UIConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public UIConfig setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public Component titleAsComponent(MessagePlaceholder... placeholders){
        return MessageFormatter.transform(title, placeholders);
    }

    public String titleAsString(MessagePlaceholder... placeholders){
        return MessagePlaceholder.replacePlaceholders(Arrays.asList(placeholders), title);
    }

    public List<UIFiller> getFillers() {
        return fillers;
    }

    public int rows(){
        return rows;
    }

    public static UIConfig buildFrom(@NotNull FlatFileSection section){
        String title = section.getString("title");
        int rows = section.getInt("rows");

        final List<UIFiller> fillers = new ArrayList<>();
        section.keySet("fillers").forEach(key -> {
            ItemStack stack = FlatFileUtils.deserializeItemStack(key);
            if(stack == null) {
                plugin.getLogger().warning("An unknown item was found in the config: " + key+", Skiping...");
                return;
            }

            fillers.add(new UIFiller(stack,
                    section.
                    getStringList("fillers." + key)
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())));
        });

        return new UIConfig(title, rows, fillers);
    }
}
