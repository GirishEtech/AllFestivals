package com.greeting.greet_app.Adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.greeting.greet_app.Model.SimpleColor;
import com.greeting.greet_app.R;

import java.util.ArrayList;

public class GradiantColorAdapter extends RecyclerView.Adapter<GradiantColorAdapter.MyViewHolder> {
    private AppCompatActivity activity;
    ArrayList<SimpleColor> data = new ArrayList();
    private OnClickListener onClickListener;

    private LayoutInflater inflater;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View textView;
          public MyViewHolder(View itemView) {
            super(itemView);
            textView = (View) itemView.findViewById( R.id.gradiantView );
        }

    }

    public GradiantColorAdapter(AppCompatActivity activity, ArrayList<SimpleColor> data) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.data = data;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.inflater.inflate(R.layout.gradiant_item , parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SimpleColor m = data.get(position);
        holder.textView.setBackgroundResource(m.getColorToUse());
        GradientDrawable m1 = (GradientDrawable) holder.textView.getBackground();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, m1);
                }
            }
        });


    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, GradientDrawable model);
    }

    public int getItemCount() {
        return this.data.size();
    }
}