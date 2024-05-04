package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Reservation;
import tn.esprit.services.ReservationService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierReservation {
    @FXML
    private TextField eventNameTextField;

    @FXML
    private TextField eventidTextField;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextField numberOfReservationsTextField;

    @FXML
    private ComboBox<String> paymentModeComboBox;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField prixToTextField;

    @FXML
    private TextField typeTextField;
    private String nameToSearchWith ="";
    public void ModifierReservation(ActionEvent event)throws SQLException {
        try {
            // Créer une nouvelle réservation avec les valeurs des champs de l'interface utilisateur
            Reservation reservation = new Reservation();
            reservation.setEventName(eventNameTextField.getText());
            reservation.setIdevent(Integer.parseInt(eventidTextField.getText()));
            reservation.setMessage(messageTextField.getText());
            reservation.setNumberOfReservations(Integer.parseInt(numberOfReservationsTextField.getText()));
            // Assurez-vous de convertir le mode de paiement en utilisant un type approprié
            reservation.setPaymentMode(paymentModeComboBox.getValue());


            reservation.setPrix_e(Double.parseDouble(prixTextField.getText()));
            reservation.setPrix_total(Double.parseDouble(prixToTextField.getText()));
            reservation.setType(typeTextField.getText());
            reservation.setDate(Date.valueOf(LocalDate.now()).toLocalDate());


            // Appeler le service pour modifier la réservation
            ReservationService reservationService = new ReservationService();
            reservationService.modifier(reservation);


            // Fermer la fenêtre après la modification
            Stage stage = (Stage) eventNameTextField.getScene().getWindow();
            stage.close();

            // ...
        } catch (NumberFormatException ex) {
            // Gérer les exceptions de conversion de type pour les champs numériques
            ex.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur si nécessaire
            // ...
        }
    }

    public void afficherReservation(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AfficherReservation.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void initData(Reservation reservation) {
        eventNameTextField.setText(reservation.getEventName()); // Remplacez le champ eventNameTextField par le champ approprié pour afficher le nom de l'événement de la réservation
        eventidTextField.setText(String.valueOf(reservation.getIdevent())); // Remplacez le champ eventidTextField par le champ approprié pour afficher l'identifiant de l'événement de la réservation
        messageTextField.setText(reservation.getMessage()); // Remplacez le champ messageTextField par le champ approprié pour afficher le message de la réservation
        // Utilisez le champ approprié pour afficher le nombre de réservations
        numberOfReservationsTextField.setText(String.valueOf(reservation.getNumberOfReservations()));
        // Utilisez le champ approprié pour afficher le mode de paiement
        paymentModeComboBox.setValue(reservation.getPaymentMode());
        prixTextField.setText(String.valueOf(reservation.getPrix_e())); // Utilisez le champ approprié pour afficher le prix de la réservation
        prixToTextField.setText(String.valueOf(reservation.getPrix_total())); // Utilisez le champ approprié pour afficher le prix total de la réservation
        typeTextField.setText(reservation.getType()); // Utilisez le champ approprié pour afficher le type de la réservation
    }

    public void getNameToSearchWith(Reservation chosenEvent) {
        nameToSearchWith = chosenEvent.getEventName();
        System.out.println("the word is :" + nameToSearchWith);
    }


}
