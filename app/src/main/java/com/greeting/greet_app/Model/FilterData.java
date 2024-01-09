package com.greeting.greet_app.Model;

public class FilterData {
    public int getColor() {
        return color;
    }

    public FilterData(int color, int resource) {
        this.color = color;
        this.resource = resource;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    int color, resource;
}
