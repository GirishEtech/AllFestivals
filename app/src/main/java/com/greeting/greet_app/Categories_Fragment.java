package com.greeting.greet_app;

import android.app.Activity;
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
import com.greeting.greet_app.Adapters.Special_Event_Category_Adapters;
import com.greeting.greet_app.Model.CategoryModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Categories_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categories_Fragment extends Fragment implements Daily_Wishes_Category_Adapters.EventListener,
        AllFestivals_Category_Adapters.EventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Categories_Fragment() {
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
    public static Categories_Fragment newInstance(String param1, String param2) {
        Categories_Fragment fragment = new Categories_Fragment();
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
    RecyclerView recyclerView_all_special;
    Special_Event_Category_Adapters special_event_category_adapters;
    ArrayList<CategoryModel> special_events_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_special_event, container, false);
        activity = getActivity();

        //  ((MainActivity)getContext()).getSupportActionBar().setTitle(getActivity().getString(R.string.app_name));
        tv_no = view.findViewById(R.id.tv_no);
        recyclerView_all_special = view.findViewById(R.id.recyclerView_all_special);
        in_it();
        Get_Special_Events();
        return view;
    }

    private void Get_Special_Events() {
        special_events_list.clear();
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Categories);

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
                                    categoryModel.setMainCategoryName("Categories");
                                    categoryModel.setImageLink(uri.toString());
                                    categoryModel.setName(item.getName().split("\\.")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0]);
                                    special_events_list.add(categoryModel);
                                    special_event_category_adapters.notifyDataSetChanged();
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

    private void in_it() {
        recyclerView_all_special.setLayoutManager(new LinearLayoutManager(activity));
        special_event_category_adapters = new Special_Event_Category_Adapters(activity, special_events_list);
        recyclerView_all_special.setAdapter(special_event_category_adapters);

    }

    CategoryModel clientModel_Add;
    int pos = -1;

    @Override
    public void onEvent(CategoryModel clientModel, int position) {
        clientModel_Add = clientModel;
        pos = position;
    }
}