package tn.esprit.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class VoyageHome extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static final String CURRENCY = "DT";


    @Override
    public void start(Stage primaryStage) {

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/CurrencyConverter.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/WeatherForecast.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoyageListInterface.fxml"));
        try
        {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("The Traveller");
            primaryStage.setScene(scene);


            // Adding an icon to the window
            InputStream iconStream = getClass().getResourceAsStream("/img/traveler_icon.png");
            if (iconStream != null) {
                try {
                    Image iconImage = new Image(iconStream);
                    primaryStage.getIcons().add(iconImage);
                } catch (Exception e) {
                    System.err.println("Error loading icon: " + e.getMessage());
                } finally {
                    try {
                        iconStream.close();
                    } catch (IOException e) {
                        System.err.println("Error closing icon stream: " + e.getMessage());
                    }
                }
            } else {
                System.err.println("Icon resource not found!");
            }


            // ends the program when window is closed
            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            primaryStage.show();

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
