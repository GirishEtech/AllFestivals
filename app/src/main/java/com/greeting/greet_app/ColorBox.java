package com.greeting.greet_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import kotlin.jvm.functions.FunctionN;
import top.defaults.colorpicker.ColorWheelView;

public class ColorBox {

    ColorListner listner;
    GradientDrawable drawable;
    Button btnCencel, btnDone;
    int[] colors;

    GradientDrawable FinalDrawable;
    TextView txtfirst, txtsecond;
    View outputTest;
    Context context;
    View testBgColor1, testBgColor2;
    ColorPickerView firstColor, SecondColor;
    Activity activity;
    Dialog dialog;


    private String colorSettingType;

    public ColorBox(Activity activity, ColorListner listner, Context context, String colorSettingType) {
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
        testBgColor1 = (android.view.View) dialog.findViewById(R.id.tempTest1);
        testBgColor2 = dialog.findViewById(R.id.tempTest2);
        outputTest = dialog.findViewById(R.id.outputTest);
        firstColor = dialog.findViewById(R.id.firstColor);
        SecondColor = dialog.findViewById(R.id.SecondColor);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        colors = new int[]{1, 2};
        drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        FinalDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        testBgColor1.setOnClickListener(v1 -> {
            firstColor.setVisibility(View.VISIBLE);
            SecondColor.setVisibility(View.INVISIBLE);
        });
        testBgColor2.setOnClickListener(v1 -> {
            firstColor.setVisibility(View.INVISIBLE);
            SecondColor.setVisibility(View.VISIBLE);
        });
        firstColor.setColorListener((ColorListener) (color, fromUser) -> {
            if (fromUser) {
                colors[0] = color;
                drawable.setColor(color);
                testBgColor1.setBackground(drawable);
                FinalDrawable.setColors(colors);
                outputTest.setBackground(FinalDrawable);
            }
        });
        SecondColor.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                if (fromUser) {
                    colors[1] = color;
                    drawable.setColor(color);
                    testBgColor2.setBackground(drawable);
                    FinalDrawable.setColors(colors);
                    outputTest.setBackground(FinalDrawable);
                }
            }
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            dialog.dismiss();
            if (colorSettingType.equals("GRADIANTBACK")) {
                listner.setBackgroundColor(FinalDrawable);
            } else {
                listner.setTxtColor(colors);
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
    public void setTxtColor(int[] colors);

    public void setBackgroundColor(GradientDrawable drawable);
}
