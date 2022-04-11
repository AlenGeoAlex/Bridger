package io.github.alenalex.bridger.utils.adventure;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class InternalPlaceholders {

    private static final boolean placeholderAPIEnabled;

    static {
        placeholderAPIEnabled = Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private final String placeholder;
    private final String toReplace;

    public InternalPlaceholders(String placeholder, String toReplace) {
        this.placeholder = placeholder;
        this.toReplace = toReplace;
    }

    public InternalPlaceholders(String placeholder, UUID toReplace){
        this.placeholder = placeholder;
        this.toReplace = toReplace.toString();
    }

    public InternalPlaceholders(String placeholder, Integer toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public InternalPlaceholders(String placeholder, Float toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public InternalPlaceholders(String placeholder, Long toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public InternalPlaceholders(String placeholder, Boolean toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public InternalPlaceholders(String placeholder, Double toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getToReplace() {
        return toReplace;
    }

    public String replacePlaceholders(String message){
        if(StringUtils.isBlank(message))
            return message;

        return message.replace(this.placeholder,this.toReplace);
    }

    public List<String> replacePlaceholders(@NotNull List<String> strings){
        if(strings.isEmpty())
            return strings;

        return strings.stream().map(this::replacePlaceholders).collect(java.util.stream.Collectors.toList());
    }

    public static String replacePlaceholders(@NotNull List<InternalPlaceholders> placeholders, String message){
        AtomicReference<String> retMessage = new AtomicReference<>(message);
        placeholders.forEach((placeholder -> {
            retMessage.set(placeholder.replacePlaceholders(message));
        }));
        return retMessage.get();
    }


}