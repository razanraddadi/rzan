package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tn.esprit.models.Blog;
import tn.esprit.services.BlogServiceback;
import java.io.IOException;
import tn.esprit.utils.MyDataBase;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterBlogback implements Initializable {

    @FXML
    private TextField description;

    @FXML
    private TextField imageb;

    @FXML
    private TextField titre;

    @FXML
    private TableColumn<Blog, String> colcontenu;

    @FXML
    private TableColumn<Blog, Integer> colid;

    @FXML
    private TableColumn<Blog, String> colimageb;

    @FXML
    private TableColumn<Blog, String> coltitre;

    @FXML
    private TableView<Blog> table;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnupdate;

    @FXML
    private Button btnadd;

    @FXML
    private ImageView imagep;

    @FXML
    private TableColumn<Blog, Date> coldatde;

    @FXML
    private TableColumn<?, ?> details;

    @FXML
    private Button detailsButton;
   @FXML
    private int id;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showblog();
    }

    @FXML
    void ajouterblog(ActionEvent event) {
        BlogServiceback service = new BlogServiceback();
        String titreText = titre.getText();
        String descriptionText = description.getText();
        String imagebText = imageb.getText();
        if (titreText.isEmpty() || descriptionText.isEmpty() || imagebText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.show();
            return;
        }
        if (descriptionText.length() < 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("La description doit avoir au moins 5 caractères.");
            alert.show();
            return;
        }
        Date currentDate = new Date();
        Blog blog = new Blog(0, titre.getText(), description.getText(), imageb.getText(), currentDate, true);
        blog.setFavoris(false);
        service.add(blog);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Blog ajouté avec succès");
        alert.show();
        showblog(); // Rafraîchir la TableView après l'ajout du blog

        refreshTableView(); }
    @FXML
    public void showblog() {
        BlogServiceback blogServiceback = new BlogServiceback();
        ObservableList<Blog> list = blogServiceback.getAll();
        table.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coltitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colcontenu.setCellValueFactory(new PropertyValueFactory<>("content"));
        colimageb.setCellValueFactory(new PropertyValueFactory<>("imageb"));
        coldatde.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @FXML
    void getData(MouseEvent event) {
        Blog blog = table.getSelectionModel().getSelectedItem();
        if (blog != null) {
            id = blog.getId();
            titre.setText(blog.getTitre());
            description.setText(blog.getContent());
            imageb.setText(blog.getImageb());
            // Mettre à jour le bouton "Ajouter" pour le désactiver pendant la mise à jour
            btnadd.setDisable(true);
        } else {
            // Réactiver le bouton "Ajouter" si aucun blog n'est sélectionné
            btnadd.setDisable(false);
        }
    }


    @FXML
    void delete(ActionEvent event) {
        Blog blog = table.getSelectionModel().getSelectedItem();
        BlogServiceback service = new BlogServiceback();
        service.delete(blog.getId());
        showblog(); // Rafraîchir la TableView après la suppression du blog
        refreshTableView();
    }

    @FXML
    void update(ActionEvent event) {
        Blog blog = table.getSelectionModel().getSelectedItem();
        blog.setTitre(titre.getText());
        blog.setContent(description.getText());
        blog.setImageb(imageb.getText());
        BlogServiceback blogServiceback = new BlogServiceback();
        blogServiceback.update(blog);
        showblog(); // Rafraîchir la TableView après la mise à jour du blog

        refreshTableView(); }
    @FXML
    private void refreshTableView() {
        BlogServiceback blogServiceback = new BlogServiceback();
        ObservableList<Blog> list = blogServiceback.getAll();
        table.setItems(list);
    }
    @FXML
    void showDetails(ActionEvent event) {
        Blog selectedBlog = table.getSelectionModel().getSelectedItem();
        if (selectedBlog != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails du Blog");
            alert.setHeaderText(selectedBlog.getTitre());
            alert.setContentText(selectedBlog.getContent());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun blog sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un blog pour afficher les détails.");
            alert.showAndWait();
        }
    }
}
