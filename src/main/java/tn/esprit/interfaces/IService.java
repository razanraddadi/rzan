package tn.esprit.interfaces;

import tn.esprit.models.Blog;
import tn.esprit.services.ServiceVoyage;

import java.util.ArrayList;

public interface IService<T> {
    void add (T t );
    ArrayList<T> getAll();
    void displayAll(ArrayList<T> items);

    void update(T t );
    boolean delete (int id);

}
