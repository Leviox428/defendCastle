package defendCastle.map;

import defendCastle.buildings.Building;
import defendCastle.buildings.buildingTypes.*;
import javafx.application.Platform;

public class BuildingGenerator implements Runnable {
    private final Map map;

    public BuildingGenerator(Map map) {

        this.map = map;
    }

    @Override
    public void run() {
        generateAllStructures();
    }

    public void generateAllStructures() {
        int center = Map.getCenterOfTheMap();
        int buildingsSquareSize = 420; // Size of the whole buildable area
        int wallImageSize = 30;

        // Generating instances of all buildings used in game
        Building[] buildingsToAdd = {
                new Windmill(center + 130, center - 50, this.map),
                new Barracks(center - 100, center - 50, this.map),
                new Blacksmith(center + 100, center + 20, this.map),
                new House(center + 50, center + 100, this.map),
                new LumberMill(center - 50, center + 100, this.map),
                new Quarry(center + 50, center - 100, this.map)
        };

        for (Building building : buildingsToAdd) {
            this.map.addBuilding(building);
            Platform.runLater(() -> this.map.addBuildingConstructionZoneToMap(building));
        }

        // Adding walls
        addWalls(center, buildingsSquareSize, wallImageSize);
    }



    private void addWalls(int center, int buildingsSquareSize, int wallImageSize) {
        int halfCenter = center / 2;
        // Top wall

        for (int i = 1; i < (buildingsSquareSize / wallImageSize) - 1; i++) {
            Building topWall = new Wall(i * wallImageSize + halfCenter, halfCenter, this.map);
            this.map.addBuilding(topWall);
            Platform.runLater(() -> this.map.addBuildingConstructionZoneToMap(topWall));
        }

        // Bottom wall
        for (int i = 1; i < (buildingsSquareSize / wallImageSize) - 1; i++) {
            Building bottomWall = new Wall(i * wallImageSize + halfCenter, buildingsSquareSize - wallImageSize + halfCenter, this.map);
            this.map.addBuilding(bottomWall);
            Platform.runLater(() -> this.map.addBuildingConstructionZoneToMap(bottomWall));
        }

        // Left wall
        for (int i = 1; i < (buildingsSquareSize / wallImageSize) - 1; i++) {
            Building leftWall = new Wall(halfCenter, i * wallImageSize + halfCenter, this.map);
            this.map.addBuilding(leftWall);
            Platform.runLater(() -> this.map.addBuildingConstructionZoneToMap(leftWall));
        }

        // Right wall
        for (int i = 1; i < (buildingsSquareSize / wallImageSize) - 1; i++) {
            Building rightWall = new Wall(buildingsSquareSize - wallImageSize + halfCenter, i * wallImageSize + halfCenter, this.map);
            this.map.addBuilding(rightWall);
            Platform.runLater(() -> this.map.addBuildingConstructionZoneToMap(rightWall));
        }
    }
}