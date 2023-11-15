package com.greeting.greet_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.greeting.greet_app.Adapters.AllFestivals_Category_Adapters;
import com.greeting.greet_app.Adapters.Daily_Wishes_Category_Adapters;
import com.greeting.greet_app.Adapters.SliderAdapter;
import com.greeting.greet_app.Model.CategoryModel;
import com.greeting.greet_app.Model.SliderItem;
import com.greeting.greet_app.databinding.ActivityAllFestivalesBinding;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Category_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Category_Fragment extends Fragment implements Daily_Wishes_Category_Adapters.EventListener,
        AllFestivals_Category_Adapters.EventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Category_Fragment() {
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
    public static Home_Category_Fragment newInstance(String param1, String param2) {
        Home_Category_Fragment fragment = new Home_Category_Fragment();
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
    RecyclerView recyclerView_daily_wishes, recyclerView_all_festivel;
    Daily_Wishes_Category_Adapters daily_wishes_category_adapters;
    AllFestivals_Category_Adapters allFestivals_category_adapters;
    ArrayList<CategoryModel> daily_wishes_list = new ArrayList<>();
    ArrayList<CategoryModel> all_festivel_list = new ArrayList<>();
    ViewDialog viewDialog;
    TextView view_All1, view_All2, view_All3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_category, container, false);
        activity = getActivity();
        viewDialog = new ViewDialog(getActivity());
        //  ((MainActivity)getContext()).getSupportActionBar().setTitle(getActivity().getString(R.string.app_name));
/*
        daily_wishes_list.add(new CategoryModel("BirthDay",R.drawable.birthday));
        daily_wishes_list.add(new CategoryModel("Good Morning",R.drawable.good_morning));
        daily_wishes_list.add(new CategoryModel("BirGood NightthDay",R.drawable.good_night));
        daily_wishes_list.add(new CategoryModel("Motivation",R.drawable.motivation));
*/

/*        all_festivel_list.add(new CategoryModel("BirthDay",R.drawable.eid_ul_fitar));
        all_festivel_list.add(new CategoryModel("Good Morning",R.drawable.buddha_purnima));
        all_festivel_list.add(new CategoryModel("BirGood NightthDay",R.drawable.ambedkar_jayanti));*/
        //     Set_Slider();

        tv_no = view.findViewById(R.id.tv_no);
        view_All1 = view.findViewById(R.id.view_All1);
        view_All2 = view.findViewById(R.id.view_All2);
        view_All3 = view.findViewById(R.id.view_All3);
        recyclerView_daily_wishes = view.findViewById(R.id.RecyclerView_daily_wishes);
        recyclerView_all_festivel = view.findViewById(R.id.recyclerView_all_festivel);
        in_it();
        viewDialog.showDialog();
        Set_Slider();
        Get_Festivals();
        Get_DailyWishes();
        Get_FamilyWishes();
        view_All1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AllFestivalesActivity.class);
                intent.putExtra(Utils.TAB_NAME,"All Festivals");
                startActivity(intent);
            }
        });
        view_All2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),DailyWishes.class);
                intent.putExtra(Utils.TAB_NAME,"Daily Wishes");
                startActivity(intent);
            }
        });
        view_All3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AllFamilyActivity.class);
                intent.putExtra(Utils.TAB_NAME,"Family Wishes");
                startActivity(intent);
            }
        });
        return view;
    }

    private void Get_Festivals() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.All_Festivals);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path", "" + prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path", "" + item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setImageLink(uri.toString());
                                    //categoryModel.setMainCategoryName();
                                    categoryModel.setName(item.getName().split("\\.")[0].split("_")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0].split("_")[0]);
                                    categoryModel.setMainCategoryName("All Festivals");
                                    categoryModel.setOrder(Integer.parseInt(item.getPath().split("\\.")[0].split("_")[1]));
                                    all_festivel_list.add(categoryModel);
                                    Collections.sort(all_festivel_list, new Comparator<CategoryModel>() {
                                        @Override
                                        public int compare(CategoryModel categoryModel, CategoryModel t1) {
                                            String s1 = "" + categoryModel.getOrder();
                                            String s2 = "" + t1.getOrder();
                                            return s1.compareTo(s2);
                                            //  return (categoryModel.getOrder() > t1.getOrder() ? 1 : -1);
                                        }
                                    });
                                    allFestivals_category_adapters.notifyDataSetChanged();
                                    Log.e("url", uri.toString());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    private void Get_DailyWishes() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Daily_Wishes);
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path", "" + prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path", "" + item.getName().split("\\.")[0] + "/" + item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setImageLink(uri.toString());
                                    categoryModel.setMainCategoryName("Daily Wishes");
                                    categoryModel.setName(item.getName().split("\\.")[0].split("_")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0].split("_")[0]);
                                    categoryModel.setOrder(Integer.parseInt(item.getPath().split("\\.")[0].split("_")[1]));
                                    daily_wishes_list.add(categoryModel);
                                    Collections.sort(daily_wishes_list, new Comparator<CategoryModel>() {
                                        @Override
                                        public int compare(CategoryModel categoryModel, CategoryModel t1) {
                                            String s1 = "" + categoryModel.getOrder();
                                            String s2 = "" + t1.getOrder();
                                            return s1.compareTo(s2);
                                            //  return (categoryModel.getOrder() > t1.getOrder() ? 1 : -1);
                                        }
                                    });
                                    daily_wishes_category_adapters.notifyDataSetChanged();
                                    Log.e("url", uri.toString());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    private void Get_FamilyWishes() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Family_Wishes);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path", "" + prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path", "" + item.getName().split("\\.")[0] + "/" + item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setImageLink(uri.toString());
                                    categoryModel.setName(item.getName().split("\\.")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0]);
                                    categoryModel.setMainCategoryName("Family Wishes");
                                    list_family.add(categoryModel);
                                    adapter.notifyDataSetChanged();
                                    Log.e("url", uri.toString());
                                }
                            });
                        }
                        viewDialog.hideDialog();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    ArrayList<CategoryModel> list_family = new ArrayList<>();
    List<SliderItem> sliderItems = new ArrayList<>();
    SliderAdapter adapter;

    private void Set_Slider() {
        SliderView sliderView;

        sliderView = view.findViewById(R.id.imageSlider);

/*        list_family.add(new CategoryModel("Mother",R.drawable.mother));
        list_family.add(new CategoryModel("Father",R.drawable.father));
        list_family.add(new CategoryModel("Son",R.drawable.son));
        list_family.add(new CategoryModel("Daughter",R.drawable.daughter));
        list_family.add(new CategoryModel("GrandFather",R.drawable.grandfather));
        list_family.add(new CategoryModel("GrandMother",R.drawable.grandmother));
        list_family.add(new CategoryModel("Uncle",R.drawable.uncle));
        list_family.add(new CategoryModel("Aunt",R.drawable.aunt));*/
        sliderItems.add(new SliderItem(list_family));
        adapter = new SliderAdapter(getActivity(), sliderItems);
        sliderView.setSliderAdapter(adapter);
    }

    private void in_it() {
        recyclerView_daily_wishes.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        daily_wishes_category_adapters = new Daily_Wishes_Category_Adapters(activity, daily_wishes_list, Home_Category_Fragment.this);
        recyclerView_daily_wishes.setAdapter(daily_wishes_category_adapters);

        recyclerView_all_festivel.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        allFestivals_category_adapters = new AllFestivals_Category_Adapters(activity, all_festivel_list, Home_Category_Fragment.this);
        recyclerView_all_festivel.setAdapter(allFestivals_category_adapters);
    }

    CategoryModel clientModel_Add;


    int pos = -1;

    @Override
    public void onEvent(CategoryModel clientModel, int position) {
        clientModel_Add = clientModel;
        pos = position;
    }
}