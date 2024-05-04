package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Voyage;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class ServiceVoyage implements IService<Voyage>{

    private Connection cnx;
    public ServiceVoyage(){ cnx = MyDataBase.getInstance().getConnection();}
    public Voyage saisie() {
        int id, prix;
        String nom, destination, description, image1, type;
        LocalDateTime date_debut, date_fin;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Donner l'ID :");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Nom :");
        nom = scanner.nextLine();
        System.out.println("Destination :");
        destination = scanner.nextLine();
        System.out.println("Description :");
        description = scanner.nextLine();
        System.out.println("Prix :");
        prix = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Type :");
        type = scanner.nextLine();

        date_debut = LocalDateTime.of(2002, 2, 25, 0, 0, 0);
        date_fin = LocalDateTime.of(2003, 3, 25, 0, 0, 0);
        image1 = "image path...";

        return new Voyage(id, nom, prix, destination, description, image1, date_debut, date_fin, type);
    }

    @Override
    public void add(Voyage voyage) {
        String qry = "INSERT INTO `voyage`(`nom`, `prix`, `destination`, `description`, `type`, `date_debut`, `date_fin`, `image1`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setString(1, voyage.getNom());
            pstm.setInt(2, voyage.getPrix());
            pstm.setString(3, voyage.getDestination());
            pstm.setString(4, voyage.getDescription());
            pstm.setString(5, voyage.getType());
            pstm.setTimestamp(6, Timestamp.valueOf(voyage.getDate_debut()));
            pstm.setTimestamp(7, Timestamp.valueOf(voyage.getDate_fin()));
            pstm.setString(8, voyage.getImage1());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void update(Voyage voyage){
        String qry = "UPDATE `voyage` SET `nom`=?, `prix`=?, `destination`=?, `description`=?, `type`=?, `date_debut`=?, `date_fin`=?, `image1`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setString(1, voyage.getNom());
            pstm.setInt(2, voyage.getPrix());
            pstm.setString(3, voyage.getDestination());
            pstm.setString(4, voyage.getDescription());
            pstm.setString(5, voyage.getType());
            pstm.setTimestamp(6, Timestamp.valueOf(voyage.getDate_debut()));
            pstm.setTimestamp(7, Timestamp.valueOf(voyage.getDate_fin()));
            pstm.setString(8, voyage.getImage1());
            pstm.setInt(9, voyage.getId());
            System.out.println("Voyage modifié !");

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public boolean delete(int voyageId){
        String qry = "DELETE FROM `voyage` WHERE `id` =?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, voyageId);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le voyage à ID " + voyageId + " à été supprimé avec succès.");
                return true;
            } else {
                System.out.println("La suppression a échoué.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("La suppression a échoué: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Voyage> getAll() {
        //1-req SELECT
        //2-recuperation de la base de donné remplissage dans Array
        //3-retour du tableau done
        ArrayList<Voyage> voyages = new ArrayList<>();
        String qry ="SELECT * FROM `voyage`";
        try {
            Statement stm = cnx.createStatement();

            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Voyage v = new Voyage();
                v.setId(rs.getInt(1));
                v.setNom(rs.getString("nom"));
                v.setPrix(rs.getInt(3));
                v.setDestination(rs.getString("destination"));
                v.setDescription(rs.getString("description"));
                v.setType(rs.getString("type"));
                v.setImage1(rs.getString("image1"));
                v.setDate_debut(rs.getTimestamp("date_debut").toLocalDateTime());
                v.setDate_fin(rs.getTimestamp("date_fin").toLocalDateTime());
                voyages.add(v);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return voyages;
    }

    @Override
    public void displayAll(ArrayList<Voyage> items) {
        //ArrayList<Voyage> voyages = this.getAll();
        for (Voyage v : items) {
            System.out.println("ID: " + v.getId());
            System.out.println("Nom: " + v.getNom());
            System.out.println("Prix: " + v.getPrix());
            System.out.println("Destination: " + v.getDestination());
            System.out.println("Description: " + v.getDescription());
            System.out.println("Type: " + v.getType());
            System.out.println("Image: " + v.getImage1());
            System.out.println("Date début: " + v.getDate_debut());
            System.out.println("Date fin: " + v.getDate_fin());
            System.out.println();
        }
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
    public ArrayList<Voyage> findByNom_ouDestination(String nom) throws ItemNotFoundException {
        ArrayList<Voyage> liste_des_voyages = this.getAll();
        ArrayList<Voyage> found_items = new ArrayList<>();
        Iterator<Voyage> itr = liste_des_voyages.iterator();
        while (itr.hasNext()) {
            Voyage voyage = itr.next();
            if (voyage.getNom().toLowerCase().contains(nom) || voyage.getDestination().toLowerCase().contains(nom)) {
                //System.out.println("Voyage trouvé");
                found_items.add(voyage);
            }
        }
        if (found_items.isEmpty()) {
            throw new ItemNotFoundException("Le voyage du nom ou destination " + nom + " n'existe pas.");
        }
        return found_items;
    }
    public ArrayList<Voyage> trierVoyagePar_type_nom_destination(){
        Comparator<Voyage> byType = new Comparator<Voyage>() {
            @Override
            public int compare(Voyage o1, Voyage o2) {return o1.getType().compareTo(o2.getType());}
        };
        Comparator<Voyage> byName = Comparator.comparing(Voyage::getNom);
        Comparator<Voyage> byDestination = Comparator.comparing(Voyage::getDestination);
        ArrayList<Voyage> voyages = this.getAll();

        voyages.sort(byType.thenComparing(byName).thenComparing(byDestination));
        return voyages;
    }
    public ArrayList<Voyage> trierVoyageParNom() {
        Comparator<Voyage> byName = Comparator.comparing(Voyage::getNom);
        ArrayList<Voyage> voyages = this.getAll();
        voyages.sort(byName);
        return voyages;
    }
    public ArrayList<Voyage> trierVoyageParType() {
        Comparator<Voyage> byType = Comparator.comparing(Voyage::getType);
        ArrayList<Voyage> voyages = this.getAll();
        voyages.sort(byType);
        return voyages;
    }
    public ArrayList<Voyage> trierVoyageParPrix() {
        Comparator<Voyage> byPrix = Comparator.comparing(Voyage::getPrix);
        ArrayList<Voyage> voyages = this.getAll();
        voyages.sort(byPrix);
        return voyages;
    }


}
