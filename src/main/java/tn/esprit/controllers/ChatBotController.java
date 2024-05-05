package tn.esprit.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBotController implements Initializable {

    /**
     * Initializes the controller class.
     */
    // Couleur des messages de l'utilisateur
    private static final String USER_MESSAGE_STYLE = "-fx-background-color: #87CEEB ; -fx-padding: 5px; -fx-border-radius: 10px;";
    // Couleur des messages du chatbot
    private static final String CHATBOT_MESSAGE_STYLE = "-fx-background-color: #D3D3D3; -fx-padding: 5px; -fx-border-radius: 10px;";

    @FXML
    private Label lblTitle;
    @FXML
    private VBox messageContainer;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnSend;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set button action
        btnSend.setOnAction(event -> {
            String input = txtInput.getText();
            String response = getResponse(input);
            appendUserMessage(input);
            appendChatbotMessage(response);
            txtInput.clear();
        });
    }

    private String getResponse(String input) {
        input = input.toLowerCase(); // Convert input to lowercase once for efficiency

        if (input.contains("salut") || input.contains("bonjour") || input.contains("bonsoir")) {
            return "Salut ! Comment puis-je vous aider ? Si vous avez des questions sur nos blogs, les pays, les visites touristiques ou les monuments, n'hésitez pas à demander.";
        }
        // Response for inquiries about blogs
        else if (input.contains("blogs") || input.contains("articles")) {
            return "Nos blogs sont une excellente source d'informations sur les voyages, les destinations populaires, les conseils pratiques et bien plus encore. Vous pouvez les consulter sur notre site web.";
        }
        // Response for inquiries about countries
        else if (input.contains("pays") || input.contains("destinations")) {
            return "Nous proposons des informations sur de nombreux pays à travers le monde. Quel pays vous intéresse ?";
        }
        // Response for inquiries about tourist attractions
        else if (input.contains("visites touristiques") || input.contains("attractions")) {
            return "Nous avons des informations sur de nombreuses attractions touristiques. Quelle attraction ou destination vous intéresse ?";
        }
        // Response for inquiries about specific countries
        else if (input.contains("tunisie")) {
            return "La Tunisie est un pays fascinant en Afrique du Nord, connu pour ses plages de sable blanc, son histoire riche et ses souks animés. Quelles informations souhaitez-vous connaître sur la Tunisie ?";
        }
        else if (input.contains("suisse")) {
            return "La Suisse est un magnifique pays d'Europe connu pour ses paysages alpins époustouflants, ses montagnes majestueuses et ses villes cosmopolites. Quelles informations souhaitez-vous connaître sur la Suisse ?";
        }
        // Response for inquiries about monuments
        else if (input.contains("monuments")) {
            return "Les monuments sont des témoins de l'histoire et de la culture. Quel monument spécifique vous intéresse ?";
        }
        // Generic response for all other inputs
        else {
            return "Je suis désolé, je ne suis pas sûr de comprendre votre question. Si vous avez des questions sur nos blogs, les pays, les visites touristiques ou les monuments, n'hésitez pas à demander.";
        }
    }

    private void appendUserMessage(String message) {
        // Create a new label for the user's message
        Label userLabel = new Label(message);
        // Set the style for the user's message
        userLabel.setStyle(USER_MESSAGE_STYLE);
        // Set the width of the label to prevent truncation
        userLabel.setWrapText(true);
        userLabel.setMaxWidth(messageContainer.getWidth() * 0.5);
        // Add the label to the message container with a margin
        VBox.setMargin(userLabel, new Insets(5, 50, 5, 5));
        messageContainer.getChildren().add(userLabel);
    }

    private void appendChatbotMessage(String message) {
        // Create a new label for the chatbot's message
        Label chatbotLabel = new Label(message);
        // Set the style for the chatbot's message
        chatbotLabel.setStyle(CHATBOT_MESSAGE_STYLE);
        // Set the width of the label to prevent truncation
        chatbotLabel.setWrapText(true);
        chatbotLabel.setMaxWidth(messageContainer.getWidth() * 0.7);
        // Add the label to the message container with a margin
        VBox.setMargin(chatbotLabel, new Insets(5, 5, 5, 50));
        messageContainer.getChildren().add(chatbotLabel);
    }


}
