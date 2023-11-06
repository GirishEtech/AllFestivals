package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.greeting.greet_app.Preview_Activity;
import com.greeting.greet_app.Quote_Preview_Activity;
import com.greeting.greet_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Quotation_Adapters extends RecyclerView.Adapter<Quotation_Adapters.StudentsViewHolder> {
    Context context;
    ArrayList<String> list;
    private LayoutInflater inflater;
    String type="";

    public Quotation_Adapters(Context context, ArrayList<String> clientModelArrayList) {
        inflater=LayoutInflater.from(context);
        this.context = context;
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
        View view=inflater.inflate(R.layout.single_quotation_item,parent,false);
        StudentsViewHolder viewHolder=new StudentsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentsViewHolder holder, int position) {
        if (list.size()>0){
         //   holder.setIsRecyclable(false);
            String model= list.get(position);
            holder.text.setText(model);
            holder.text.setBackgroundColor(generateRandomColor());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Stash.put("position", position);
                    Stash.put("preview_list", list);

                    context.startActivity(new Intent(context, Quote_Preview_Activity.class)
                            .putExtra("link",model));
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
        ImageView img;
        public StudentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            text =itemView.findViewById(R.id.text);
        }
    }
}
