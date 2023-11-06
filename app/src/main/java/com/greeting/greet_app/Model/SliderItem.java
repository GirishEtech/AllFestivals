package com.greeting.greet_app.Model;

import java.util.ArrayList;

public class SliderItem {

    private String description;
    private String imageUrl;
    int Image;
    ArrayList<CategoryModel> categoryModelArrayList=new ArrayList<>();

    public SliderItem(ArrayList<CategoryModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
    }

    public ArrayList<CategoryModel> getCategoryModelArrayList() {
        return categoryModelArrayList;
    }

    public void setCategoryModelArrayList(ArrayList<CategoryModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SliderItem(int image) {
        Image = image;
    }
}