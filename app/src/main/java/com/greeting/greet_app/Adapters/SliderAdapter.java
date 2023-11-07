package com.greeting.greet_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.greeting.greet_app.Model.SliderItem;
import com.greeting.greet_app.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context, List<SliderItem> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        Home_Slider_Category_Adapters home_slider_category_adapters=new Home_Slider_Category_Adapters(context,sliderItem.getCategoryModelArrayList());
        holder.recyclerView.setAdapter(home_slider_category_adapters);
       // viewHolder.imageViewBackground.setImageResource(sliderItem.getImage());
/*        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);*/

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    static class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        RecyclerView recyclerView;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.RecyclerView);
            this.itemView = itemView;
        }
    }

}
