package defendCastle.buildings;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class BuildingConstructionZone {
    private final SVGPath constructionZone;
    public BuildingConstructionZone(int width, int xPosition, int yPosition) {
        this.constructionZone = new SVGPath();
        this.constructionZone.setContent("M5 5h90v90H5Z M5 5 L95 5 L95 95 L5 95 L5 5 M50 30 V70 M30 50 H70");
        this.constructionZone.setStroke(Color.WHITE);
        this.constructionZone.setStrokeWidth(2);
        this.constructionZone.getStrokeDashArray().addAll(3.0, 3.0);
        this.constructionZone.setFill(Color.TRANSPARENT);

        double currentSize = Math.max(this.constructionZone.getBoundsInLocal().getWidth(), this.constructionZone.getBoundsInLocal().getHeight());
        double scaleFactor = width / currentSize;

        this.constructionZone.setScaleX(scaleFactor);
        this.constructionZone.setScaleY(scaleFactor);
        this.constructionZone.setTranslateX(xPosition - (this.constructionZone.getBoundsInLocal().getWidth() / 2));
        this.constructionZone.setTranslateY(yPosition - (this.constructionZone.getBoundsInLocal().getHeight() / 2));
    }

    public SVGPath getConstructionZone() {
        return this.constructionZone;
    }

    public void setVisible(boolean visible) {
        this.constructionZone.setVisible(visible);
    }
}
