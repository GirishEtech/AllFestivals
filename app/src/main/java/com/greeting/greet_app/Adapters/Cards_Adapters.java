package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fxn.stash.Stash;
import com.greeting.greet_app.CreateFramePhoto_Activity;
import com.greeting.greet_app.Preview_Activity;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList; 
import java.util.Random;

public class Cards_Adapters extends RecyclerView.Adapter<Cards_Adapters.StudentsViewHolder> {
    Context context;
    ArrayList<String> list;
    private LayoutInflater inflater;
    String Type="";
    public Cards_Adapters(Context context, ArrayList<String> clientModelArrayList,String type) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.Type = type;
        this.list = clientModelArrayList;
    }
    final Random mRandom = new Random(System.currentTimeMillis());
    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }
    @NonNull
    @NotNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.gif_item,parent,false);
        StudentsViewHolder viewHolder=new StudentsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentsViewHolder holder, int position) {
        if (list.size()>0){
         //   holder.setIsRecyclable(false);
            String model = list.get(position);
            Glide.with(context).load(R.raw.loading).into(holder.ic_pro);
            new Handler().postDelayed(() -> {
                holder.ic_pro.setVisibility(View.GONE);
                holder.ic_img.setVisibility(View.VISIBLE);
                Glide.with(context).asBitmap().load(model).into(holder.ic_img);
            },300);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Stash.put("position", position);
                    Stash.put("preview_list", list);
                    if (Type == Utils.Frames){
                        context.startActivity(new Intent(context, CreateFramePhoto_Activity.class)
                                .putExtra("type", Type)
                                .putStringArrayListExtra("previewList",list)
                                .putExtra("Link", model));
                    }
                    else {
                        context.startActivity(new Intent(context, Preview_Activity.class)
                                .putExtra("type", Type)
                                .putExtra("Link", model));
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class StudentsViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView ic_img,ic_pro;
        public StudentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ic_img =itemView.findViewById(R.id.ic_img);
            ic_pro =itemView.findViewById(R.id.ic_pro);
        }
    }
}
