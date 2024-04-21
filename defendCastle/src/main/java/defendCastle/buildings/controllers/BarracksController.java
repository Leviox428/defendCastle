package defendCastle.buildings.controllers;

import defendCastle.buildings.buildingTypes.Barracks;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import defendCastle.movable.npc.Archer;
import defendCastle.movable.npc.Catapult;
import defendCastle.movable.npc.Knight;
import defendCastle.movable.npc.NPC;

import java.util.ArrayList;

public class BarracksController {
    @FXML
    private Pane trainingPane;
    @FXML
    private Label knightGoldCost;
    @FXML
    private Label knightFoodCost;
    @FXML
    private Label knightHousingCost;
    @FXML
    private Label knightTrainingSpeed;

    @FXML
    private Label archerGoldCost;
    @FXML
    private Label archerFoodCost;
    @FXML
    private Label archerHousingCost;
    @FXML
    private Label archerTrainingSpeed;

    @FXML
    private Label catapultGoldCost;
    @FXML
    private Label catapultFoodCost;
    @FXML
    private Label catapultHousingCost;
    @FXML
    private Label catapultTrainingSpeed;
    @FXML
    private Label trainingSlotsOccupied;
    @FXML
    private ProgressBar trainingProgressBar;
    @FXML
    private ImageView trainingSlot1;
    @FXML
    private ImageView trainingSlot2;
    @FXML
    private ImageView trainingSlot3;
    @FXML
    private ImageView trainingSlot4;
    private ArrayList<ImageView> trainingSlots;
    private Barracks barracks;

    @FXML
    private void initialize() {
        NPC knight = new Knight();
        NPC archer = new Archer();
        NPC catapult = new Catapult();
        int[] knightTrainingCostsAndSpeed = knight.getNPCTrainingCostsAndSpeed();
        int[] archerTrainingCostsAndSpeed = archer.getNPCTrainingCostsAndSpeed();
        int[] catapultTrainingCostsAndSpeed = catapult.getNPCTrainingCostsAndSpeed();
        this.knightGoldCost.setText(String.valueOf(knightTrainingCostsAndSpeed[0]));
        this.knightFoodCost.setText(String.valueOf(knightTrainingCostsAndSpeed[1]));
        this.knightHousingCost.setText(String.valueOf(knightTrainingCostsAndSpeed[2]));
        this.knightTrainingSpeed.setText(knightTrainingCostsAndSpeed[3] + "s");

        this.archerGoldCost.setText(String.valueOf(archerTrainingCostsAndSpeed[0]));
        this.archerFoodCost.setText(String.valueOf(archerTrainingCostsAndSpeed[1]));
        this.archerHousingCost.setText(String.valueOf(archerTrainingCostsAndSpeed[2]));
        this.archerTrainingSpeed.setText(archerTrainingCostsAndSpeed[3] + "s");

        this.catapultGoldCost.setText(String.valueOf(catapultTrainingCostsAndSpeed[0]));
        this.catapultFoodCost.setText(String.valueOf(catapultTrainingCostsAndSpeed[1]));
        this.catapultHousingCost.setText(String.valueOf(catapultTrainingCostsAndSpeed[2]));
        this.catapultTrainingSpeed.setText(catapultTrainingCostsAndSpeed[3] + "s");

        this.trainingSlots = new ArrayList<>();
        this.trainingSlots.add(this.trainingSlot1);
        this.trainingSlots.add(this.trainingSlot2);
        this.trainingSlots.add(this.trainingSlot3);
        this.trainingSlots.add(this.trainingSlot4);
    }

    public Pane getTrainingPane() {
        return this.trainingPane;
    }

    public void setBarracks(Barracks barracks) {
        this.barracks = barracks;
        this.barracks.setController(this);
    }

    public ArrayList<ImageView> getTrainingSlots() {
        return this.trainingSlots;
    }

    public ProgressBar getProgressBar() {
        return this.trainingProgressBar;
    }

    public void setTrainingSlotsOccupied(int occupiedSlotsCount) {
        this.trainingSlotsOccupied.setText(occupiedSlotsCount + "/4");
    }

    @FXML
    private void trainKnight(MouseEvent event) {
        this.barracks.trainNPC("Knight");
        event.consume();
    }

    @FXML
    private void trainArcher(MouseEvent event) {
        this.barracks.trainNPC("Archer");
        event.consume();
    }

    @FXML
    private void trainCatapult(MouseEvent event) {
        this.barracks.trainNPC("Catapult");
        event.consume();
    }

    @FXML
    private void goBack(MouseEvent event) {
        this.trainingPane.setVisible(false);
        event.consume();
    }




}
