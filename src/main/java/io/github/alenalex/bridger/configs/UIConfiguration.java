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

    private UIConfig islandSelectorConfig;
    private UIItem islandSelectorItem, islandSelectorSpectateItem, islandSelectorNextPage, islandSelectorPreviousPage;

    public UIConfiguration(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.fireWorkShopConfig = UIConfig.buildFrom(getSectionOf("shop.fireworks"));
        this.fireWorkShopItem = UIItem.buildFrom(getSectionOf("shop.fireworks.buttons.item-button"));

        this.materialShopConfig = UIConfig.buildFrom(getSectionOf("shop.materials"));
        this.materialShopItem = UIItem.buildFrom(getSectionOf("shop.materials.buttons.item-button"));

        this.islandSelectorConfig = UIConfig.buildFrom(getSectionOf("island-selector"));
        this.islandSelectorItem = UIItem.buildFrom(getSectionOf("island-selector.buttons.island-button"));
        this.islandSelectorSpectateItem = UIItem.buildFrom(getSectionOf("island-selector.buttons.spec-button"));
        this.islandSelectorNextPage = UIItem.buildFrom(getSectionOf("island-selector.buttons.next-button"));
        this.islandSelectorPreviousPage = UIItem.buildFrom(getSectionOf("island-selector.buttons.pre-button"));
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

    public UIConfig getIslandSelectorConfig() {
        return islandSelectorConfig;
    }

    public UIItem getIslandSelectorItem() {
        return islandSelectorItem;
    }

    public UIItem getIslandSelectorSpectateItem() {
        return islandSelectorSpectateItem;
    }

    public UIItem getIslandSelectorNextPage() {
        return islandSelectorNextPage;
    }

    public UIItem getIslandSelectorPreviousPage() {
        return islandSelectorPreviousPage;
    }
}
