package com.greeting.greet_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.cardview.widget.CardView;

import com.madrapps.pikolo.RGBColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class ColorBox {

    ColorListner listner;
    GradientDrawable drawable;
    Button btnCencel, btnDone;
    int[] colors;
    View view;

    CardView testBgColor;
    RGBColorPicker firstColor, SecondColor;
    Activity activity;
    Dialog dialog;

    public ColorBox(Activity activity, ColorListner listner) {
        this.activity = activity;
        this.listner = listner;
    }


    public void showDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        dialog = new Dialog(activity);
        dialog.setCancelable(false);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setTitle("Choose Gradiant Color");
        dialog.setContentView(R.layout.color_picker);
        testBgColor = dialog.findViewById(R.id.testBgColor);
        firstColor = dialog.findViewById(R.id.firstColor);
        SecondColor = dialog.findViewById(R.id.secondColor);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        colors = new int[]{1, 2};
        drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        firstColor.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                colors[0] = color;
                drawable.setColors(colors);
                testBgColor.setBackground(drawable);
            }
        });
        SecondColor.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                colors[1] = color;
                drawable.setColors(colors);
                testBgColor.setBackground(drawable);
            }
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            listner.setDrawable(drawable);
            dialog.dismiss();
        });
        dialog.show();
    }


    public void hideDialog() {
        dialog.dismiss();
    }

    public boolean isShowing() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                return true;
            }
            return false;
        }
        return false;
    }

}

interface ColorListner {
    public void setDrawable(GradientDrawable drawable);
}