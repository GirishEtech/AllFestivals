package com.greeting.greet_app.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greeting.greet_app.Adapters.Gifs_Adapters;
import com.greeting.greet_app.Adapters.Saved_Gifs_Adapters;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Saved_Gif_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Saved_Gif_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Saved_Gif_Fragment() {
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
    public static Saved_Gif_Fragment newInstance(String param1, String param2) {
        Saved_Gif_Fragment fragment = new Saved_Gif_Fragment();
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
    Saved_Gifs_Adapters gifs_adapters;

    DatabaseReference UserSavedref;
    ArrayList<String> Saved_list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_gif, container, false);
        activity=getActivity();
        UserSavedref= FirebaseDatabase.getInstance().getReference(Utils.UserSavedItems);
        recyclerView=view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,Utils.Span_Count));
        gifs_adapters =new Saved_Gifs_Adapters(activity,list);
        recyclerView.setAdapter(gifs_adapters);
        Log.i("a", "as");
        Get_Saved_Items();
        return view;
    }
    private void Get_Saved_Items() {
        list.clear();
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserSavedref.child(android_id).child(Utils.Gifs).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Saved_list.add(dataSnapshot.getValue(String.class));
                        list.add(dataSnapshot.getValue(String.class));
                        gifs_adapters.notifyDataSetChanged();
                    }
                    if (list.size()==0){
                        view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                    }
                }else {
                    view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}