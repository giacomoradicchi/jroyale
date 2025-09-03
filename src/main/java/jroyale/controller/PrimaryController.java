package jroyale.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jroyale.App;

public class PrimaryController implements Initializable{

    @FXML
    private ImageView arenaImageView;
    @FXML
    private ImageView kingTowerImageView;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carica l'immagine dell'arena di Clash Royale
        try {
            Image arenaImage = new Image(getClass().getResourceAsStream("/jroyale/images/arena.png"));
            Image king_tower = new Image(getClass().getResourceAsStream("/jroyale/images/towers/blue/king_tower.png"));
            
            // Arena 
            arenaImageView.setImage(arenaImage);
            arenaImageView.setFitWidth(arenaImage.getWidth()*0.65);
            arenaImageView.setFitHeight(arenaImage.getHeight()*0.65);
            arenaImageView.setTranslateY(-80);
            arenaImageView.setTranslateX(-1);
            arenaImageView.setPreserveRatio(false); // Adatta esattamente alle dimensioni

            // King Tower
            kingTowerImageView.setImage(king_tower);
            kingTowerImageView.setFitWidth(king_tower.getWidth()*0.35);
            kingTowerImageView.setFitHeight(king_tower.getHeight()*0.35);  
            kingTowerImageView.setTranslateY(+135);
            

        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine dell'arena: " + e.getMessage());
        }
    }
}
