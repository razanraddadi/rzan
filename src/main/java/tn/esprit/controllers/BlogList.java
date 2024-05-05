package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.esprit.controllers.Cardblogs;
import tn.esprit.interfacesb.MyListenerb;
import tn.esprit.models.Blog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tn.esprit.models.Blog;
import tn.esprit.services.BlogService;
import tn.esprit.utils.Pagination;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.ToggleButton;

import java.util.ResourceBundle;
public class BlogList implements Initializable {
    @FXML
    private ToggleButton favButton;
    @FXML
    private Button favadd;
    @FXML
    private Button favoris;
    @FXML
    private Button blogEdit_btn;

    @FXML
    private ImageView blogimg;
    @FXML
    private Label currentPageLabel;
    @FXML
    private Label totalPagesLabel;
    @FXML
    private VBox chosenblogCard;

    @FXML
    private Label date;

    @FXML
    private ImageView delete_button;

    @FXML
    private HBox goToAjouter;

    @FXML
    private GridPane grid;

    @FXML
    private TextField onsearchbloglabel;
    @FXML
    private ScrollPane scroll;
    @FXML
    private ImageView favorisbutton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label titre;

    private Pagination pagination;
    private ObservableList<Blog> blogs = FXCollections.observableArrayList(getList_blogs());
    private ArrayList<Blog> getList_blogs(){
        BlogService sv = new BlogService();
        return sv.getAll();
    }
    private MyListenerb myListenerb;



    @FXML
    void deleteblog(MouseEvent event) {
        // Retrieve the chosen voyage name
        String name = titre.getText();

        Blog chosenblog = null;
        for (Blog unit : blogs) {
            if (unit.getTitre().equals(name)) {
                chosenblog = unit;
                System.out.println("the thing worked");
                break;
            }
        }

        if (chosenblog != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de la suppression");
            alert.setContentText("Etes-vous sûre de vouloir supprimer cet item ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                System.out.println(" deleting");
                BlogService sv = new BlogService();
                sv.delete(chosenblog);


                blogs.remove(chosenblog);
                // Update the UI by rebuilding the grid
                buildGrid();

            }
            else{
                alert.close();
            }
        } else {
            // Handle case where the chosen voyage is not found
            System.out.println("Chosen blog not found: " + name);
        }

    }
    @FXML
    void onblogedit(ActionEvent event) throws IOException {
        String name = titre.getText();
        Blog chosenblog = null;
        for (Blog unit : blogs) {
            if (unit.getTitre().equals(name)) {
                chosenblog = unit;
                System.out.println("modidffff avec succes ");
                break;
            }
        }
        if (chosenblog != null) {
            System.out.println("IM NAVIGATING");
            navigateToAjouterblog(chosenblog, event);

        } else {
            System.out.println("Chosen blog not found: " + name);
        }

    }

