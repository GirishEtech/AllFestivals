package com.greeting.greet_app.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    String Name;
    String Path;
    String MainCategoryName;
    int Image;
    String ImageLink;
    int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getMainCategoryName() {
        return MainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        MainCategoryName = mainCategoryName;
    }

    public CategoryModel(String name, int image) {
        Name = name;
        Image = image;
    }

    public CategoryModel() {
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
