package defendCastle.game;

import defendCastle.buildings.Building;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import defendCastle.map.Map;
import defendCastle.movable.npc.NPC;
import defendCastle.movable.npc.NPCType;
import defendCastle.movable.player.Player;
import defendCastle.movable.player.PlayerAction;

import java.util.ArrayList;
import java.util.Random;

public class GameSceneMouseEventsHandler {
    private boolean playerCanPerformActions;
    private final Scene gameScene;
    private final Map spawnMap;
    private Rectangle selectionRectangle;
    private double selectionRectangleStartX;
    private double selectionRectangleStartY;

    private final GameStatus gameStatus;
    private final ArrayList<Building> buildings;
    private final Player player;
    public GameSceneMouseEventsHandler(Scene gameScene, Map spawnMap, Player player, ArrayList<Building> buildings, GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.buildings = buildings;
        this.player = player;
        this.gameScene = gameScene;
        this.spawnMap = spawnMap;
        this.playerCanPerformActions = true;
    }

    public void generateSceneMouseEventHandlers() {
        //Selection rectangle for selecting multiple NPCs at once
        this.gameScene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.selectionRectangleStartX = event.getX();
                this.selectionRectangleStartY = event.getY();
                this.selectionRectangle = new Rectangle(this.selectionRectangleStartX, this.selectionRectangleStartY, 0, 0);
                this.selectionRectangle.setFill(Color.TRANSPARENT);
                this.selectionRectangle.setStroke(Color.BLUE);
                this.spawnMap.getMap().getChildren().add(this.selectionRectangle);

                this.gameScene.setOnMouseDragged(dragEvent -> {
                    double minX = Math.min(this.selectionRectangleStartX, dragEvent .getX());
                    double minY = Math.min(this.selectionRectangleStartY, dragEvent .getY());
                    double maxX = Math.max(this.selectionRectangleStartX, dragEvent .getX());
                    double maxY = Math.max(this.selectionRectangleStartY, dragEvent .getY());
                    this.selectionRectangle.setX(minX);
                    this.selectionRectangle.setY(minY);
                    this.selectionRectangle.setWidth(maxX - minX);
                    this.selectionRectangle.setHeight(maxY - minY);
                });

                this.gameScene.setOnMouseReleased(exitEvent -> {
                    if (exitEvent.getButton() == MouseButton.PRIMARY) {
                        for (NPC allyNPC : Map.getAllyNPCs()) {
                            ImageView NPCHitbox = allyNPC.getNPCHitbox();
                            Bounds hitbox = NPCHitbox.localToScene(NPCHitbox.getBoundsInLocal());

                            Platform.runLater(() -> {
                                if (this.selectionRectangle.intersects(hitbox)) {
                                    allyNPC.selectNPC();
                                }
                            });
                        }
                        this.spawnMap.getMap().getChildren().remove(this.selectionRectangle);
                    }
                });

            }
        });
        //  Rgh click events
        this.gameScene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                int numberOfSelectedNPCS = 0;
                for (NPC selectedNPC : Map.getAllyNPCs()) {
                    // NPCs movements with right clicking
                    if (selectedNPC.isSelected()) {
                        numberOfSelectedNPCS++;
                        Random random = new Random();
                        double offsetX = 0;
                        double offsetY = 0;
                        if (numberOfSelectedNPCS > 1) {
                            offsetX = random.nextDouble() * 45 - 25;
                            offsetY = random.nextDouble() * 45 - 25;
                        }

                        selectedNPC.stopNPCActions();

                        double initialX = selectedNPC.getNPC().getTranslateX();
                        double initialY = selectedNPC.getNPC().getTranslateY();


                        double newPositionX = event.getX() - selectedNPC.getNPC().getBoundsInParent().getWidth() / 2 + offsetX;
                        double newPositionY = event.getY() - selectedNPC.getNPC().getBoundsInParent().getHeight() / 2 + offsetY;

                        double deltaX = newPositionX - initialX;
                        double deltaY = newPositionY - initialY;

                        selectedNPC.move(selectedNPC.distance(deltaX, deltaY), newPositionX, newPositionY, NPCType.ALLY);
                    }
                }

                if (numberOfSelectedNPCS > 0) { // If none NPC is selected then we move player
                    event.consume();
                } else {
                    if (this.playerCanPerformActions) {

                        this.player.stopPlayerActions();

                        double initialX = this.player.getPlayerImage().getTranslateX();
                        double initialY = this.player.getPlayerImage().getTranslateY();

                        double newPositionX = event.getX() - (double) 56 / 2;
                        double newPositionY = event.getY() - (double) 56 / 2;

                        double deltaX = newPositionX - initialX;
                        double deltaY = newPositionY - initialY;

                        this.player.move(this.player.distance(deltaX, deltaY), newPositionX, newPositionY, PlayerAction.MOVE);

                        for (Building building : this.buildings) {
                            building.hideBuildingInfo();
                            if (building.getBuilding().getBoundsInParent().contains(event.getX(), event.getY()) && building.buildingCanBeConstructed()) {
                                this.player.stopPlayerActions();
                                this.player.upgradeBuilding(building);
                            }
                        }

                    }
                }
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                for (NPC selectedNPC : Map.getAllyNPCs()) {
                    if (selectedNPC.isSelected()) {
                        selectedNPC.deselectNPC();
                    }
                }

                for (Building building : this.buildings) {
                    building.hideBuildingInfo();
                    if (building.getBuilding().getBoundsInParent().contains(event.getX(), event.getY())) {
                        building.addGameStatus(this.gameStatus);
                        building.showBuildingInfo();
                    }
                }
            }
        });
    }

    public void removeMouseEventHandlers() {
        // Remove the mouse pressed, mouse dragged, and mouse released handlers for selection behavior
        this.gameScene.setOnMousePressed(null);
        this.gameScene.setOnMouseDragged(null);
        this.gameScene.setOnMouseReleased(null);

        // Remove the mouse clicked handler for right-click and primary click behavior
        this.gameScene.setOnMouseClicked(null);
    }

    public void setPlayerActions(boolean canPerformAction) {
        this.playerCanPerformActions = canPerformAction;
        if (!canPerformAction) {
            this.player.moveToCenter(Map.getCenterOfTheMap(), Map.getCenterOfTheMap());
        }
    }
}
