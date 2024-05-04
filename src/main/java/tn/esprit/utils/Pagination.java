package tn.esprit.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Blog;

public class Pagination {
    private ObservableList<Blog> allBlogs;
    private int itemsPerPage;
    private int currentPageIndex;

    public Pagination(ObservableList<Blog> allBlogs, int itemsPerPage) {
        this.allBlogs = allBlogs;
        this.itemsPerPage = itemsPerPage;
        this.currentPageIndex = 0;
    }

    public ObservableList<Blog> getCurrentPageBlogs() {
        int fromIndex = currentPageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allBlogs.size());
        return FXCollections.observableArrayList(allBlogs.subList(fromIndex, toIndex));
    }

    public void nextPage() {
        int pageCount = getPageCount();
        if (currentPageIndex < pageCount - 1) {
            currentPageIndex++;
        }
    }

    public void previousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
        }
    }

    public int getPageCount() {
        return (int) Math.ceil((double) allBlogs.size() / itemsPerPage);
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
}
