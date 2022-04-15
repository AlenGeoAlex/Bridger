package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.player.UserData;

import java.util.UUID;

public class UserManager extends AbstractRegistry<UUID, UserData> {

    public UserManager(Bridger plugin) {
        super(plugin);
    }

}
