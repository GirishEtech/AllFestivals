package com.greeting.greet_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.rtugeek.android.colorseekbar.AlphaSeekBar;

import java.util.Objects;

import top.defaults.colorpicker.ColorWheelView;

public class MyColorPicker {

    TextView txtTemp;
    AlphaSeekBar seekBar;
    SimpleColorListner listner;
    Button btnCencel, btnDone;

    View view;
    int colors,progress;

    int Alpha = -2000;
    Context context;
    FrameLayout testBgColor;
    ColorWheelView mainColor;
    Activity activity;
    Dialog dialog;

    String Type;
    GradientDrawable drawable;


    public MyColorPicker(Activity activity, SimpleColorListner listner, Context context, String Type) {
        this.context = context;
        this.activity = activity;
        this.listner = listner;
        this.Type = Type;
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
        View v = activity.getLayoutInflater().inflate(R.layout.color_picker_simple, null);
        dialog.setContentView(v);
        drawable = new GradientDrawable();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        testBgColor = dialog.findViewById(R.id.Sample);
        txtTemp = dialog.findViewById(R.id.Sample1);
        seekBar = dialog.findViewById(R.id.MyAlphaSeekbar);
        mainColor = dialog.findViewById(R.id.MyColorPicker);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        if (Objects.equals(Type, "TEXT")) {
            txtTemp.setVisibility(View.VISIBLE);
            testBgColor.setBackgroundColor(context.getColor(android.R.color.transparent));
            Toast.makeText(context, "text", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "bg", Toast.LENGTH_SHORT).show();
            txtTemp.setVisibility(View.GONE);
            colors = Color.argb(Alpha, Color.red(colors), Color.green(colors), Color.blue(colors));
            drawable.setColor(colors);
            testBgColor.setBackground(drawable);
            testBgColor.setVisibility(View.VISIBLE);
        }
        mainColor.subscribe((color, fromUser, shouldPropagate) -> {
            if (fromUser) {
                if (Alpha == -2000) {
                    colors = color;
                    drawable.setColor(colors);
                } else {
                    colors = Color.argb(Alpha, Color.red(color), Color.green(color), Color.blue(color));
                    drawable.setColor(colors);
                    txtTemp.setTextColor(colors);
                }
                if (Type == "TEXT") {
                    txtTemp.setTextColor(colors);
                } else {
                    testBgColor.setBackground(drawable);
                }
            }
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            dialog.dismiss();
            if (Type == "TEXT") {
                listner.setTextStickerColor(colors,Alpha,progress);
            } else {
                listner.setDrawable(drawable,Alpha,progress);
            }
        });
        seekBar.setOnAlphaChangeListener((progress, alpha) -> {
            colors = Color.argb(alpha, Color.red(colors), Color.green(colors), Color.blue(colors));
            drawable.setColor(colors);
            this.progress = progress;
            Alpha = alpha;
            if (Type == "TEXT") {
                txtTemp.setTextColor(colors);
                txtTemp.setTextColor(drawable.getColor());
            } else {
                testBgColor.setBackground(drawable);
            }
        });
        dialog.show();
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

interface SimpleColorListner {
    public void setDrawable(GradientDrawable drawable,int Alpha,int progress);

    public void setTextStickerColor(int color,int Alpha,int progress);

}
