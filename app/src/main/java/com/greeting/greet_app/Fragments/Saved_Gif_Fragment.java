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
public class Saved_Gif_Fragment extends BaseFragment {





    public Saved_Gif_Fragment() {
        // Required empty public constructor
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