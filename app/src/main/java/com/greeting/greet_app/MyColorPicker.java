package com.greeting.greet_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.skydoves.colorpickerview.ColorPickerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.github.antonpopoff.colorwheel.ColorWheel;
import com.rtugeek.android.colorseekbar.AlphaSeekBar;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;

import java.util.Objects;

import top.defaults.colorpicker.AlphaSliderView;
import top.defaults.colorpicker.ColorWheelView;

public class MyColorPicker {

    TextView txtTemp;
    SimpleColorListner listner;
    Button btnCencel, btnDone;

    int colors, progress;

    int Alpha = -2000;
    Context context;
    View testBgColor;
    ColorPickerView mainColor;
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


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        testBgColor = dialog.findViewById(R.id.tempTest);
        //txtTemp = dialog.findViewById(R.id.Sample1);
        mainColor = dialog.findViewById(R.id.MyColorPicker);
        btnCencel = dialog.findViewById(R.id.btnCencel);
        btnDone = dialog.findViewById(R.id.btnDone);
        //txtTemp.setTextColor(test);
        testBgColor.setBackground(drawable);
        mainColor.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                if (fromUser) {
                    colors = color;
                    drawable.setColor(colors);
                    testBgColor.setBackgroundColor(color);
                }
            }
        });
        btnCencel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        btnDone.setOnClickListener(view1 -> {
            dialog.dismiss();
            if (Type == "TEXT") {
                listner.setTextStickerColor(colors);
            } else {
                listner.setDrawable(drawable);
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
    public void setDrawable(GradientDrawable drawable);

    public void setTextStickerColor(int color);

}
