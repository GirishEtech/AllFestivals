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
import com.greeting.greet_app.Adapters.Saved_Cards_Adapters;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Saved_Cards_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Saved_Cards_Fragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Saved_Cards_Fragment() {
        // Required empty public constructor
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
    ArrayList<String> list = new ArrayList<>();
    Saved_Cards_Adapters gifs_adapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gif, container, false);
        activity = getActivity();
        UserSavedref = FirebaseDatabase.getInstance().getReference(Utils.UserSavedItems);
        recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, Utils.Span_Count));
        gifs_adapters = new Saved_Cards_Adapters(activity, list, Utils.Cards);
        recyclerView.setAdapter(gifs_adapters);
        if (!checkPermission()) {
            getpermission();
        } else {
            Get_Storage();
        }
        Get_Saved_Items();
        return view;
    }

    DatabaseReference UserSavedref;
    ArrayList<String> Saved_list = new ArrayList<>();

    private void Get_Saved_Items() {
        list.clear();
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserSavedref.child(android_id).child(Utils.Cards).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Saved_list.add(dataSnapshot.getValue(String.class));
                        list.add(dataSnapshot.getValue(String.class));
                        gifs_adapters.notifyDataSetChanged();
                    }
                    if (list.size() == 0) {
                        view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                    }
                } else {
                    view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Get_Storage() {
        list.clear();
        view.findViewById(R.id.tv_no).setVisibility(View.GONE);
        new LoadDownloadSongs().execute();
    }

    class LoadDownloadSongs extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //  progressDialog.showDialog();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadDownloaded();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (activity != null) {
                gifs_adapters.notifyDataSetChanged();
                if (list.size() == 0) {
                    view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // {"className":"Neet","subject":"Biology","chapterName":"biology in human welfare","topic":"human health and disease","sub_topic":"acquired immunity"}
    private void loadDownloaded() {
        try {
            ArrayList<String> videoModelArrayList1 = new ArrayList<>();
            File fileroot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + getString(R.string.app_name) + "/" + Utils.Cards);
            File[] files = fileroot.listFiles();
            if (files != null) {
                for (File file : files) {
                    videoModelArrayList1.add(file.toString());
                }
                list.clear();
                list.addAll(videoModelArrayList1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(activity, exread) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                exwrite) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void getpermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{exread, exwrite}, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "PERMISSION_GRANTED",
                        Toast.LENGTH_LONG).show();
                Get_Storage();
                //  new LoadDownloadSongs().execute();
            } else {
                Toast.makeText(activity, "PERMISSION_DENIED",
                        Toast.LENGTH_LONG).show();
                requestStoragePermission();
            }
        }
    }


    private void requestStoragePermission() {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Needed")
                .setMessage("Permission is needed to access files from your device...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1000);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Get_Storage();
            //  new LoadDownloadSongs().execute();
        }
    }
}