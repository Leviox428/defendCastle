package defendCastle.game;

import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import defendCastle.map.Map;
import defendCastle.map.Resource;
import defendCastle.map.Tile;

import java.io.IOException;
/**
 * Write a description of class ResourceCounter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameStatus {
    private int currentTime;
    private final int preparationTimeSecond = 180;
    private HBox gameStatusContainer;
    private int woodCount = 30;
    private int stoneCount = 30;
    private int goldCount = 20;
    private int foodCount = 15;
    private int housing = 5;
    private int currentWave;
    private final Spawner spawner;
    private boolean waveIsSpawned;
    private final Game game;
    private GameSceneMouseEventsHandler eventsHandler;
    private final GameStatusController controller;
    /**
     * Constructor for objects of class ResourceCounter
     */
    public GameStatus(Spawner spawner, Game game) {
        this.game = game;
        this.spawner = spawner;
        this.currentWave = 0;
        this.currentTime = this.preparationTimeSecond;
        this.waveIsSpawned = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameStatus.fxml"));
        try {
            this.gameStatusContainer = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameStatusContainer.setPrefWidth(Map.getMapSize() * Tile.getTileSize());
        this.controller = loader.getController();
        this.controller.setTime(this.preparationTimeSecond);
        this.controller.setNewGoldAmount(this.goldCount);
        this.controller.setNewStoneAmount(this.stoneCount);
        this.controller.setNewWoodAmount(this.woodCount);
        this.controller.setNewFoodAmount(this.foodCount);
        this.controller.setNewHousingAmount(this.housing);
    }

    public void setEventsHandler(GameSceneMouseEventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }

    public HBox getGameStatusContainer() {
        return this.gameStatusContainer;
    }
    
    public int[] getResources() {
        return new int[]{this.goldCount, this.woodCount, this.stoneCount, this.foodCount, this.housing};
    }
    
    public void addResource(Resource resource, int amount) {
        String resourceType = resource.getClass().getSimpleName();
        switch (resourceType) {
            case "Tree":
                this.woodCount += amount;
                this.controller.setNewWoodAmount(this.woodCount);
                break;
            case "Stone":
                this.stoneCount += amount;
                this.controller.setNewStoneAmount(this.stoneCount);
                break;
        }        
    }
    
    public void addResource(String resource, int amount) {
        switch (resource) {
            case "Gold":
                this.goldCount += amount;
                this.controller.setNewGoldAmount(this.goldCount);
                break;
            case "Wood":
                this.woodCount += amount;
                this.controller.setNewWoodAmount(this.woodCount);
                break;
            case "Stone":
                this.stoneCount += amount;
                this.controller.setNewStoneAmount(this.stoneCount);
                break;
            case "Food":
                this.foodCount += amount;
                this.controller.setNewFoodAmount(this.foodCount);
                break;
            case "Housing":
                this.housing += amount;
                this.controller.setNewHousingAmount(this.housing);
                break;
        }
    }
    
    public void updateTimer() {
        if (Map.getEnemies().isEmpty()) {
            if (this.currentWave == 10) {
                this.game.gameWon();
            }
            this.eventsHandler.setPlayerActions(true);
            this.currentTime--;
            this.controller.setTime(this.currentTime);
            this.waveIsSpawned = false;
        }
        if (this.currentTime <= 0 && !waveIsSpawned) {
            this.currentWave++;
            this.controller.updateWave(this.currentWave);
            this.controller.setTime(this.preparationTimeSecond);
            this.spawner.spawnWave(this.currentWave);
            this.eventsHandler.setPlayerActions(false);
            this.currentTime = this.preparationTimeSecond;
            this.waveIsSpawned = true;
        }
    }
    
}
