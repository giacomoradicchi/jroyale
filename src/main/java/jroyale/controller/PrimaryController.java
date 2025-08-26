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
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carica l'immagine dell'arena di Clash Royale
        try {
            Image arenaImage = new Image(getClass().getResourceAsStream("/jroyale/images/arena.png"));
            arenaImageView.setImage(arenaImage);
            arenaImageView.setFitWidth(arenaImage.getWidth()*0.65);
            arenaImageView.setFitHeight(arenaImage.getHeight()*0.65);
            arenaImageView.setTranslateY(-80);
            //arenaImageView.setFitHeight(arenaImage.getHeight()*0.6);
            arenaImageView.setPreserveRatio(false); // Adatta esattamente alle dimensioni
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine dell'arena: " + e.getMessage());
        }
    }
}
