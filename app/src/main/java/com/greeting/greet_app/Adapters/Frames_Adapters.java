package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.PerformanceTracker;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.greeting.greet_app.CreateFramePhoto_Activity;
import com.greeting.greet_app.Preview_Activity;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Frames_Adapters extends RecyclerView.Adapter<Frames_Adapters.StudentsViewHolder> {
    Context context;
    ArrayList<String> list;
    private LayoutInflater inflater;

    private frameListner listener;
    String Type = "";

    public Frames_Adapters(Context context, ArrayList<String> clientModelArrayList, String type, frameListner listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.Type = type;
        this.list = clientModelArrayList;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frame_item, parent, false);
        StudentsViewHolder viewHolder = new StudentsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentsViewHolder holder, int position) {
        if (list.size() > 0) {
            //   holder.setIsRecyclable(false);
            String model = list.get(position);
            Glide.with(context).load(R.raw.loading).into(holder.ic_pro);
            new Handler().postDelayed(() -> {
                holder.ic_pro.setVisibility(View.GONE);
                holder.ic_img.setVisibility(View.VISIBLE);
                Glide.with(context).asBitmap().load(model).into(holder.ic_img);
            }, 300);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.setFrame(model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StudentsViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView ic_img, ic_pro;

        public StudentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ic_img = itemView.findViewById(R.id.ic_img);
            ic_pro = itemView.findViewById(R.id.ic_pro);
        }
    }
    public static interface frameListner {
        void setFrame(String frame);
    }

}
