package com.greeting.greet_app.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.greeting.greet_app.Adapters.Gifs_Adapters;
import com.greeting.greet_app.Adapters.Quotation_Adapters;
import com.greeting.greet_app.AllCategory_Activity;
import com.greeting.greet_app.MainActivity;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Gif_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Gif_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Gif_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gif_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Gif_Fragment newInstance(String param1, String param2) {
        Gif_Fragment fragment = new Gif_Fragment();
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
    RecyclerView recyclerView;
    Activity activity;
    ArrayList<String> list=new ArrayList<>();
    Gifs_Adapters quotation_adapters;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_gif, container, false);
        activity=getActivity();
        view.findViewById(R.id.btn_home).setOnClickListener(view1 -> {
            Toast.makeText(activity, "home btn is Click", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireActivity(), MainActivity.class));
        });
        recyclerView=view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,Utils.Span_Count));
        quotation_adapters=new Gifs_Adapters(activity,list);
        recyclerView.setAdapter(quotation_adapters);
        Get_Storage();
        return view;
    }


    private void Get_Storage() {
        list.clear();
        view.findViewById(R.id.tv_no).setVisibility(View.GONE);
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.categoryModel.getPath()+"/"+Utils.Gifs);
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        if (listResult.getItems().size()==0){
                            view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.btn_home).setOnClickListener(view1 -> {
                                Toast.makeText(activity, "home btn is Click", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(requireActivity(), MainActivity.class));
                            });
                        }
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path",""+prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path",""+item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    list.add(uri.toString());
                                    quotation_adapters.notifyDataSetChanged();
                                    Log.e("url",uri.toString());
                                }
                            });
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        // Uh-oh, an error occurred!
                    }
                });
    }
    private boolean _hasLoadedOnce= false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            if (AllCategory_Activity.tv_title!=null){
                AllCategory_Activity.tv_title.setText(Utils.Gifs);
            }
        }
    }

}