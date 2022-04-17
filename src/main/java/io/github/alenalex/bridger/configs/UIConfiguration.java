package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.gui.config.UIConfig;
import io.github.alenalex.bridger.gui.config.UIItem;
import io.github.alenalex.bridger.handler.ConfigurationHandler;

public class UIConfiguration extends AbstractFileSettings {

    private UIConfig fireWorkShopConfig;
    private UIItem fireWorkShopItem;

    private UIConfig materialShopConfig;
    private UIItem materialShopItem;

    public UIConfiguration(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.fireWorkShopConfig = UIConfig.buildFrom(getSectionOf("shop.fireworks"));
        this.fireWorkShopItem = UIItem.buildFrom(getSectionOf("shop.fireworks.buttons.item-button"));

        this.materialShopConfig = UIConfig.buildFrom(getSectionOf("shop.materials"));
        this.materialShopItem = UIItem.buildFrom(getSectionOf("shop.materials.buttons.item-button"));
    }

    @Override
    public void prepareReload() {

    }

    public UIConfig getFireWorkShopConfig() {
        return fireWorkShopConfig;
    }

    public UIItem getFireWorkShopItem() {
        return fireWorkShopItem;
    }

    public UIConfig getMaterialShopConfig() {
        return materialShopConfig;
    }

    public UIItem getMaterialShopItem() {
        return materialShopItem;
    }
}
