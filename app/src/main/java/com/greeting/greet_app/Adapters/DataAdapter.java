package com.greeting.greet_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.greeting.greet_app.Model.SimpleColor;
import com.greeting.greet_app.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    private AppCompatActivity activity;
    ArrayList<SimpleColor> data = new ArrayList();
    private OnClickListener onClickListener;

    private LayoutInflater inflater;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View textView;
          public MyViewHolder(View itemView) {
            super(itemView);
            textView = (View) itemView.findViewById( R.id.view );
        }

    }

    public DataAdapter(AppCompatActivity activity, ArrayList<SimpleColor> data) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.data = data;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.inflater.inflate(R.layout.color , parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        SimpleColor m = data.get(position);
        holder.textView.setBackgroundResource(m.getColorToUse());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, m);
                }
            }
        });


    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, SimpleColor model);
    }

    public int getItemCount() {
        return this.data.size();
    }
}