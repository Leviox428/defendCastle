package defendCastle.buildings;

import javafx.scene.image.ImageView;

public class BuildingImageContainer {
    ImageView buildingImageContainer;

    public BuildingImageContainer(int height, int width, int x, int y) {
        this.buildingImageContainer = new ImageView();
        this.buildingImageContainer.preserveRatioProperty();
        this.buildingImageContainer.setFitHeight(height);
        this.buildingImageContainer.setFitWidth(width);
        this.buildingImageContainer.setTranslateX(x - (this.buildingImageContainer.getBoundsInLocal().getWidth() / 2));
        this.buildingImageContainer.setTranslateY(y - (this.buildingImageContainer.getBoundsInLocal().getHeight() / 2));
        this.buildingImageContainer.setVisible(false);
    }

    public ImageView getBuildingImageContainer() {
        return buildingImageContainer;
    }
}
