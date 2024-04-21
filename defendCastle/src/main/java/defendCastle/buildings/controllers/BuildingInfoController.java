package defendCastle.buildings.controllers;



import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.buildingTypes.Barracks;
import defendCastle.buildings.buildingTypes.Blacksmith;
import defendCastle.game.GameStatus;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

import java.util.ArrayList;


public class BuildingInfoController {
    @FXML
    DefaultController defaultController;
    @FXML
    BarracksController barracksController;
    @FXML
    ShopController shopController;
    @FXML
    private void initialize() {
        this.setTrainingPane();
        this.setShopScrollPane();
    }


    public void setBuildingName(String buildingName) {
        this.defaultController.setBuildingName(buildingName);
    }

    public void setGoldCost(int goldCost) {
        this.defaultController.setGoldCost(goldCost);
    }

    public void setWoodCost(int woodCost) {
        this.defaultController.setWoodCost(woodCost);
    }

    public void setStoneCost(int stoneCost) {
        this.defaultController.setStoneCost(stoneCost);
    }

    public void setBuildingLevel(int buildingLevel) {
        this.defaultController.setBuildingLevel(buildingLevel);
    }

    public void setBuildingHP(int buildingHP) {
        this.defaultController.setBuildingHP(buildingHP);
    }

    public void setBuildingEffectString(String buildingEffect) {
        this.defaultController.setBuildingEffectString(buildingEffect);
    }

    public void setBuildingEffect(BuildingEffect effect) {
        this.defaultController.setBuildingEffect(effect);
    }

    public void setBuilding(Building building) {
        this.defaultController.setBuilding(building);
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.defaultController.setGameStatus(gameStatus);
        this.shopController.setGameStatus(gameStatus);
    }

    public void buildingIsAtMaxLevel() {
        this.defaultController.buildingIsAtMaxLevel();
    }

    public ArrayList<ImageView> getTrainingSlots() {
        return this.barracksController.getTrainingSlots();
    }

    public ProgressBar getProgressBar() {
        return this.barracksController.getProgressBar();
    }

    public void setBlacksmith(Blacksmith blacksmith) {
        this.shopController.setBlacksmith(blacksmith);
    }

    public void setBarracks(Barracks barracks) {
        this.barracksController.setBarracks(barracks);
    }

    private void setTrainingPane() {
        this.defaultController.setTrainingPane(this.barracksController.getTrainingPane());
    }

    private void setShopScrollPane() {
        this.defaultController.setShopScrollPane(this.shopController.getShopScrollPane());
    }

    public void receiveHit(int damage) {
        this.defaultController.receiveHit(damage);
    }
}