    @FXML
    void navigateToAjouterblog(MouseEvent event) {
        System.out.println("Je navigue");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/AjouterBlog.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }
    private boolean isGridEmpty(){
        ObservableList<Node> children = grid.getChildren();
        return children.isEmpty();
    }
    private void buildGrid() {
        if (!isGridEmpty()) {
            grid.getChildren().clear(); // Clear the grid
        }
        int column = 0;
        int row = 1;
        try {
            for (Blog blog : blogs) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cardblogs.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();


                Cardblogs controller = fxmlLoader.getController();
                controller.setData(blog, myListenerb); // Assurez-vous que BlogItem a une méthode setData appropriée
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row); //(child, column, row)

                // Définir la largeur de la grille
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                // Définir la hauteur de la grille
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int setChosenBlog(Blog blog) {
        titre.setText(blog.getTitre()); // Assurez-vous d'avoir une référence à l'élément correspondant dans votre FXML
        // Assurez-vous d'avoir une référence à l'élément correspondant dans votre FXML pour le prix ou toute autre information spécifique au blog
        File imageFile = new File(blog.getImageb()); // Assurez-vous d'avoir un attribut "image" dans votre classe Blog

        int ID = blog.getId(); // Assurez-vous d'avoir un attribut "id" dans votre classe Blog

        // Vérifiez si le fichier image existe
        if (imageFile.exists()) {
            // Chargez l'image depuis le fichier
            Image image = new Image(imageFile.toURI().toString());
            blogimg.setImage(image); // Assurez-vous d'avoir une référence à l'élément correspondant dans votre FXML pour afficher l'image
            Cardblogs.setStyle("-fx-background-color: #eef4f3" + ";\n" +
                    "    -fx-background-radius: 30;");
        } else {
            //  Gérez le cas où le fichier image n'est pas trouvé
            System.out.println("Image file not found: " + blog.getImageb());
            // Vous pouvez éventuellement définir une image par défaut ou gérer la situation différemment
        }
        return ID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        BlogService blogService = new BlogService();
        List<Blog> allBlogsList = blogService.getAll();

        pagination = new Pagination(blogs, 6); //
        updateGrid();
        blogs.addAll(blogService.getAll());
        if (!blogs.isEmpty()) {
            setChosenBlog(blogs.get(0));
            myListenerb = new MyListenerb() {
                @Override
                public void onClickListenerb(Blog blog) {
                    setChosenBlog(blog);   }
            };
        }
        buildGrid();

    }




    private void navigateToAjouterblog(Blog chosenblog, ActionEvent event) {

        System.out.println("Je navigue");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/AjouterBlog.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void onsearchvoyage(ActionEvent event) throws BlogService.ItemNotFoundException {
        String keyWord = onsearchbloglabel.getText();
        BlogService sv = new BlogService();
        ArrayList<Blog> foundItems = sv.searchByTitle(keyWord);
        blogs = FXCollections.observableArrayList(foundItems);
        buildGrid();

    }

    @FXML
    void onsearchfavoris(ActionEvent event) {
        BlogService sv = new BlogService();
        ArrayList<Blog> foundItems = sv.searchByFavoris(true); // Recherche des blogs avec favoris = true
        blogs = FXCollections.observableArrayList(foundItems);
        buildGrid();
    }

    @FXML
    void onfavadd(ActionEvent event) {

        String name = titre.getText();
        Blog chosenBlog = null;
        for (Blog unit : blogs) {
            if (unit.getTitre().equals(name)) {
                chosenBlog = unit;
                break;
            }
        }
        if (chosenBlog != null) {
            chosenBlog.setFavoris(true);
            BlogService blogService = new BlogService();
            blogService.addToFavorites(chosenBlog);

            buildGrid();
            // Update the blog in the database or wherever it's stored

            blogService.update(chosenBlog);

            ArrayList<Blog> blogArrayList = new ArrayList<>(blogs);
            blogService.displayAll(blogArrayList);
            // Optionally, provide feedback to the user
            System.out.println("Blog ajouté aux favoris avec succès !");
        } else {
            // Provide feedback to the user that no blog is chosen
            System.out.println("Aucun blog choisi.");
        }
    }
    @FXML
    void nextPage(ActionEvent event) {
        pagination.nextPage(); // Passez à la page suivante
        updateGrid(); // Mettez à jour la grille avec les blogs de la nouvelle page

    }
    @FXML
    void previouspage(ActionEvent event) {
        pagination.previousPage(); // Passez à la page précédente
        updateGrid(); // Mettez à jour la grille avec les blogs de la nouvelle page

    }
    private void updateGrid() {
        grid.getChildren().clear(); // Effacez le contenu actuel de la grille

        // Obtenez les blogs de la page actuelle à partir de la pagination
        ObservableList<Blog> currentPageBlogs = pagination.getCurrentPageBlogs();

        // Ajoutez les blogs de la page actuelle à la grille
        for (int i = 0; i < currentPageBlogs.size(); i++) {
            Blog blog = currentPageBlogs.get(i);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cardblogs.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = fxmlLoader.load();
                Cardblogs controller = fxmlLoader.getController();
                controller.setData(blog, myListenerb);
                grid.getChildren().add(anchorPane);
                // Définir la position de l'élément dans la grille
                GridPane.setRowIndex(anchorPane, i / 3); // Par exemple, 3 éléments par ligne
                GridPane.setColumnIndex(anchorPane, i % 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mettez à jour les informations de pagination (par exemple, le numéro de page actuelle)
        currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1));
        totalPagesLabel.setText("sur " + pagination.getPageCount());
    }


    public void navtochat(MouseEvent event) {

        System.out.println("let's chat");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/ChatBot.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show(); }
}