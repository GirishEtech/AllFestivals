package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.greeting.greet_app.Model.SliderItems;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Objects;

public class SlidersAdapter extends RecyclerView.Adapter<SlidersAdapter.SliderViewHolder> {
    private List<String> sliderItems;

    private String imageType;
    private ViewPager2 viewPager2;
    Context context;

    public SlidersAdapter(List<String> sliderItems, ViewPager2 viewPager2, Context context, String imageType) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = context;
        this.imageType = imageType;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        Log.d("help", "ygh" + sliderItems.toString());
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(String sliderItems) {
            Glide.with(context).load(sliderItems).into(imageView);
        }
    }

}