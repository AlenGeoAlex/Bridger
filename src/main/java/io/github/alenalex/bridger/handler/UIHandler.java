package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.ui.dynamic.IslandSelector;
import io.github.alenalex.bridger.ui.dynamic.cosmetics.MaterialSelector;
import io.github.alenalex.bridger.ui.dynamic.profile.PlayerSettings;
import io.github.alenalex.bridger.ui.dynamic.shop.FireworkShop;
import io.github.alenalex.bridger.ui.dynamic.shop.MaterialsShop;
import io.github.alenalex.bridger.ui.statics.CosmeticShop;
import io.github.alenalex.bridger.ui.statics.SetBackSelector;
import io.github.alenalex.bridger.interfaces.IHandler;

public final class UIHandler implements IHandler {

    private final Bridger plugin;

    private final FireworkShop fireworkShop;
    private final CosmeticShop cosmeticShop;
    private final MaterialsShop materialsShop;

    private final IslandSelector islandSelector;

    private final PlayerSettings playerSettings;
    private final SetBackSelector setBackSelector;

    private final MaterialSelector materialSelector;

    public UIHandler(Bridger plugin) {
        this.plugin = plugin;
        this.cosmeticShop = new CosmeticShop(this);
        this.fireworkShop = new FireworkShop(this);
        this.materialsShop = new MaterialsShop(this);
        this.islandSelector = new IslandSelector(this);
        this.playerSettings = new PlayerSettings(this);
        this.setBackSelector = new SetBackSelector(this);
        this.materialSelector = new MaterialSelector(this);
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

    public FireworkShop getFireworkShop() {
        return fireworkShop;
    }

    public CosmeticShop getCosmeticShop() {
        return cosmeticShop;
    }

    public MaterialsShop getMaterialsShop() {
        return materialsShop;
    }

    public IslandSelector getIslandSelector() {
        return islandSelector;
    }

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }

    public SetBackSelector getSetBackSelector() {
        return setBackSelector;
    }

    public MaterialSelector getMaterialSelector() {
        return materialSelector;
    }
}
