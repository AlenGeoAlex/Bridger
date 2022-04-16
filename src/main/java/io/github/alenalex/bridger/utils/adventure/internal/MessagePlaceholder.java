package io.github.alenalex.bridger.utils.adventure.internal;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class MessagePlaceholder {

    public static MessagePlaceholder of(@NotNull String placeholder, String toReplace){
        return new MessagePlaceholder(placeholder, toReplace);
    }

    private final String placeholder;
    private final String toReplace;

    public MessagePlaceholder(String placeholder, String toReplace) {
        this.placeholder = placeholder;
        this.toReplace = toReplace;
    }

    public MessagePlaceholder(String placeholder, UUID toReplace){
        this.placeholder = placeholder;
        this.toReplace = toReplace.toString();
    }

    public MessagePlaceholder(String placeholder, Integer toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public MessagePlaceholder(String placeholder, Float toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public MessagePlaceholder(String placeholder, Long toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public MessagePlaceholder(String placeholder, Boolean toReplace) {
        this.placeholder = placeholder;
        this.toReplace = String.valueOf(toReplace);
    }

    public MessagePlaceholder(String placeholder, Double toReplace) {
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

    public static String replacePlaceholders(@NotNull List<MessagePlaceholder> placeholders, String message){
        AtomicReference<String> retMessage = new AtomicReference<>(message);
        placeholders.forEach((placeholder -> {
            retMessage.set(placeholder.replacePlaceholders(message));
        }));
        return retMessage.get();
    }

    public static List<String> replacePlaceholders(@NotNull List<MessagePlaceholder> placeholders, List<String> message){
        return message.stream().map(eachMessage -> replacePlaceholders(placeholders, eachMessage)).collect(java.util.stream.Collectors.toList());
    }


}