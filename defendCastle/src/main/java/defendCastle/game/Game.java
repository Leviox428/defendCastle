package defendCastle.game;

import defendCastle.buildings.Building;
import defendCastle.buildings.buildingTypes.Castle;
import defendCastle.map.Tile;
import defendCastle.movable.npc.NPC;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import defendCastle.map.Map;
import defendCastle.map.MapType;
import defendCastle.map.Resource;
import defendCastle.movable.player.Player;
/**
 * Write a description of class Hra here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game {
    private final Timeline gameTickTimeline;
    private final Scene gameScene;
    private final Spawner spawner;
    private final GameDifficulty gameDifficulty;
    private final Player player;
    private final Stage stage;
    private final Map spawnMap;
    private final Map forestMap;
    private final Map quarryMap;
    private MapType currentMapType;
    private ArrayList<Resource> resources;
    private final GameStatus gameStatus;
    private final HBox gameStatusContainer;
    private final GameSceneMouseEventsHandler mouseEventsHandler;
    
    /**
     * Constructor for objects of class Hra
     */
    public Game(GameDifficulty gameDifficulty, Stage stage) {
        this.gameTickTimeline = new Timeline();
        //Setting game difficulty
        this.gameDifficulty = gameDifficulty; 
        //Generating maps
        this.currentMapType = MapType.SPAWN;
        this.spawnMap = new Map(MapType.SPAWN);
        this.forestMap = new Map(MapType.FOREST);
        this.quarryMap = new Map(MapType.QUARRY);
        //Getting spawn map
        Pane spawn = this.spawnMap.getMap();
        //Creating resources counter
        this.spawner = new Spawner(this.spawnMap, this.gameDifficulty); //Adding spawnMap to spawner
        this.gameStatus = new GameStatus(this.spawner, this);
        this.gameStatusContainer = this.gameStatus.getGameStatusContainer();
        Platform.runLater(() -> {
            double containerWidth = this.gameStatusContainer.getWidth();
            this.gameStatusContainer.setLayoutX((((Map.getMapSize()) * Tile.getTileSize()) - containerWidth) / 2);
        });
        this.spawnMap.addGameStatusToMap(this.gameStatusContainer); //Adding resources counter into the spawn map
        //Creating spawner for spawning enemy waves

        //Creating player and adding him to spawn map
        this.player = new Player((Map.getMapSize() * Tile.getTileSize() - 56) / 2.0, (Map.getMapSize() * Tile.getTileSize() - 56) / 2.0, this.gameStatus);
        this.spawnMap.addPlayerToMap(this.player);
        //Getting buildings from spawn map
        ArrayList<Building> buildings;
        buildings = Map.getBuildings();

        //Adding spawn map to our game scene and showing it
        this.gameScene = new Scene(spawn);
        this.stage = stage;
        this.stage.setScene(this.gameScene);
        this.stage.show();
        //Adding mouse click events to our game        
        this.mouseEventsHandler = new GameSceneMouseEventsHandler(this.gameScene, this.spawnMap, this.player, buildings, this.gameStatus);
        this.mouseEventsHandler.generateSceneMouseEventHandlers();
        this.gameStatus.setEventsHandler(this.mouseEventsHandler);
        //Starting game loop
        this.gameLoop();
    }
    public void gameLost() {
        ArrayList<NPC> allNPC = new ArrayList<>();
        allNPC.addAll(Map.getEnemies());
        allNPC.addAll(Map.getAllyNPCs());
        for (NPC npc : allNPC) {
            npc.stopNPCActions();
        }
        this.player.stopPlayerActions();
        this.mouseEventsHandler.removeMouseEventHandlers();
        this.gameTickTimeline.stop();
        this.gameTickTimeline.getKeyFrames().clear();
    }
    public void gameWon() {
        this.mouseEventsHandler.removeMouseEventHandlers();
        this.gameTickTimeline.getKeyFrames().clear();
        this.gameTickTimeline.stop();
    }

    private void gameLoop() {
        KeyFrame positionKeyFrame = new KeyFrame(Duration.seconds(0.5), e -> this.checkPlayerPosition()); //KeyFrame for checking player position each half second
        KeyFrame timerKeyFrame = new KeyFrame(Duration.seconds(1), e -> this.gameTick());
        this.gameTickTimeline.getKeyFrames().addAll(positionKeyFrame, timerKeyFrame);
        this.gameTickTimeline.setCycleCount(Timeline.INDEFINITE);
        this.gameTickTimeline.play();
    }
    
    private void gameTick() {
        if (Castle.castleIsDestroyed()) {
            this.gameLost();
        } else {
            this.gameStatus.updateTimer();
        }
    }
        
    private void checkPlayerPosition() {  
        ImageView player = this.player.getPlayerImage();
        double playerCurrentXPosition = player.getTranslateX();
        double playerCurrentYPosition = player.getTranslateY();
        player.setTranslateX(playerCurrentXPosition);
        boolean generateResourcesOnClick = false;
        switch (this.currentMapType) {                
            case MapType.FOREST:
                if (playerCurrentYPosition > Map.getMapSize() * 14) {
                    this.player.stopPlayerActions();
                    double newYPosition = 20;
                    player.setTranslateY(newYPosition);
                    this.currentMapType = MapType.SPAWN;
                    this.spawnMap.addPlayerToMap(this.player);
                    this.spawnMap.addGameStatusToMap(this.gameStatusContainer);
                    Pane spawn = this.spawnMap.getMap();                       
                    this.gameScene.setRoot(spawn);
                    this.stage.setScene(this.gameScene); 
                    generateResourcesOnClick = true;
                }
                break;
            case MapType.QUARRY:
                if (playerCurrentYPosition < 20) {
                    this.player.stopPlayerActions();
                    double newYPosition = Map.getMapSize() * 14;
                    player.setTranslateY(newYPosition);
                    this.currentMapType = MapType.SPAWN;
                    this.spawnMap.addPlayerToMap(this.player);
                    this.spawnMap.addGameStatusToMap(this.gameStatusContainer);
                    Pane spawn = this.spawnMap.getMap();
                    this.gameScene.setRoot(spawn);
                    this.stage.setScene(this.gameScene);
                    generateResourcesOnClick = true;
                }
                break;
            case MapType.SPAWN:
                if (playerCurrentYPosition < 20) {
                    this.player.stopPlayerActions();
                    double newYPosition = Map.getMapSize() * 14;
                    player.setTranslateY(newYPosition);
                    this.currentMapType = MapType.FOREST;
                    this.forestMap.addPlayerToMap(this.player);
                    this.forestMap.addGameStatusToMap(this.gameStatusContainer);
                    this.resources = this.forestMap.getMapResources();
                    Pane forest = this.forestMap.getMap();
                    this.gameScene.setRoot(forest);
                    this.stage.setScene(this.gameScene); 
                    generateResourcesOnClick = true;
                }
                
                if (playerCurrentYPosition > Map.getMapSize() * 14) {
                    this.player.stopPlayerActions();
                    double newYPosition = 20;
                    player.setTranslateY(newYPosition);
                    this.currentMapType = MapType.QUARRY;
                    this.resources = this.quarryMap.getMapResources();
                    this.quarryMap.addPlayerToMap(this.player);
                    this.quarryMap.addGameStatusToMap(this.gameStatusContainer);
                    Pane quarry = this.quarryMap.getMap();
                    this.gameScene.setRoot(quarry);
                    this.stage.setScene(this.gameScene);
                    generateResourcesOnClick = true;
                        
                }                                        
                break;                
        }
            
        if (generateResourcesOnClick) {
            for (int i = 0; i < this.resources.size(); i++) {
                int index = i;
                this.resources.get(index).getResourceImageContainer().setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        this.player.stopPlayerActions();
                        this.player.mineResource(this.resources.get(index));
                        event.consume();
                    }
                });
            }
        }
    }
}