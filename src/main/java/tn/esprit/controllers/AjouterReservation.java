package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Event;
import tn.esprit.models.Reservation;
import tn.esprit.services.ReservationService;
import tn.esprit.services.EventService;
import tn.esprit.utils.MyDataBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterReservation {
    @FXML
    private TextField messageTextField;

    @FXML
    private TextField numberOfReservationsTextField;

    @FXML
    private ComboBox<?> paymentModeComboBox;

    @FXML
    private TextField typeTextField;

    // Injecting the ReservationService
    private ReservationService reservationService;
    @FXML private TextField prixTextField;
    @FXML private TextField prixToTextField;

    @FXML private TextField eventNameTextField;
    @FXML private TextField  eventidTextField;


    // Constructor to inject the service


    @FXML
    void afficherReservation(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/ReservationList.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();    }

    @FXML
    private void ajouterReservation(ActionEvent e) {
        try {
            // Instanciate your reservation management service
            ReservationService reservationService = new ReservationService();
            EventService eventService = new EventService();

            // Check if the database connection is closed
            if (MyDataBase.getInstance().getConnection().isClosed()) {
                MyDataBase.getInstance().getConnection();
                showAlert(Alert.AlertType.ERROR, "Error", "Database connection is closed.");
                return;
            }

            // Retrieve data from the fields
            String message = messageTextField.getText();
            int numberOfReservations = Integer.parseInt(numberOfReservationsTextField.getText());
            String paymentMode = (String) paymentModeComboBox.getValue(); // Ensure ComboBox is initialized properly
            String type = typeTextField.getText();
            String eventName = eventNameTextField.getText();
            double prix_e = Double.parseDouble(prixTextField.getText());
            double prix_total = Double.parseDouble(prixToTextField.getText());
            int eventid = Integer.parseInt(eventidTextField.getText()); // Retrieve the value from eventidTextField
            LocalDate currentDate = LocalDate.now();

            // Check if payment mode is selected
            if (paymentMode == null || paymentMode.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a payment mode.");
                return;
            }

            // Retrieve the event for which the reservation is being made
            Event event = eventService.getEventById(eventid);

            if (event != null) {

                    // Create a new reservation object
                    Reservation reservation = new Reservation();
                    reservation.setMessage(message);
                    reservation.setNumberOfReservations(numberOfReservations);
                    reservation.setPaymentMode(paymentMode);
                    reservation.setType(type);
                    reservation.setEventName(eventName);
                    reservation.setPrix_e(prix_e);
                    reservation.setPrix_total(prix_total);
                    reservation.setIdevent(eventid);
                    reservation.setDate(currentDate);

                    // Add the reservation using the service
                    reservationService.addReservation(event, reservation);
                    reservationService.ajouter(reservation);
                generatePDF(reservation);

                    // Show a success alert
                    showAlert(Alert.AlertType.CONFIRMATION, "Success", "Reservation added successfully");

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "The event does not exist.");
            }
        } catch (SQLException ex) {
            // Show an error alert with the SQL error message
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void generatePDF(Reservation reservation) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw background gradient
                contentStream.setNonStrokingColor(new Color(128, 103, 183)); // #8067B7
                contentStream.fillRect(0, 0, 200, 300); // Adjust dimensions as needed

                // Draw white rectangles for top, bottom, and rip sections
                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.fillRect(10, 250, 180, 40); // Top section
                contentStream.fillRect(10, 10, 180, 40);   // Bottom section
                contentStream.fillRect(20, 180, 160, 20); // Rip section

                // Draw event image (replace this with actual code to draw the image)
                // Image img = ...;
                // contentStream.drawImage(img, x, y, width, height);

                // Draw event name
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 270); // Adjust text position as needed
                contentStream.showText(reservation.getEventName());
                contentStream.endText();

                // Draw date, price, and payment mode
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 220); // Adjust text position as needed
                contentStream.showText("Date: " + reservation.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                contentStream.newLine();
                contentStream.showText("Price: " + reservation.getPrix_total());
                contentStream.newLine();
                contentStream.showText("Payment Mode: " + reservation.getPaymentMode());
                contentStream.endText();

                // Draw barcode and reservation details
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 160); // Adjust text position as needed
                contentStream.showText("Reservation ID: " + reservation.getId());
                contentStream.newLine();
                contentStream.showText("Number of Reservations: " + reservation.getNumberOfReservations());
                contentStream.newLine();
                contentStream.showText("Message: " + reservation.getMessage());
                contentStream.endText();

                // Draw "BUY TICKET" button
                contentStream.setNonStrokingColor(new Color(93, 156, 236)); // #5D9CEC
                contentStream.fillRect(20, 20, 160, 30); // Adjust dimensions as needed
                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(80, 35); // Adjust text position as needed
                contentStream.showText("BUY TICKET");
                contentStream.endText();
            }

            document.save("event_details.pdf");
        }
    }



    // Helper method to show alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void initData(int eventId) {



            // Utiliser l'ID de l'événement pour récupérer les détails de l'événement à partir de votre source de données
            // Supposons que vous avez une méthode dans votre service de réservation pour récupérer les détails de l'événement par ID
            Event event = EventService.getEventById(eventId);

            // Utiliser les détails de l'événement pour pré-remplir les champs dans votre formulaire de réservation
            prixTextField.setText(String.valueOf(event.getPrix()));
            eventNameTextField.setText(event.getName());
        eventidTextField.setText(String.valueOf(eventId));

    }

}


