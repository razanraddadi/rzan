package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import tn.esprit.models.Event;
import tn.esprit.models.Reservation;
import tn.esprit.services.EventService;

import tn.esprit.services.MyListener;
import tn.esprit.services.MyListenerR;
import tn.esprit.services.ReservationService;

import java.io.File;

import java.net.URL;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherLisReservation implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label EventPriceLabelR;

    @FXML
    private VBox chosenVoyageCard;

    @FXML
    private ImageView delete_btn;

    @FXML
    private Button editR_btn;

    @FXML
    private Label eventNameLabelR;

    @FXML
    private ImageView eventimg;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scrollR;
    private ArrayList<Reservation> list_r = new ArrayList<>();
    private MyListenerR myListener;
    @FXML
    private Label totalincome;

    @FXML
    private ScrollPane scroll;
    @FXML
    private Label todaysincome;
    private ArrayList<Reservation> getList_R(){
        ReservationService sv = new ReservationService();
        return sv.recuperer();
    }

    @FXML
    void EditEvent(ActionEvent event) {
        String name = eventNameLabelR.getText();

        Reservation chosenEvent = null; // Declare without initialization


        for (Reservation unit : list_r) {
            if (unit.getEventName().equals(name)) {
                chosenEvent = unit;
                System.out.println("the thing worked");
                System.out.println(chosenEvent);
                break; // Break out of the loop after finding the voyage
            }
        }

        // Check if the chosen voyage was found
        if (chosenEvent != null) {
            // Pass the chosen voyage to the next page
            System.out.println("IM NAVIGATING");
            navigateToModifierReservation(chosenEvent);
            actualiserGridPane();

        } else {
            // Handle case where the chosen voyage is not found
            System.out.println("Chosen reservation not found: " + name);
        }

    }

    private void navigateToModifierReservation(Reservation chosenEvent) {
        System.out.println("I NAVIGATED");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Get the controller of the next page
        ModifierReservation Controller = loader.getController();
        // Pass the chosen voyage to the next page controller
        Controller.getNameToSearchWith( chosenEvent);
        Controller.initData( chosenEvent);

        // Display the next page
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        list_r = getList_R(); // Remplacer getList_voyages() par la méthode correspondante pour récupérer les événements
        if (!list_r.isEmpty()) {
            setChosenEvent(list_r.get(0)); // Remplacer setChosenVoyage() par la méthode correspondante pour définir l'événement sélectionné
            myListener = new MyListenerR() {
                @Override
                public void onClickListenerR(Reservation r) {
                    setChosenEvent(r);
                }
            };
        }

        // Calculate total income
        double totalIncome = ReservationService.calculateTotalIncome(list_r);
        totalincome.setText(String.valueOf(totalIncome) + " DT");
        double totalIncomeT = ReservationService.calculateTotalIncomeToday( list_r);
        todaysincome.setText(String.valueOf(totalIncomeT) + " DT");

        int column = 0;
        int row = 1;
        try {
            for (Reservation reservation : list_r) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Reservation.fxml"));
                Pane anchorPane = fxmlLoader.load();

                ReservationC controller = fxmlLoader.getController();
                controller.initData(reservation, myListener);

                // Add the anchor pane to the grid
                grid.add(anchorPane, column, row);

                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    private void setChosenEvent(Reservation reservation) {
        eventNameLabelR.setText(reservation.getEventName());
        EventPriceLabelR.setText(reservation.getPrix_total() + " DT"); // Modifier en fonction de la façon dont vous obtenez le prix total

        int ID = reservation.getId();
        // Récupérer le chemin de l'image de la réservation


    }


    public void deleteR(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {

        String name = eventNameLabelR.getText();
        Reservation chosenEvent = null; // Declare without initialization


        for (Reservation unit : list_r) {
            if (unit.getEventName().equals(name)) {
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
                ReservationService sv = new ReservationService();
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

    private void actualiserGridPane() {
        grid.getChildren().clear();
        list_r = getList_R(); // Remplacer getList_voyages() par la méthode correspondante pour récupérer les événements
        if (!list_r.isEmpty()) {
            setChosenEvent(list_r.get(0)); // Remplacer setChosenVoyage() par la méthode correspondante pour définir l'événement sélectionné
            myListener = new MyListenerR() {
                @Override
                public void onClickListenerR(Reservation r) {
                    setChosenEvent(r);
                }
            };
        }

        // Calculate total income
        double totalIncome = ReservationService.calculateTotalIncome(list_r);
        totalincome.setText(String.valueOf(totalIncome) + " DT");
        double totalIncomeT = ReservationService.calculateTotalIncomeToday(list_r);
        todaysincome.setText(String.valueOf(totalIncomeT) + " DT");


        int column = 0;
        int row = 1;
        try {
            for (Reservation reservation : list_r) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Reservation.fxml"));
                Pane anchorPane = fxmlLoader.load();

                ReservationC controller = fxmlLoader.getController();
                controller.initData(reservation, myListener);

                // Add the anchor pane to the grid
                grid.add(anchorPane, column, row);

                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
