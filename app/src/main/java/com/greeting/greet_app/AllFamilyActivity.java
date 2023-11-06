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
import com.greeting.greet_app.Adapters.AllFestivals_Category_Adapters;
import com.greeting.greet_app.Adapters.Home_SliderAdapters;
import com.greeting.greet_app.Adapters.Home_Slider_Category_Adapters;
import com.greeting.greet_app.Adapters.SliderAdapter;
import com.greeting.greet_app.Model.CategoryModel;
import com.greeting.greet_app.Model.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class AllFamilyActivity extends AppCompatActivity {
    ArrayList<CategoryModel> list_family =new ArrayList<>();
    Home_SliderAdapters adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_family);
        recyclerView=findViewById(R.id.recyclerView_all_festivel);
        recyclerView.setLayoutManager(new GridLayoutManager(AllFamilyActivity.this,3));
        adapter=new Home_SliderAdapters(AllFamilyActivity.this,list_family);
        recyclerView.setAdapter(adapter);
        Get_FamilyWishes();

    }
    private void Get_FamilyWishes() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Family_Wishes);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path",""+prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path",""+item.getName().split("\\.")[0]+"/"+item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CategoryModel categoryModel=new CategoryModel();
                                    categoryModel.setImageLink(uri.toString());
                                    categoryModel.setName(item.getName().split("\\.")[0]);
                                    categoryModel.setPath(item.getPath().split("\\.")[0]);
                                    list_family.add(categoryModel);
                                    adapter.notifyDataSetChanged();
                                    Log.e("url",uri.toString());
                                }
                            });
                        }
//                        viewDialog.hideDialog();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }


    public void back(View view) {
        onBackPressed();
    }
}