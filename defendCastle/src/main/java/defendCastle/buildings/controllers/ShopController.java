package defendCastle.buildings.controllers;

import defendCastle.buildings.buildingTypes.Blacksmith;
import defendCastle.game.GameStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import defendCastle.movable.player.upgrades.PlayerUpgrade;
import defendCastle.movable.player.upgrades.upgradeTypes.BuildingSpeedUpgrade;
import defendCastle.movable.player.upgrades.upgradeTypes.MovementSpeedUpgrade;
import defendCastle.movable.player.upgrades.upgradeTypes.StoneGatheringUpgrade;
import defendCastle.movable.player.upgrades.upgradeTypes.WoodGatheringUpgrade;

import java.util.ArrayList;
import java.util.Objects;


public class ShopController {
    @FXML
    private ScrollPane itemStoreScrollPane;
    @FXML
    private Label woodUpgradeNumberLabel;
    @FXML
    private Label stoneUpgradeNumberLabel;
    @FXML
    private Label buildUpgradeNumberLabel;
    @FXML
    private Label movementSpeedUpgradeNumberLabel;
    private GameStatus gameStatus;
    private Blacksmith blacksmith;
    private ArrayList<PlayerUpgrade> playerUpgrades;

    @FXML
    private void initialize() {
        this.playerUpgrades = new ArrayList<>();
        this.playerUpgrades.add(new WoodGatheringUpgrade());
        this.playerUpgrades.add(new StoneGatheringUpgrade());
        this.playerUpgrades.add(new BuildingSpeedUpgrade());
        this.playerUpgrades.add(new MovementSpeedUpgrade());
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setBlacksmith(Blacksmith blacksmith) {
        this.blacksmith = blacksmith;
    }

    public ScrollPane getShopScrollPane() {
        return this.itemStoreScrollPane;
    }
    @FXML
    private void buyUpgrade(MouseEvent event) {
        PlayerUpgrade upgrade;
        int[] resources = this.gameStatus.getResources();
        int gold = resources[0];
        Button clickedButton = (Button) event.getSource();
        Label upgradeLabel;
        String buttonText = clickedButton.getText();
        upgradeLabel = switch (buttonText) {
            case "Wood" -> {
                upgrade = this.playerUpgrades.getFirst();
                yield this.woodUpgradeNumberLabel;
            }
            case "Stone" -> {
                upgrade = this.playerUpgrades.get(1);
                yield this.stoneUpgradeNumberLabel;
            }
            case "Build" -> {
                upgrade = this.playerUpgrades.get(2);
                yield this.buildUpgradeNumberLabel;
            }
            case "Movement" -> {
                upgrade = this.playerUpgrades.get(3);
                yield this.movementSpeedUpgradeNumberLabel;
            }
            default -> {
                upgrade = null;
                yield null;
            }
        };
        if (gold >= 50 && this.blacksmith.playerCanBuyItem() && Objects.requireNonNull(upgrade).upgradeCanBeBought()) {
            upgrade.applyUpgrade(this.blacksmith.getPlayer());
            this.gameStatus.addResource("Gold", (-50));
            Objects.requireNonNull(upgradeLabel).setText(upgrade.getCurrentUpgradeStatus());
        }
        event.consume();
    }

    @FXML
    private void goBack(MouseEvent event) {
        this.itemStoreScrollPane.setVisible(false);
        event.consume();
    }
}
