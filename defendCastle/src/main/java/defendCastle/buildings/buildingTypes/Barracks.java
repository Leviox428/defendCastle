package defendCastle.buildings.buildingTypes;

import java.util.ArrayList;

import defendCastle.buildings.Building;
import defendCastle.buildings.BuildingEffect;
import defendCastle.buildings.SpecialBuilding;
import defendCastle.buildings.controllers.BarracksController;
import defendCastle.buildings.controllers.BuildingInfoController;
import defendCastle.game.GameStatus;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressBar;
import javafx.animation.KeyValue;
import defendCastle.map.Map;
import defendCastle.movable.npc.Archer;
import defendCastle.movable.npc.Knight;
import defendCastle.movable.npc.Catapult;
import defendCastle.movable.npc.NPC;
import defendCastle.movable.npc.NPCType;

import java.util.Objects;
import java.util.Random;
/**
 * Write a description of class Buildings.Barracks here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Barracks extends Building implements SpecialBuilding {
    private BarracksController barracksController;
    private final BuildingInfoController controller;
    private final ImageView barracksImage;
    private final Timeline trainingTimeline;
    private final int[] hp = {0, 50, 75, 100, 125, 150};
    private final int[] goldCost = {5, 10, 25, 55, 120};
    private final int[] woodCost = {15, 30, 60, 120, 250};
    private final int[] stoneCost = {15, 30, 60, 120, 250};
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] buildingAnimation = {
        "Misc/Buildings/Barracks/UpgradeAnimation/start.png", "Misc/Buildings/Barracks/UpgradeAnimation/frame2.png", "Misc/Buildings/Barracks/UpgradeAnimation/frame3.png",
        "Misc/Buildings/Barracks/UpgradeAnimation/frame4.png", "Misc/Buildings/Barracks/UpgradeAnimation/frame5.png", "Misc/Buildings/Barracks/barracks.png"
    };
    private final Map map;
    private GameStatus gameStatus;
    private final  ArrayList<ImageView> trainingSlots;
    private final ArrayList<NPC> trainingQueue;
    private final  ProgressBar progressBar;
    private int occupiedSlotsCount;

    /**
     * Constructor for objects of class Farm
     */
    public Barracks(int x, int y, Map map) {
        super(x, y, 80, 80, "/Misc/Buildings/Barracks/barracks.png", map);
        super.setConstructionTime(2);
        super.setBuildingAnimation(this.buildingAnimation);
        super.setBuildingCostsAndHp(this.woodCost, this.stoneCost, this.goldCost, this.hp);
        this.controller = super.getBuildingInfoController();
        this.map = map;
        this.updateBuildingInfo();
        this.trainingTimeline = new Timeline();
        this.barracksImage = super.getBuilding();
        Image cursorImage = new Image("Misc/MouseCursors/army_cursor.png");
        this.barracksImage.setOnMouseEntered(event -> this.barracksImage.setCursor(Cursor.cursor(cursorImage.getUrl())));
        this.barracksImage.setOnMouseExited(event -> this.barracksImage.setCursor(Cursor.DEFAULT));
        this.controller.setBuildingEffectString("You can train your troops here.");
        this.controller.setBuildingName("Barracks");
        this.controller.setBarracks(this);
        this.trainingQueue = new ArrayList<>();
        this.trainingSlots = this.controller.getTrainingSlots();
        this.progressBar = this.controller.getProgressBar();
        this.occupiedSlotsCount = 0;
    }

    public void setController(BarracksController controller) {
        this.barracksController = controller;
    }


    public void trainNPC(String NPC) {
        if (this.gameStatus == null) {
            this.gameStatus = super.getGameStatus();
        }
        NPC newNPC;
        Random random = new Random();
        int randomX = random.nextInt(30);
        int randomY = random.nextInt(30);
        double bottomLeftX = this.barracksImage.getBoundsInParent().getMinX() + randomX;
        double bottomLeftY = this.barracksImage.getBoundsInParent().getMaxY() + randomY;
        int[] trainingCostsAndSpeed = switch (NPC) {
            case "Knight" -> {
                newNPC = new Knight(NPCType.ALLY, 300, 400);
                yield  newNPC.getNPCTrainingCostsAndSpeed();
            }
            case "Archer" -> {
                newNPC = new Archer(NPCType.ALLY, bottomLeftX, bottomLeftY);
                yield newNPC.getNPCTrainingCostsAndSpeed();
            }
            case "Catapult" -> {
                newNPC = new Catapult(NPCType.ALLY, bottomLeftX, bottomLeftY);
                yield newNPC.getNPCTrainingCostsAndSpeed();
            }
            default -> {
                newNPC = null;
                yield null;
            }
        };
        Image NPCImage = Objects.requireNonNull(newNPC).getNPCImage();
        int[] resources = this.gameStatus.getResources();
        int gold = resources[0];
        int food = resources[3];
        int housing = resources[4];
        if (gold >= Objects.requireNonNull(trainingCostsAndSpeed)[0] && food >=  trainingCostsAndSpeed[1] && housing >= trainingCostsAndSpeed[2]) {
           for (ImageView slot : this.trainingSlots) {
                if (slot.getImage() == null) {
                    slot.setImage(NPCImage);
                    this.gameStatus.addResource("Gold", -trainingCostsAndSpeed[0]);
                    this.gameStatus.addResource("Food", -trainingCostsAndSpeed[1]);
                    this.gameStatus.addResource("Housing", -trainingCostsAndSpeed[2]);
                    int trainingSpeed = trainingCostsAndSpeed[3];
                    this.trainingQueue.add(newNPC);
                    this.occupiedSlotsCount++;
                    this.barracksController.setTrainingSlotsOccupied(this.occupiedSlotsCount);
                    if (trainingTimeline.getStatus() != Timeline.Status.RUNNING) {
                        this.trainProgressBarAnimation(newNPC, trainingSpeed);
                    }
                    break;
                }
            }
        }
    }
    
    private void trainProgressBarAnimation(NPC newNPC, int trainingSpeed) {
        this.trainingTimeline.getKeyFrames().clear();
        this.trainingTimeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(trainingSpeed), event -> {
            },
            new KeyValue(this.progressBar.progressProperty(), 1))
        );
        this.trainingTimeline.setOnFinished(event -> {
            this.progressBar.progressProperty().set(0);
            for (ImageView trainingSlot : this.trainingSlots) {
                if (trainingSlot.getImage() != null) {
                    trainingSlot.setImage(null);
                    break;
                }
            }
            this.map.addAllyNPCToMap(newNPC);
            this.trainingQueue.remove(newNPC);
            this.occupiedSlotsCount--;
            this.barracksController.setTrainingSlotsOccupied(this.occupiedSlotsCount);
            this.moveImagesToTheLeft();
            
            if (!this.trainingQueue.isEmpty()) {
                this.trainProgressBarAnimation(this.trainingQueue.getFirst(), this.trainingQueue.getFirst().getNPCTrainingCostsAndSpeed()[3]);
            }
            
        });
        
        this.trainingTimeline.play();
    }
    
    private void moveImagesToTheLeft() {
        for (int i = 0; i < this.trainingSlots.size() - 1; i++) {
            ImageView currentSlot = this.trainingSlots.get(i + 1);
            ImageView previousSlot = this.trainingSlots.get(i);
            if (currentSlot.getImage() != null && previousSlot.getImage() == null) {                
                previousSlot.setImage(currentSlot.getImage());
                currentSlot.setImage(null);
            }
        }
    }

    @Override
    public void setSpecialBuildingEffect() {
        this.controller.setBuildingEffect(BuildingEffect.TROOPS_TRAINING);
    }
}