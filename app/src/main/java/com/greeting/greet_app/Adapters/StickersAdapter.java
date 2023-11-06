package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.greeting.greet_app.R;

import java.net.URI;
import java.util.ArrayList;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.MyViewHolder> {
    private AppCompatActivity activity;
    ArrayList<String> data = new ArrayList();
    private OnClickListener onClickListener;

    private LayoutInflater inflater;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView loadingImage, MainImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            loadingImage = itemView.findViewById(R.id.loadingImage);
            MainImage = itemView.findViewById(R.id.MainImage);
        }

    }

    public StickersAdapter(AppCompatActivity activity, ArrayList<String> data) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.data = data;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.inflater.inflate(R.layout.sticker_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String m = data.get(position);
        View context = holder.itemView;
        Log.i("TEST URL", "onBindViewHolder: URI IMAGES" + m);

        Glide.with(context).asGif().load(R.raw.loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.loadingImage);
        Glide.with(context).asBitmap().load(m).diskCacheStrategy(DiskCacheStrategy.ALL).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.loadingImage.setVisibility(View.GONE);
                holder.MainImage.setVisibility(View.VISIBLE);
                holder.MainImage.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public int getItemCount() {
        return this.data.size();
    }
}