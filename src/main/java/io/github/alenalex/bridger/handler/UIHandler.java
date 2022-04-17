package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.gui.dynamic.shop.FireworkShop;
import io.github.alenalex.bridger.gui.dynamic.shop.MaterialsShop;
import io.github.alenalex.bridger.gui.statics.CosmeticShop;
import io.github.alenalex.bridger.interfaces.IHandler;

public class UIHandler implements IHandler {

    private final Bridger plugin;

    private final FireworkShop fireworkShop;
    private final CosmeticShop cosmeticShop;
    private final MaterialsShop materialsShop;

    public UIHandler(Bridger plugin) {
        this.plugin = plugin;
        this.cosmeticShop = new CosmeticShop(this);
        this.fireworkShop = new FireworkShop(this);
        this.materialsShop = new MaterialsShop(this);
    }

    @Override
    public boolean initHandler() {
        return cosmeticShop.initGui();
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
        IHandler.super.reloadHandler();
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
}
