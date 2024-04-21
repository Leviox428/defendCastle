package defendCastle.map;

import defendCastle.buildings.*;
import defendCastle.buildings.buildingTypes.*;
import defendCastle.movable.npc.NPCType;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import javafx.scene.image.ImageView;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import defendCastle.movable.npc.NPC;
import defendCastle.movable.player.Player;

/**
 * Write a description of class map.Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map {
    private static ArrayList<NPC> enemies;
    private static final int MAP_SIZE = 50;
    private final Pane map;
    private final MapType mapType;
    private ArrayList<Resource> resources;
    private static final ArrayList<Building> BUILDINGS = new ArrayList<>();
    private static ArrayList<NPC> allies;
    private static final int MAP_CENTER = MAP_SIZE * Tile.getTileSize() / 2;

    private boolean allStructuresGenerated;
    private final ExecutorService threadPool;
    private Player player;

    public Map(MapType mapType) {
        this.threadPool = Executors.newFixedThreadPool(3); // Creating threadPool which will be used for multithreading to generate buildings without lag
        this.allStructuresGenerated = false;
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        this.map = new Pane();
        this.mapType = mapType;                
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                Tile groundTile = new Tile(TileType.GRASS);
                ImageView imageView = new ImageView(groundTile.getTileImage());
                imageView.setLayoutX(i * Tile.getTileSize());
                imageView.setLayoutY(j * Tile.getTileSize());
                this.map.getChildren().add(imageView);
            }
        }        
        this.generateResourcesAndCastle();
    }  

    
    public void addPlayerToMap(Player player) {
        this.map.getChildren().add(player.getPlayerImage());
        this.player = player;
    }
    
    public void addAllyNPCToMap(NPC newNPC) {
        VBox newNpcImage = newNPC.getNPC();        
        this.map.getChildren().add(newNpcImage);
        allies.add(newNPC);
        newNPC.spawnNPC();
    }    
    
    public void addGameStatusToMap(HBox gameStatus) {
        this.map.getChildren().add(gameStatus);
    }
    
    public static ArrayList<NPC> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return this.player;
    }
    public void addEnemyWaveToMap(ArrayList<NPC> NPCs) {
        for (NPC npc : NPCs) {
            this.map.getChildren().add(npc.getNPC());
            npc.spawnNPC();
            enemies.add(npc);
        }
    }
    
    public ArrayList<Resource> getMapResources() {
        return this.resources;
    }
    
    public Pane getMap() {
        return this.map;
    }
    
    public static int getMapSize() {
        return MAP_SIZE;
    }
    
    public static ArrayList<Building> getBuildings() {
        return BUILDINGS;
    }
    
    public static ArrayList<NPC> getAllyNPCs() {
        return allies;
    }
    
    public static int getCenterOfTheMap() {
        return MAP_CENTER;
    }

    public void generateAllStructures() {
        if (!this.allStructuresGenerated) {
            this.allStructuresGenerated = true;
            threadPool.execute(new BuildingGenerator(this));
            threadPool.shutdown();
        }
    }

    public static void removeNPC(NPC npc) {
        if (npc.getNPCType() == NPCType.ALLY) {
            allies.remove(npc);
        } else {
            enemies.remove(npc);
        }


    }

    public void addElementToMap(Node element) {
        this.map.getChildren().add(element);
    }

    public void addBuilding(Building building) {
        BUILDINGS.add(building);
    }
    public synchronized void addBuildingConstructionZoneToMap(Building building) {
        this.map.getChildren().add(building.getConstructionZone());
        this.map.getChildren().add(building.getBuilding());
    }
    private void generateResourcesAndCastle() {
        int minRange = 30;       
        int maxRange = MAP_SIZE * 14;
        switch (this.mapType) {
            case MapType.FOREST:
                this.resources = new ArrayList<>();
                for (int k = 0; k < 15; k++) {
                    Random random = new Random();
                    int randomXLocation = random.nextInt((maxRange - minRange) + 1) + minRange;
                    int randomYLocation = random.nextInt((maxRange - minRange) + 1) + minRange;
                    Resource resource = new Tree();
                    this.resources.add(resource);
                    ImageView imageView = resource.getResourceImageContainer();
                    imageView.setFitHeight(56);
                    imageView.setFitWidth(56);
                    imageView.setLayoutX(randomXLocation);
                    imageView.setLayoutY(randomYLocation);
                    this.map.getChildren().add(imageView);
                }
                break;
            case MapType.QUARRY:
                this.resources = new ArrayList<>();
                for (int k = 0; k < 15; k++) {
                    Random random = new Random();
                    int randomXLocation = random.nextInt((maxRange - minRange) + 1) + minRange;
                    int randomYLocation = random.nextInt((maxRange - minRange) + 1) + minRange;
                    Resource resource = new Stone();
                    this.resources.add(resource);
                    ImageView imageView = resource.getResourceImageContainer();
                    imageView.setFitHeight(56);
                    imageView.setFitWidth(56);
                    imageView.setLayoutX(randomXLocation);
                    imageView.setLayoutY(randomYLocation);
                    this.map.getChildren().add(imageView);
                }
                break;
            case MapType.SPAWN:
                Building castle = new Castle(MAP_CENTER, MAP_CENTER, this);
                BUILDINGS.add(castle);
                this.addBuildingConstructionZoneToMap(castle);
                break;                                
        }                
    }

}