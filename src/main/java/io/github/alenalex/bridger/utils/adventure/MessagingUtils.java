package io.github.alenalex.bridger.utils.adventure;

import io.github.alenalex.bridger.Bridger;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;


public final class MessagingUtils {

    private final Bridger plugin;
    private final BukkitAudiences audiences;

    public MessagingUtils(Bridger plugin) {
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create(plugin);
    }

    public void sendTo(@NotNull final Player player, String message){
        audiences.player(player).sendMessage(MessageFormatter.transform(message));
    }

    public void sendTo(@NotNull final CommandSender sender, String message){
        audiences.sender(sender).sendMessage(MessageFormatter.transform(message));
    }

    public void sendTo(@NotNull final CommandSender sender, Component message){
        audiences.sender(sender).sendMessage(message);
    }

    public void sendTo(@NotNull final CommandSender sender, List<String> message){
        message.forEach((eachLine) -> {
            sendTo(sender,MessageFormatter.transform(eachLine));
        });
    }

    public void sendTo(@NotNull final Player player, String message, InternalPlaceholders... placeholders){
        audiences.player(player).sendMessage(MessageFormatter.transform(message,placeholders));
    }

    public void sendInInterval(@NotNull final Player player, List<String> message, int interval, InternalPlaceholders... placeholders){
        message.forEach(m->{
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if(player == null)
                        return;

                    sendTo(player,m,placeholders);
                }
            },interval);
        });
    }

    public void sendTo(@NotNull final Player player, List<String> messages){
        messages.forEach(m -> {
            this.sendTo(player,m);
        });
    }

    public void sendTo(@NotNull final Player player, List<String> messages, InternalPlaceholders... placeholders){
        messages.forEach(m -> {
            this.sendTo(player,m,placeholders);
        });
    }

    public void sendToAsComponent(@NotNull final Player player, Component message){
        audiences.player(player).sendMessage(message);
    }

    public void sendToAsComponent(@NotNull final Player player, List<Component> message){
        message.forEach(m -> {
            sendToAsComponent(player,m);
        });
    }

    public void sendActionBar(@NotNull Player player, String message){
        audiences.player(player).sendActionBar(MessageFormatter.transform(message));
    }

    public void sendActionBar(@NotNull List<Player> players, String message){
        players.forEach((p) -> sendActionBar(p,message));
    }

    public void setBossBar(@NotNull Player player,@NotNull String barString, float percent , BossBar.Color color, BossBar.Overlay style, int duration){
        final BossBar bossBar = BossBar.bossBar(MessageFormatter.transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                audiences.player(player).hideBossBar(bossBar);
            }
        }, 20L *duration);
    }

    public  BossBar setBossBar(@NotNull Player player,@NotNull String barString,float percent ,BossBar.Color color, BossBar.Overlay style){
        final BossBar bossBar = BossBar.bossBar(MessageFormatter.transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        return bossBar;
    }

    public void sendTitle(@NotNull Player player, String header, String footer ){
        final Title title = Title.title(
                MessageFormatter.transform(header)
                ,MessageFormatter.transform(footer)
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header, String footer, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                MessageFormatter.transform(header)
                ,MessageFormatter.transform(footer)
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header){
        final Title title = Title.title(
                MessageFormatter.transform(header),
                Component.empty()
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                MessageFormatter.transform(header)
                ,Component.empty()
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public void broadcast(@NotNull final List<String> messages, int delay){
        if(messages.isEmpty())
            return;

        for(int i = 0; i<messages.size();i++){
            int finalI = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    audiences.all().sendMessage(MessageFormatter.transform(messages.get(finalI)));
                }
            },delay);
        }
    }

    public void broadcast(String message){
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                audiences.all().sendMessage(MessageFormatter.transform(message));
            }
        });
    }

}