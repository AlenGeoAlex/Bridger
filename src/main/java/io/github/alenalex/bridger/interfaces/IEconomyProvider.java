package io.github.alenalex.bridger.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IEconomyProvider {

    boolean hasBalance(@NotNull Player player, double amount);

    double getBalance(@NotNull Player player);

    void withdraw(@NotNull Player player, double amount);

    void deposit(@NotNull Player player, double amount);
}
