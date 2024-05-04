package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import tn.esprit.models.Event;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import tn.esprit.services.EventService;
import tn.esprit.services.MyListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.List;


public class EventC {
    @FXML
    private Label date;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label price;
    Event e;
    private MyListener myListener;


    public void initData(Event event, MyListener myListener) {
        this.e = event;
        this.myListener = myListener;
        name.setText(event.getName());
        price.setText(String.valueOf(event.getPrix()) + " " +"DT");
        date.setText(String.valueOf(event.getDate()));
        // Create a File object using the absolute path
        File imageFile = new File(event.getImageFile());

        // Check if the file exists
        if (imageFile.exists()) {
            // Load the image from the file
            Image image = new Image(imageFile.toURI().toString());
            img.setFitWidth(256);
            img.setFitHeight(221);
            img.setImage(image);
        } else {
            // Handle case where image file is not found
            System.out.println("Image file not found: " + event.getImageFile());
            // Optionally, you can set a default image or handle the situation differently
        }
    }


    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(e);
    }
}
