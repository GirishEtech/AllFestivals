package com.greeting.greet_app.Model;

public class SimpleColor {
    int colorToUse,color;

    public SimpleColor(int colorToUse) {
        this.colorToUse = colorToUse;
    }

    public SimpleColor() {
    }

    public int getColorToUse() {
        return colorToUse;
    }

    public void setColorToUse(int colorToUse) {
        this.colorToUse = colorToUse;
    }


    public void setColor(int color1){
        color = color1;
    }
    public int getColor(){
        return color;
    }
}
