package defendCastle.buildings;

import defendCastle.buildings.controllers.BuildingInfoController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import defendCastle.map.Map;

import java.io.IOException;

public class BuildingInfo {
    private final Pane buildingInfoPane;
    private boolean buildingInfoIsDisplayed;
    private final BuildingInfoController controller;
    public BuildingInfo(double translateX, double translateY) {
        this.buildingInfoIsDisplayed = false;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/buildingInfo.fxml"));
        try {
            this.buildingInfoPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.controller = loader.getController();
        this.buildingInfoPane.setTranslateX(translateX - (this.buildingInfoPane.getBoundsInLocal().getWidth() / 2));
        this.buildingInfoPane.setTranslateY(translateY - (this.buildingInfoPane.getBoundsInLocal().getWidth() / 2));
        this.buildingInfoPane.setVisible(false);
    }

    public BuildingInfoController getController() {
        return this.controller;
    }

    public void setVisible(boolean visible) {
        Platform.runLater(() -> this.buildingInfoPane.setVisible(visible));
    }

    public void toFront() {
        this.buildingInfoPane.toFront();
    }

    public void addBuildingInfoToMap(Map map) {
        if (!this.buildingInfoIsDisplayed) {
            map.addElementToMap(this.buildingInfoPane);
            this.buildingInfoIsDisplayed = true;
        }
    }
}
