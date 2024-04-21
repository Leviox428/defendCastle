package defendCastle.game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
/**
 * Write a description of class ResourcesCounterController here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameStatusController {    
    @FXML
    private Label goldCounter;
    
    @FXML
    private Label woodCounter;
    
    @FXML
    private Label stoneCounter;
    
    @FXML
    private Label foodCounter;
    
    @FXML
    private Label housingCounter;
    
    @FXML
    private Label waveCounter;
    
    @FXML
    private Label timer;
    public void setNewWoodAmount(int newWoodAmount) {
        this.woodCounter.setText(String.valueOf(newWoodAmount));
    }
    
    public void setNewStoneAmount(int newStoneAmount) {
        this.stoneCounter.setText(String.valueOf(newStoneAmount));
    }
    
    public void setNewGoldAmount(int newGoldAmount) {
        this.goldCounter.setText(String.valueOf(newGoldAmount));
    }
    
    public void setNewFoodAmount(int newFoodAmount) {
        this.foodCounter.setText(String.valueOf(newFoodAmount));
    }
    
    public void setNewHousingAmount(int newHousingAmount) {
        this.housingCounter.setText(String.valueOf(newHousingAmount));
    }
    public void updateWave(int wave) {
        this.waveCounter.setText(Integer.toString(wave));
    }
    
    public void setTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        this.timer.setText(String.format("%d:%02d", minutes, seconds));
    }
       
}