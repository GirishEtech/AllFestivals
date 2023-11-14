package com.greeting.greet_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.madrapps.pikolo.RGBColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class ColorBox {

    ColorListner listner;
    GradientDrawable drawable;
    Button btnCencel, btnDone;
    int[] colors;
    TextView txtfirst,txtsecond;
    View view;

    CardView testBgColor;
    CardView firstColor, SecondColor;
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = activity.getLayoutInflater().inflate(R.layout.color_picker, null);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtfirst = dialog.findViewById(R.id.textview_first);
        txtsecond = dialog.findViewById(R.id.textview_second);
        testBgColor = dialog.findViewById(R.id.outPutTest);
        firstColor = dialog.findViewById(R.id.firstColor);
        SecondColor = dialog.findViewById(R.id.SecondColor);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        colors = new int[]{1, 2};
        drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        firstColor.setOnClickListener(view1 -> {
            new ColorPickerDialog.Builder(activity)
                    .setTitle("Select First Gradiant")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton("OK", new ColorEnvelopeListener() {
                        @Override
                        public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                            colors[0] = envelope.getColor();
                            drawable.setColors(colors);
                            testBgColor.setBackground(drawable);
                            txtfirst.setText(convertColorToHex(envelope.getColor()));
                            firstColor.setCardBackgroundColor(envelope.getColor());
                        }
                    })
                    .setNegativeButton("CENCEL",(dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .attachBrightnessSlideBar(true)
                    .setBottomSpace(12)
                    .attachAlphaSlideBar(true)
                    .show();
        });
        SecondColor.setOnClickListener(view1 -> {
            new ColorPickerDialog.Builder(activity)
                    .setTitle("Select First Gradiant")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton("OK", new ColorEnvelopeListener() {
                        @Override
                        public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                            colors[1]=envelope.getColor();
                            drawable.setColors(colors);
                            txtsecond.setText(convertColorToHex(envelope.getColor()));
                            testBgColor.setBackground(drawable);
                            SecondColor.setCardBackgroundColor(envelope.getColor());
                        }
                    })
                    .setNegativeButton("CENCEL",(dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .attachBrightnessSlideBar(true)
                    .setBottomSpace(12)
                    .attachAlphaSlideBar(true)
                    .show();
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            listner.setDrawable(colors);
            dialog.dismiss();
            listner.setDrawable(colors);

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
}