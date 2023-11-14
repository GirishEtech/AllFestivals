package com.greeting.greet_app;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.greeting.greet_app.Model.CategoryModel;

public class Utils {
    public static final String CAT_TYPE = "CATEGORYTYPE";
    public static final String TAB_NAME = "tab_name";
    public static Integer Span_Count = 4;
    public static Context context;
    public static CategoryModel categoryModel;
    public static String Cards = "Cards";
    public static String Clients = "Clients";
    public static String Games = "Games";
    public static String Sheet = "Sheet";
    public static String KeyBoard_Default = "Default";
    public static String KeyBoard_Custom = "Custom";
    public static String KeyBoard_ = "";
    public static String Games_Draw = "Games_Draw";
    public static String Repeat_List_Sheet = "Repeat_List_Sheet";
    public static String Account = "Account";
    public static String Declare = "Declare";
    public static String Comission = "Comission";
    public static String QR_ON = "QR_ON";
    public static String QR_OFF = "QR_OFF";
    public static String QR_PREF = "QR_PREF";
    public static String QR_PREF_KEY = "QR_PREF_KEY";
    public static String All_Festivals = "All Festivals";
    public static String Daily_Wishes = "Daily Wishes";
    public static String Family_Wishes = "Family Wishes";
    public static String Gifs = "Gifs";
    public static String Frames = "Frames";
    public static String Stickers = "Stickers";
    public static String Quotes = "Quotes";
    public static String Special_Events = "Special Events";
    public static String Categories = "Categories";
    public static String Family_Details = "Family_Details";
    public static String Best_Quotes = "Best Quotes";
    public static String UserSavedItems = "UserSavedItems";
    public static String Background = "Background";


    public static void setToast(Context activity, String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

    }
    public static String convertColorToHex(Drawable drawable) {
        if (drawable instanceof ColorDrawable) {
            // If the Drawable is a ColorDrawable, get its color
            int color = ((ColorDrawable) drawable).getColor();

            // Convert the color to hexadecimal
            return String.format("#%06X", (0xFFFFFF & color));
        }

        // Return a default color or handle the case where the Drawable is not a ColorDrawable
        return "#000000"; // Default to black
    }

}
