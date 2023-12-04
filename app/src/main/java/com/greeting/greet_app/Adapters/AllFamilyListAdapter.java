package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.greeting.greet_app.FamilyDetails;
import com.greeting.greet_app.Model.CategoryModel;
import com.greeting.greet_app.R;

import java.util.List;

public class AllFamilyListAdapter extends RecyclerView.Adapter<AllFamilyListAdapter.FamilyHolder> {

    private final List<CategoryModel> list;

    public AllFamilyListAdapter(List<CategoryModel> list) {
        this.list = list;
    }

    public class FamilyHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView subText;

        public FamilyHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.img);
            subText = view.findViewById(R.id.tv_name);
        }

        public void setData(CategoryModel model) {
            Glide.with(itemView)
                    .load(model.getImageLink())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            subText.setText(model.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, FamilyDetails.class)
                            .putExtra("name", model.getName()));
                }
            });
        }
    }

    @Override
    public FamilyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_item, parent, false);
        return new FamilyHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
