package com.greeting.greet_app;

import android.app.Activity;
import android.app.Dialog;

import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class ViewDialog {

    Activity activity;
    Dialog dialog;
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        if (dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
        }
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(activity).asGif()
                .load(R.raw.loading)
                .placeholder(R.drawable.loadgid)
              //  .centerCrop()
                .into(gifImageView);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
    public boolean isShowing(){
        if (dialog!=null){
            if (dialog.isShowing()){
                return true;
            }
            return false;
        }
        return false;
    }

}