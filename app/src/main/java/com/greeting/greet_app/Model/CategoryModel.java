package com.greeting.greet_app.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    String Name;

    public String getWishType() {
        switch (wishType) {
            case "anniversary": {
                return "Anniversary";
            }
            case "good morning": {
                return "Good Morning";
            }
            case "birthday": {
                return "Birthday";
            }
            case "love": {
                return "Love";
            }
            case "good night": {
                return "Good Night";
            }
            case "retirement": {
                return "Retirement";
            }
            default:
                return "";
        }
    }

    public void setWishType(String wishType) {
        this.wishType = wishType;
    }

    String wishType;
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
