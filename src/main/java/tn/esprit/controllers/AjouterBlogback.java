package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import tn.esprit.models.Blog;
import tn.esprit.services.BlogServiceback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
int id ;

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
    private Object SwingFXUtils;

    @FXML
  
        private void addphoto(ActionEvent event) throws IOException {
            FileChooser fc = new FileChooser();
            fc.setTitle("Ajouter une Image");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"));
            File f = fc.showOpenDialog(null);
            String DBPath = "C:\\\\\\\\xampp\\\\\\\\htdocs\\\\\\\\Version-Integre\\\\\\\\public\\\\\\\\uploads\\\\\\\\"+f.getName();
            String i = f.getName();
            Blog.setImageP(i);
        if (f != null) {
            // Charger l'image à partir du fichier
            Image image = new Image(f.toURI().toString());

            // Afficher l'image dans votre ImageView
            imagep.setImage(image);

            // Lire le contenu de l'image pour le traitement ultérieur si nécessaire
            FileInputStream fin = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fin.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                byte[] post_image = bos.toByteArray();
            }
        }


    }

    @FXML
    private TableView<Blog> tftableview;
   // @Override
    public void initialize(URL url, ResourceBundle rb) {
showblog();
        //BlogService service = new BlogService();
      //  ObservableList<Blog> list = service.getAll();
       // System.out.print(list);
       // coid.setCellValueFactory(new PropertyValueFactory<>("id"));
       // coltitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
       // colcontenu.setCellValueFactory(new PropertyValueFactory<>("content"));
        //colimage.setCellValueFactory(new PropertyValueFactory<>("imageb"));
       // tftableview.setItems(list);
    }

        @FXML
    void ajouterblog(ActionEvent event) {
    BlogServiceback service=new BlogServiceback();
        String titreText = titre.getText();
        String descriptionText = description.getText();
        String imagebText = imageb.getText();
            if (titreText.isEmpty() || descriptionText.isEmpty() || imagebText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez remplir tous les champs obligatoires.");
                alert.show();
                return; // Sortie de la méthode si les champs obligatoires ne sont pas remplis
            }
            if (descriptionText.length() < 5) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("La description doit avoir au moins 5 caractères.");
                alert.show();
                return; // Sortie de la méthode si la description est trop courte
            }
        Date currentDate = new Date();
        Blog blog = new Blog(0, titre.getText(), description.getText(), imageb.getText(),currentDate,true);
        blog.setFavoris(false);
        boolean estFavoris = blog.isFavoris();
        BlogServiceback BlogServiceback = new BlogServiceback();
        BlogServiceback.add(blog);
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("blog ajoute avec succes");
        alert.show();
            service.update(blog);
            showblog();


            //FXMLLoader loader= new FXMLLoader(getClass().getResource("/afficherblog.fxml"));
       // try {
          //  Parent root =loader.load();
       // Afficherblog Afficherblog=loader.getController();
       // Afficherblog.setTitre(titre.getText());
       // Afficherblog.setContenu(description.getText());
       // Afficherblog.setImageb(imageb);
       // titre.getScene().setRoot(root);
        //} catch (IOException e) {throw new RuntimeException(e);}
       // refreshtable();
    }

    public void showblog(){
        BlogServiceback blogServiceback = new BlogServiceback();
        ObservableList<Blog> list= blogServiceback.getAll();
        table.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<Blog , Integer>("id"));
        coltitre.setCellValueFactory(new PropertyValueFactory<Blog , String>("titre"));
        colcontenu.setCellValueFactory(new PropertyValueFactory<Blog , String>("content"));
        colimageb.setCellValueFactory(new PropertyValueFactory<Blog , String>("imageb"));
        coldatde.setCellValueFactory(new PropertyValueFactory<Blog , Date>("date"));
}
    @FXML
    void getData(MouseEvent event) {
        Blog blog= table.getSelectionModel().getSelectedItem();
       id = blog.getId();
       titre.setText(blog.getTitre());
       description.setText(blog.getContent());
       imageb.setText(blog.getImageb());

       btnadd.setDisable(true);
    }
    @FXML
    void delete(ActionEvent event) {
        Blog b=table.getSelectionModel().getSelectedItem();
        BlogServiceback service=new BlogServiceback();
       // service.delete(b.getId());

      service.delete(b);
        showblog();

    }

    @FXML
    void update(ActionEvent event) {
     Blog b =table.getSelectionModel().getSelectedItem();
        BlogServiceback blogServiceback = new BlogServiceback();
        b.setTitre(titre.getText());
        b.setContent(description.getText());
        b.setImageb(imageb.getText());
        blogServiceback.update(b);
         showblog();



    }


    @FXML
    void initialize(){

    }
    @FXML
    void showDetails(ActionEvent event) {
        Blog selectedBlog = table.getSelectionModel().getSelectedItem();

        if (selectedBlog != null) {
            // Afficher le contenu du blog dans une boîte de dialogue
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails du Blog");
            alert.setHeaderText(selectedBlog.getTitre());
            alert.setContentText(selectedBlog.getContent());

            alert.showAndWait();
        } else {
            // Aucun blog sélectionné, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun blog sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un blog pour afficher les détails.");
            alert.showAndWait();
        }
    }
}
