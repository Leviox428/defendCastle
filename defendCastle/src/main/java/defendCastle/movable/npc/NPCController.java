package defendCastle.movable.npc;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
/**
 * Write a description of class npc.NPCController here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NPCController {
    @FXML
    private ProgressBar healthBar;
    
    @FXML
    private Polygon selectionTriangle;
    
    @FXML
    private ImageView NPCHitbox;

    private double maximumHP;
    
    
    @FXML
    private void initialize() {
        this.selectionTriangle.setVisible(false);
        this.healthBar.setStyle("-fx-accent: red; -fx-background-color: red; -fx-background-radius: 0;");

    }
    
    public ImageView getNPCImage() {
        return this.NPCHitbox;
    }
    
    public void setSelectionTriangleVisible(boolean visible) {
        this.selectionTriangle.setVisible(visible);
    }
    
    public void setHealthBarAndSelectionColor(NPCType type) {
        if (type == NPCType.ALLY) {
            this.selectionTriangle.setFill(Color.web("#59ee37"));
            this.healthBar.setStyle("-fx-accent: #59ee37");
        } else { 
            this.selectionTriangle.setFill(Color.web("#ff2929"));
            this.healthBar.setStyle("-fx-accent: #ff2929");
        }
    }

    public void updateHealthBar(double currentHP) {
        if (currentHP <= 0) {
            this.healthBar.setProgress(0);
        } else {
            this.healthBar.setProgress(currentHP / this.maximumHP);
        }
    }

    public void setHealthBar(double hp) {
        this.maximumHP = hp;
    }
}
