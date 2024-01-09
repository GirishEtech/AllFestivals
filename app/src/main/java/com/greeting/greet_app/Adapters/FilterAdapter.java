package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greeting.greet_app.Model.FilterData;
import com.greeting.greet_app.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ImageViewHolder> {
    private final List<FilterData> imageList;
    FilterClickListner listner;

    public FilterAdapter(List<FilterData> imageList, FilterClickListner listner) {
        this.imageList = imageList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position).getResource());
        holder.bgview.setBackgroundColor(imageList.get(position).getColor());
        holder.itemView.setOnClickListener(v -> {
            listner.onFilterClick(imageList.get(position).getColor());
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View bgview;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            bgview = itemView.findViewById(R.id.bgview);
        }
    }

    public interface FilterClickListner {
        public void onFilterClick(int color);
    }
}

