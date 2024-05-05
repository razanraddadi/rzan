package tn.esprit;
import org.json.JSONObject;
import tn.esprit.controllers.WeatherForecast;
import tn.esprit.models.Activite;
import tn.esprit.models.Voyage;
import tn.esprit.services.ServiceActivite;
import tn.esprit.services.ServiceVoyage;
import tn.esprit.utils.MyDataBase;
import tn.esprit.services.BlogService;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ServiceVoyage.ItemNotFoundException, ServiceActivite.ItemNotFoundException ,BlogService.ItemNotFoundException  {
        String jsonString = "{\"key\": \"value\"}"; // Exemple de chaîne JSON
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);

        System.out.println(WeatherForecast.getLocationData("Tokyo"));
        MyDataBase myDatabase = new MyDataBase();
        Connection connection = myDatabase.getConnection();

        if (connection != null) {
            System.out.println("Connexion à la base de données établie avec succès !");

        } else {
            System.out.println("Erreur lors de la connexion à la base de données.");
        }

    }

}