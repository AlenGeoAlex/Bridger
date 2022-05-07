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

    private UIConfig playerSettingConfig;
    private UIItem playerSelectingMaterial, playerSelectingFirework, playerSelectingParticle, playerSelectingClose, playerSelectingSetBack;

    private UIConfig materialSelectorConfig;
    private UIItem materialSelectorNext, materialSelectorPre;

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

        this.playerSettingConfig = UIConfig.buildFrom(getSectionOf("player.settings"));
        this.playerSelectingMaterial = UIItem.buildFrom(getSectionOf("player.settings.buttons.materials-selector"));
        this.playerSelectingParticle = UIItem.buildFrom(getSectionOf("player.settings.buttons.particles-selector"));
        this.playerSelectingFirework = UIItem.buildFrom(getSectionOf("player.settings.buttons.firework-selector"));
        this.playerSelectingSetBack = UIItem.buildFrom(getSectionOf("player.settings.buttons.setback-selector"));
        this.playerSelectingClose = UIItem.buildFrom(getSectionOf("player.settings.buttons.close-button"));

        this.materialSelectorConfig = UIConfig.buildFrom(getSectionOf("selector.material"));
        this.materialSelectorNext = UIItem.buildFrom(getSectionOf("selector.material.buttons.next-button"));
        this.materialSelectorPre = UIItem.buildFrom(getSectionOf("selector.material.buttons.pre-button"));
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

    public UIConfig getPlayerSettingConfig() {
        return playerSettingConfig;
    }

    public UIItem getPlayerSelectingMaterial() {
        return playerSelectingMaterial;
    }

    public UIItem getPlayerSelectingFirework() {
        return playerSelectingFirework;
    }

    public UIItem getPlayerSelectingParticle() {
        return playerSelectingParticle;
    }

    public UIItem getPlayerSelectingClose() {
        return playerSelectingClose;
    }

    public UIItem getPlayerSelectingSetBack() {
        return playerSelectingSetBack;
    }

    public UIConfig getMaterialSelectorConfig() {
        return materialSelectorConfig;
    }

    public UIItem getMaterialSelectorNext() {
        return materialSelectorNext;
    }

    public UIItem getMaterialSelectorPre() {
        return materialSelectorPre;
    }
}
