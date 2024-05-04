package tn.esprit.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tn.esprit.interfaces.MyListener;
import tn.esprit.models.Voyage;
import tn.esprit.controllers.VoyageHome;

public class VoyageItem {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imageFrame;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    private Voyage voyage;
    private MyListener myListener;

    @FXML
    void click(MouseEvent event) {
        myListener.onClickListener(voyage);
    }

    public void setData(Voyage voyage, MyListener myListener){
        this.voyage = voyage;
        this.myListener = myListener;
        nameLabel.setText(voyage.getNom());
        priceLable.setText(String.valueOf(voyage.getPrix()) + VoyageHome.CURRENCY);

        // Create a File object using the absolute path
        File imageFile = new File(voyage.getImage1());

        // Check if the file exists
        if (imageFile.exists()) {
            // Load the image from the file
            Image image = new Image(imageFile.toURI().toString());
            imageFrame.setImage(image);
        } else {
            // Handle case where image file is not found
            System.out.println("Image file not found: " + voyage.getImage1());
            // Optionally, you can set a default image or handle the situation differently
        }
    }

    /*
    @FXML
    void initialize() {
        assert imageFrame != null : "fx:id=\"imageFrame\" was not injected: check your FXML file 'Voyage_item.fxml'.";
        assert nameLabel != null : "fx:id=\"nameLabel\" was not injected: check your FXML file 'Voyage_item.fxml'.";
        assert priceLable != null : "fx:id=\"priceLable\" was not injected: check your FXML file 'Voyage_item.fxml'.";

    }
    */
}
