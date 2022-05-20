package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.ui.config.UIConfig;
import io.github.alenalex.bridger.ui.config.UIItem;
import io.github.alenalex.bridger.handler.ConfigurationHandler;

import java.util.ArrayList;
import java.util.List;

public final class UIConfiguration extends AbstractFileSettings {

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

    private UIConfig particleShopConfig;
    private UIItem particleShopItem;

    private UIConfig leaderboardMenuConfig;
    private final List<UIItem> leaderboardPlayersConfig;

    private UIConfig playerShopMainConfig;
    private UIItem playerShopFireWorkItem, playerShopParticleItem, playerShopMaterialItem, playerShopCloseItem;

    public UIConfiguration(ConfigurationHandler handler) {
        super(handler);
        this.leaderboardPlayersConfig = new ArrayList<>();
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
        this.leaderboardMenuConfig = UIConfig.buildFrom(getSectionOf("leaderboard"));
        for(int i =1 ;i<=10; i++){
            final UIItem item = UIItem.buildAsNullable(getSectionOf("leaderboard.buttons.pos-"+i));
            this.leaderboardPlayersConfig.add(item);
        }

        this.particleShopConfig = UIConfig.buildFrom(getSectionOf("shop.particle"));
        this.particleShopItem = UIItem.buildFrom(getSectionOf("shop.particle.buttons.item-button"));

        this.playerShopMainConfig = UIConfig.buildFrom(getSectionOf("player.shop"));
        this.playerShopFireWorkItem = UIItem.buildFrom(getSectionOf("player.shop.buttons.firework"));
        this.playerShopCloseItem = UIItem.buildFrom(getSectionOf("player.shop.buttons.close-button"));
        this.playerShopMaterialItem = UIItem.buildFrom(getSectionOf("player.shop.buttons.material"));
        this.playerShopParticleItem = UIItem.buildFrom(getSectionOf("player.shop.buttons.particle"));
    }

    @Override
    public void prepareReload() {
        this.leaderboardPlayersConfig.clear();
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

    public UIConfig getLeaderboardMenuConfig() {
        return leaderboardMenuConfig;
    }

    public List<UIItem> getLeaderboardPlayersConfig() {
        return leaderboardPlayersConfig;
    }

    public UIConfig getParticleShopConfig() {
        return particleShopConfig;
    }

    public UIItem getParticleShopItem() {
        return particleShopItem;
    }

    public UIConfig getPlayerShopMainConfig() {
        return playerShopMainConfig;
    }

    public UIItem getPlayerShopFireWorkItem() {
        return playerShopFireWorkItem;
    }

    public UIItem getPlayerShopParticleItem() {
        return playerShopParticleItem;
    }

    public UIItem getPlayerShopMaterialItem() {
        return playerShopMaterialItem;
    }

    public UIItem getPlayerShopCloseItem() {
        return playerShopCloseItem;
    }
}
