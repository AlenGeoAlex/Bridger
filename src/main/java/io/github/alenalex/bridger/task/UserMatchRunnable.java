package io.github.alenalex.bridger.task;

import io.github.alenalex.bridger.Bridger;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class UserMatchRunnable extends BukkitRunnable {

    private static final Bridger instance;

    static {
        instance = Bridger.instance();
    }

    private final Player player;
    private final long startTimer;

    private UserMatchRunnable(Player player, long startTimer) {
        this.player = player;
        this.startTimer = startTimer;
    }

    @Override
    public void run() {
        if(player == null) {
            stop();
            return;
        }

        if(!player.isOnline()) {
            stop();
            return;
        }

        final long currentSec = System.currentTimeMillis() - startTimer;

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currentSec),
                TimeUnit.MILLISECONDS.toMinutes(currentSec) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(currentSec) % TimeUnit.MINUTES.toSeconds(1));

        Bridger.instance().messagingUtils().sendActionBar(player, "<aqua>"+ hms);
    }

    public void stop() {
        this.cancel();
    }

    public static UserMatchRunnable start(Player player, long startTimer) {
        if(player == null)
            return null;

        UserMatchRunnable userMatchRunnable = new UserMatchRunnable(player, startTimer);
        userMatchRunnable.runTaskTimerAsynchronously(instance, 0, 10);
        return userMatchRunnable;
    }
}
