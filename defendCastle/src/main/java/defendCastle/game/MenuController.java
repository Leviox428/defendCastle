package defendCastle.game;

import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
/**
 * Write a description of class Menu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MenuController {
    
    /**
     * Constructor for objects of class Menu
     */
    
    
    
    @FXML
    private void easyMode(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Game game = new Game(GameDifficulty.EASY, stage);
    }
    
    @FXML
    private void mediumMode(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Game game = new Game(GameDifficulty.MEDIUM, stage);
    }
    
    @FXML
    private void hardMode(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Game game = new Game(GameDifficulty.HARD, stage);
    }
    
    @FXML
    private void settings(MouseEvent event) {
    }
}
