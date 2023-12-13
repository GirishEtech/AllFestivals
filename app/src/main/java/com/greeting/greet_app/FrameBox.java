package com.greeting.greet_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.greeting.greet_app.Adapters.Frames_Adapters;

import java.util.ArrayList;

import top.defaults.colorpicker.ColorWheelView;

public class FrameBox {

    RecyclerView frameList;
    ImageView ic_crossBtn;
    Context context;
    Activity activity;
    Dialog dialog;

    Frames_Adapters adapters;

    public FrameBox(Activity activity, Context context, Frames_Adapters adapters) {
        this.context = context;
        this.activity = activity;
        this.adapters = adapters;
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
        View v = activity.getLayoutInflater().inflate(R.layout.add_frame_dialog, null);
        dialog.setContentView(v);
        ic_crossBtn = v.findViewById(R.id.ic_Cross_btn);
        frameList = v.findViewById(R.id.frameList);
        frameList.setAdapter(adapters);
        ic_crossBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

