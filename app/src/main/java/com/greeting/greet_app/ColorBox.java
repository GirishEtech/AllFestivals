package com.greeting.greet_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;

import com.madrapps.pikolo.RGBColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import top.defaults.colorpicker.ColorWheelView;

public class ColorBox {

    ColorListner listner;
    GradientDrawable drawable;
    Button btnCencel, btnDone;
    int[] colors;
    TextView txtfirst, txtsecond;
    View view;
    Context context;
    CardView testBgColor;
    ColorWheelView firstColor, SecondColor;
    Activity activity;
    Dialog dialog;



    private String colorSettingType;

    public ColorBox(Activity activity, ColorListner listner, Context context,String colorSettingType) {
        this.context = context;
        this.activity = activity;
        this.listner = listner;
        this.colorSettingType = colorSettingType;
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
        View v = activity.getLayoutInflater().inflate(R.layout.color_picker, null);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        testBgColor = dialog.findViewById(R.id.outPutTest);
        firstColor = dialog.findViewById(R.id.firstcolor);
        SecondColor = dialog.findViewById(R.id.secondcolor);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        colors = new int[]{1, 2};
        drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);

        firstColor.subscribe((color, fromUser, shouldPropagate) -> {
            if (fromUser){
                colors[0] =color;
                drawable.setColors(colors);
                testBgColor.setBackground(drawable);
            }
        });

        SecondColor.subscribe((color, fromUser, shouldPropagate) -> {
            if (fromUser){
                colors[1] =color;
                drawable.setColors(colors);
                testBgColor.setBackground(drawable);
            }
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            dialog.dismiss();
            if (colorSettingType.equals("GRADIANT")){
                listner.setBackground(drawable);
            }
            else {
                listner.setDrawable(colors);
            }
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

    public String convertColorToHex(int color) {
        return String.format("#%06X", 0xFFFFFF & color);
    }
}

interface ColorListner {
    public void setDrawable(int colors[]);

    public void setBackground(GradientDrawable drawable);
}
