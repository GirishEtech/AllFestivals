package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.greeting.greet_app.AllCategory_Activity;
import com.greeting.greet_app.Model.CategoryModel;
import com.greeting.greet_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Daily_Wishes_Category_Adapters extends RecyclerView.Adapter<Daily_Wishes_Category_Adapters.StudentsViewHolder> {
    EventListener listener;

    public interface EventListener {
        void onEvent(CategoryModel clientModel, int position);
    }
    Context context;
    ArrayList<CategoryModel> clientModelArrayList;
    private LayoutInflater inflater;
    String type="";

    public Daily_Wishes_Category_Adapters(Context context, ArrayList<CategoryModel> clientModelArrayList) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.clientModelArrayList = clientModelArrayList;
    }
    public Daily_Wishes_Category_Adapters(Context context, ArrayList<CategoryModel> clientModelArrayList, EventListener listener) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.clientModelArrayList = clientModelArrayList;
        this.listener=listener;
    }
    @NonNull
    @NotNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.daily_wishes_item,parent,false);
        StudentsViewHolder viewHolder=new StudentsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentsViewHolder holder, int position) {
        if (clientModelArrayList.size()>0){
         //   holder.setIsRecyclable(false);
            CategoryModel model= clientModelArrayList.get(position);
            holder.tv_name.setAllCaps(true);
            holder.tv_name.setText(model.getName());
          //  holder.img.setImageResource(model.getImage());
            Glide.with(context).load(model.getImageLink()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, AllCategory_Activity.class)
                            .putExtra("model",model));
                    Log.i("TESTING", "onClick: CAT NAME"+model.getMainCategoryName());
                    Log.i("TESTING", "onClick: NAME"+model.getName());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return clientModelArrayList.size();
    }

    class StudentsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        ImageView img;
        public StudentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_name =itemView.findViewById(R.id.tv_name);
            img =itemView.findViewById(R.id.img);
        }
    }
}
