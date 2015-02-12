package project.se.model;

/**
 * Created by wiwat on 2/12/2015.
 */
public class Category {
    int id;
    String cat_name;

    public Category(int id, String cat_name) {
        this.id = id;
        this.cat_name = cat_name;
    }

    public int getId() {
        return id;
    }

    public String getCat_name() {
        return cat_name;
    }
}
