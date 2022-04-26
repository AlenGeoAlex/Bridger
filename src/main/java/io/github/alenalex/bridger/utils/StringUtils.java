package io.github.alenalex.bridger.utils;

import java.util.concurrent.TimeUnit;

public class StringUtils {

    public static String convertLongToReadableDate(long value) {
        return String.format("%02d :%02d :%02d",
                TimeUnit.MILLISECONDS.toMinutes(value) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(value) % TimeUnit.MINUTES.toSeconds(1),
                TimeUnit.MILLISECONDS.toMillis(value) % TimeUnit.SECONDS.toMillis(1));
    }

}
