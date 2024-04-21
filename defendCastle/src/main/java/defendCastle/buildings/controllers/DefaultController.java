package defendCastle.buildings.controllers;

import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.ResourceGenerator;
import defendCastle.game.GameStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DefaultController {

    private Pane trainingPane;

    private ScrollPane shopScrollPane;
    @FXML
    private Label goldCost;
    @FXML
    private Label woodCost;
    @FXML
    private Label stoneCost;
    @FXML
    private Label buildingName;
    @FXML
    private Label buildingLevel;
    @FXML
    private Label currentHP;
    @FXML
    private Label buildingEffectLabel;

    @FXML
    private ImageView buildingImage;

    @FXML
    private Button upgradeButton;

    @FXML
    private Button buildingEffectButton;
    private Building building;
    private int currentWoodCost;
    private int currentGoldCost;
    private int currentStoneCost;
    private int currentHp;
    private GameStatus gameStatus;
    private BuildingEffect buildingEffect;

    @FXML
    private void initialize() {
        this.buildingEffectButton.setVisible(false);
        this.upgradeButton.setText("Upgrade");
    }

    public void setGoldCost(int goldCost) {
        this.goldCost.setText(String.valueOf(goldCost));
        this.currentGoldCost = goldCost;
    }

    public void setTrainingPane(Pane trainingPane) {
        this.trainingPane = trainingPane;
    }

    public void setShopScrollPane(ScrollPane shop) {
        this.shopScrollPane = shop;
    }

    public void setWoodCost(int woodCost) {
        this.woodCost.setText(String.valueOf(woodCost));
        this.currentWoodCost = woodCost;
    }

    public void setStoneCost(int stoneCost) {
        this.stoneCost.setText(String.valueOf(stoneCost));
        this.currentStoneCost = stoneCost;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName.setText(String.valueOf(buildingName));
    }

    public void setBuildingLevel(int buildingLevel) {
        String level = buildingLevel + "/5";
        this.buildingLevel.setText(level);
    }

    public void setBuildingHP(int buildingHP) {
        this.currentHp = buildingHP;
        this.currentHP.setText(String.valueOf(buildingHP));
    }

    public void setBuildingEffectString(String buildingEffect) {
        this.buildingEffectLabel.setText(buildingEffect);
    }

    public void setBuildingEffect(BuildingEffect effect) {
        this.buildingEffect = effect;
        switch (effect) {
            case BuildingEffect.TROOPS_TRAINING:
                this.buildingEffectButton.setText("Train");
                this.buildingEffectButton.setVisible(true);
                break;
            case BuildingEffect.ITEM_STORE:
                this.buildingEffectButton.setText("Buy Items");
                this.buildingEffectButton.setVisible(true);
                break;
        }
    }

    public void setBuilding(Building building) {
        this.building = building;
        this.buildingImage.setImage(this.building.getBuildingImage());
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void buildingIsAtMaxLevel() {
        this.stoneCost.setText("0");
        this.woodCost.setText("0");
        this.goldCost.setText("0");
        this.upgradeButton.setVisible(false);
        this.upgradeButton.setDisable(true);
    }

    @FXML
    private void showBuildingEffect(MouseEvent event) {
        switch (buildingEffect) {
            case TROOPS_TRAINING:
                this.trainingPane.setVisible(true);
                this.trainingPane.toFront();
                break;
            case ITEM_STORE:
                this.shopScrollPane.setVisible(true);
        }
        event.consume();
    }

    @FXML
    private void upgrade(MouseEvent event) {
        this.buildingEffectButton.setVisible(false);
        int[] resources = this.gameStatus.getResources();
        int gold = resources[0];
        int wood = resources[1];
        int stone = resources[2];
        if (gold >= this.currentGoldCost && wood >= this.currentWoodCost && stone >= this.currentStoneCost && !this.building.buildingCanBeConstructed()) {
            this.building.getBuildingCanBeConstructedImage().setVisible(true);
            this.building.getBuilding().setVisible(false);
            this.gameStatus.addResource("Gold", (-this.currentGoldCost));
            this.gameStatus.addResource("Wood", (-this.currentWoodCost));
            this.gameStatus.addResource("Stone", (-this.currentStoneCost));
            if (this.building instanceof ResourceGenerator generator) {
                generator.stopBuildingEffect();
            }
            this.building.setBuildingCanBeConstructed(true);
            this.building.hideBuildingInfo();
            this.building.showBuildingCanBeConstructedImage();
        }
        event.consume();
    }

    public void receiveHit(int damage) {
        this.currentHp -= damage;
        if (this.currentHp <= 0) {
            this.building.resetBuilding();
            this.currentHP.setText("0");
        } else {
            this.currentHP.setText(Integer.toString(this.currentHp));
        }
    }
}
