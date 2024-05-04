package tn.esprit.controllers;

import javafx.fxml.FXML;

import java.awt.*;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.awt.*;

import java.io.IOException;
import javafx.event.ActionEvent;


import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.Event;
import tn.esprit.services.EventService;
import tn.esprit.services.MyListener;


public class AfficherListEvent implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label EventPriceLabel;

    @FXML
    private Button edit_btn;

    @FXML
    private VBox chosenVoyageCard;

    @FXML
    private ImageView delete_btn;
    @FXML
    private TextField Recherche;

    @FXML
    private Button Recherche_btn;

    @FXML
    private ImageView eventimg;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label eventNameLabel;

    private ArrayList<Event> list_event = new ArrayList<>();
    private MyListener myListener;

    private Image image;
    private ArrayList<Event> getList_event(){
        EventService sv = new EventService();
        return sv.recuperer();
    }
    public void actualiserGridPane() {
        // Effacez le contenu actuel du GridPane
        grid.getChildren().clear();
        list_event = getList_event(); // Remplacer getList_voyages() par la méthode correspondante pour récupérer les événements
        if (!list_event.isEmpty()) {
            setChosenEvent(list_event.get(0)); // Remplacer setChosenVoyage() par la méthode correspondante pour définir l'événement sélectionné
            myListener = new MyListener() {
                @Override
                public void onClickListener(Event event) {
                    setChosenEvent(event); }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (Event event : list_event) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
                Pane anchorPane = fxmlLoader.load();

                EventC controller = fxmlLoader.getController();
                controller.initData(event, myListener);

                // Add the anchor pane to the grid
                grid.add(anchorPane, column, row);

                Button eventButton = new Button("Reserver");
                eventButton.setOnAction(e -> handleEventButtonClick(event));

                // Add the button after adding the anchor pane to ensure it's in the foreground
                grid.add(eventButton, column, row);

                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }
                // No need to set grid width and height repeatedly inside the loop

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        list_event = getList_event(); // Remplacer getList_voyages() par la méthode correspondante pour récupérer les événements
        if (!list_event.isEmpty()) {
            setChosenEvent(list_event.get(0)); // Remplacer setChosenVoyage() par la méthode correspondante pour définir l'événement sélectionné
            myListener = new MyListener() {
                @Override
                public void onClickListener(Event event) {
                    setChosenEvent(event); }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (Event event : list_event) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
                Pane anchorPane = fxmlLoader.load();

                EventC controller = fxmlLoader.getController();
                controller.initData(event, myListener);

                // Add the anchor pane to the grid
                grid.add(anchorPane, column, row);

                Button eventButton = new Button("Reserver");
                eventButton.setOnAction(e -> handleEventButtonClick(event));

                // Add the button after adding the anchor pane to ensure it's in the foreground
                grid.add(eventButton, column, row);

                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }
                // No need to set grid width and height repeatedly inside the loop

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleEventButtonClick(Event event) {
        try {
            // Load the reservation window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            // Pass the event ID to the reservation controller
            AjouterReservation reserverController = loader.getController();
            reserverController.initData(event.getId());

            // Create a new stage for the reservation window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (if needed)
            Stage currentStage = (Stage) grid.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private int setChosenEvent(Event event) {
        eventNameLabel.setText(event.getName());
        EventPriceLabel.setText(event.getPrix() + " DT"); // Assuming Prix is an integer
        File imageFile = new File(event.getImageFile());
        int ID = event.getId();
        // Check if the file exists
        if (imageFile.exists()) {
            // Load the image from the file
            Image image = new Image(imageFile.toURI().toString());
            eventimg.setImage(image);
            chosenVoyageCard.setStyle("-fx-background-color: #eef4f3" + ";\n" +
                    "    -fx-background-radius: 30;");
        } else {
            // Handle case where image file is not found
            System.out.println("Image file not found: " + event.getImageFile());
            // Optionally, you can set a default image or handle the situation differently
        }
        return ID;
    }

    public void EditEvent(ActionEvent event) {
        String name = eventNameLabel.getText();

        Event chosenEvent = null; // Declare without initialization

        // Loop through the list of voyages to find the chosen voyage
        for (Event unit : list_event) {
            if (unit.getName().equals(name)) {
                chosenEvent = unit; // Assign the found voyage
                System.out.println("the thing worked");
                break; // Break out of the loop after finding the voyage
            }
        }

        // Check if the chosen voyage was found
        if (chosenEvent != null) {
            // Pass the chosen voyage to the next page
            System.out.println("IM NAVIGATING");
            navigateToModifierVoyage(chosenEvent);
            actualiserGridPane();

        } else {
            // Handle case where the chosen voyage is not found
            System.out.println("Chosen voyage not found: " + name);
        }

    }

    private void navigateToModifierVoyage(Event chosenEvent) {
        // Load the FXML file of the next page
        System.out.println("I NAVIGATED");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/ModifierEvent.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Get the controller of the next page
        ModifierEvent Controller = loader.getController();
        // Pass the chosen voyage to the next page controller
        Controller.getNameToSearchWith( chosenEvent);
        Controller.initData( chosenEvent);
        grid.getChildren().clear();
        // Display the next page
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void deleteEvent(MouseEvent mouseEvent) {
        String name = eventNameLabel.getText();

        Event chosenEvent = null; // Declare without initialization


        for (Event unit : list_event) {
            if (unit.getName().equals(name)) {
                chosenEvent = unit;
                System.out.println("the thing worked");
                break;
            }

        }


        if (chosenEvent != null) {
            // Pass the chosen voyage to the next page
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de la suppression");
            alert.setContentText("Etes-vous sûre de vouloir supprimer cet item ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("IM deleting");
                EventService sv = new EventService();
                sv.supprimer(chosenEvent.getId());
                actualiserGridPane();

            } else {
                alert.close();
            }

        } else {
            // Handle case where the chosen voyage is not found
            System.out.println("Chosen Event not found: " + name);
        }
    }

    public void RechercheEvent(ActionEvent event) {
        grid.getChildren().clear();

        String chaine = Recherche.getText();
        list_event = new EventService().chercherEvent(chaine);
        myListener = new MyListener() {
            @Override
            public void onClickListener(Event event) {
                setChosenEvent(event); }
        };

    int column = 0;
    int row = 1;
        try {
        for (Event eventt : list_event) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
            Pane anchorPane = fxmlLoader.load();

            EventC controller = fxmlLoader.getController();
            controller.initData(eventt, myListener);

            // Add the anchor pane to the grid
            grid.add(anchorPane, column, row);

            Button eventButton = new Button("Reserver");
            eventButton.setOnAction(e -> handleEventButtonClick(eventt));

            // Add the button after adding the anchor pane to ensure it's in the foreground
            grid.add(eventButton, column, row);

            if (column == 2) {
                column = 0;
                row++;
            } else {
                column++;
            }
            // No need to set grid width and height repeatedly inside the loop

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }


}
