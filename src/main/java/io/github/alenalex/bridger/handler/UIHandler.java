package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.interfaces.IGui;
import io.github.alenalex.bridger.ui.dynamic.IslandSelector;
import io.github.alenalex.bridger.ui.dynamic.LeaderboardMenu;
import io.github.alenalex.bridger.ui.dynamic.cosmetics.MaterialSelector;
import io.github.alenalex.bridger.ui.dynamic.profile.PlayerSettings;
import io.github.alenalex.bridger.ui.dynamic.shop.FireworkShop;
import io.github.alenalex.bridger.ui.dynamic.shop.MaterialsShop;
import io.github.alenalex.bridger.ui.statics.CosmeticShop;
import io.github.alenalex.bridger.ui.statics.SetBackSelector;
import io.github.alenalex.bridger.interfaces.IHandler;

public final class UIHandler implements IHandler {

    private final Bridger plugin;

    private final IGui fireworkShop;
    private final CosmeticShop cosmeticShop;
    private final IGui materialsShop;

    private final IGui islandSelector;

    private final IGui playerSettings;
    private final SetBackSelector setBackSelector;

    private final IGui materialSelector;

    private final IGui leaderboardMenu;

    public UIHandler(Bridger plugin) {
        this.plugin = plugin;
        this.cosmeticShop = new CosmeticShop(this);
        this.fireworkShop = new FireworkShop(this);
        this.materialsShop = new MaterialsShop(this);
        this.islandSelector = new IslandSelector(this);
        this.playerSettings = new PlayerSettings(this);
        this.setBackSelector = new SetBackSelector(this);
        this.materialSelector = new MaterialSelector(this);
        this.leaderboardMenu = new LeaderboardMenu(this);
    }

    @Override
    public boolean initHandler() {
        return cosmeticShop.initGui() && setBackSelector.initGui();
    }

    @Override
    public void prepareHandler() {

    }

    @Override
    public Bridger plugin() {
        return plugin;
    }

    @Override
    public void disableHandler() {
        IHandler.super.disableHandler();
    }

    @Override
    public void reloadHandler() {
        initHandler();
    }

    public IGui getFireworkShop() {
        return fireworkShop;
    }

    public IGui getCosmeticShop() {
        return cosmeticShop;
    }

    public IGui getMaterialsShop() {
        return materialsShop;
    }

    public IGui getIslandSelector() {
        return islandSelector;
    }

    public IGui getPlayerSettings() {
        return playerSettings;
    }

    public IGui getSetBackSelector() {
        return setBackSelector;
    }

    public IGui getMaterialSelector() {
        return materialSelector;
    }

    public IGui getLeaderboardMenu() {
        return leaderboardMenu;
    }
}
