package tn.esprit.interfacesb;

import tn.esprit.models.Blog;

import java.util.ArrayList;

public interface IService<T> {

    void add (T t );
    ArrayList<T> getAll();

    void displayAll(ArrayList<Blog> items);

    void update(T t );
    boolean delete (T t);
//findby..

    //getby ...

}
