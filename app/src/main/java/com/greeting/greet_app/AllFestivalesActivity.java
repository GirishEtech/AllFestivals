package com.greeting.greet_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.greeting.greet_app.Adapters.AllFestivalsAdapters;
import com.greeting.greet_app.Adapters.AllFestivals_Category_Adapters;
import com.greeting.greet_app.Model.CategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllFestivalesActivity extends AppCompatActivity implements AllFestivalsAdapters.EventListener {
    AllFestivalsAdapters allFestivals_category_adapters;
    ArrayList<CategoryModel> all_festivel_list = new ArrayList<>();
    RecyclerView recyclerView_all_festivel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_festivales);
        recyclerView_all_festivel = findViewById(R.id.recyclerView_all_festivel);
        recyclerView_all_festivel.setLayoutManager(new GridLayoutManager(AllFestivalesActivity.this, 2));
        allFestivals_category_adapters = new AllFestivalsAdapters(AllFestivalesActivity.this, all_festivel_list, AllFestivalesActivity.this);
        recyclerView_all_festivel.setAdapter(allFestivals_category_adapters);
        Get_Festivals();

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
                                    categoryModel.setName(item.getName().split("\\.")[0].split("_")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0].split("_")[0]);
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

    CategoryModel clientModel_Add;
    int pos = -1;

    @Override
    public void onEvent(CategoryModel clientModel, int position) {
        clientModel_Add = clientModel;
        pos = position;
    }

    public void back(View view) {
        onBackPressed();
    }
}