package com.greeting.greet_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greeting.greet_app.Adapters.AllFestivals_Category_Adapters;
import com.greeting.greet_app.Adapters.SliderAdapter;
import com.greeting.greet_app.Model.CategoryModel;
import com.greeting.greet_app.Model.SliderItem;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link All_Festivals_Category_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class All_Festivals_Category_Fragment extends Fragment implements AllFestivals_Category_Adapters.EventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public All_Festivals_Category_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Category_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static All_Festivals_Category_Fragment newInstance(String param1, String param2) {
        All_Festivals_Category_Fragment fragment = new All_Festivals_Category_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    Activity activity;
    TextView tv_no;
    RecyclerView recyclerView;
    ArrayList<CategoryModel> book_list =new ArrayList<>();
    AllFestivals_Category_Adapters bookClientsAdapters;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_festival_category, container, false);
        activity=getActivity();
      //  ((MainActivity)getContext()).getSupportActionBar().setTitle(getActivity().getString(R.string.app_name));
        SliderView sliderView;
        SliderAdapter adapter;
        sliderView = view.findViewById(R.id.imageSlider);
        List<SliderItem> sliderItems=new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.gudi_padwa));
        sliderItems.add(new SliderItem(R.drawable.guru_ravidas_jayanti));
        sliderItems.add(new SliderItem(R.drawable.happy_holi));
        sliderItems.add(new SliderItem(R.drawable.holika_dahan));
        sliderItems.add(new SliderItem(R.drawable.maha_shivratri));
        sliderItems.add(new SliderItem(R.drawable.ram_navami));
        sliderItems.add(new SliderItem(R.drawable.republic_day));
        sliderItems.add(new SliderItem(R.drawable.shivaji_jayanti));
        adapter = new SliderAdapter(getActivity(),sliderItems);
        sliderView.setSliderAdapter(adapter);
        sliderView.startAutoCycle();
        tv_no=view.findViewById(R.id.tv_no);
        recyclerView=view.findViewById(R.id.RecyclerView);
        setHasOptionsMenu(true);
        in_it();
        return view;
    }

    private void in_it() {
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        bookClientsAdapters=new AllFestivals_Category_Adapters(activity,book_list, All_Festivals_Category_Fragment.this);
        recyclerView.setAdapter(bookClientsAdapters);
    }

    @Override
    public void onEvent(CategoryModel clientModel, int position) {

    }
}