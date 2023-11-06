package com.greeting.greet_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class FamilyDetails extends AppCompatActivity implements Daily_Wishes_Category_Adapters.EventListener,
        AllFestivals_Category_Adapters.EventListener {
    TextView tv_no;
    RecyclerView recyclerView_all_special;
    Special_Event_Category_Adapters special_event_category_adapters;
    ArrayList<CategoryModel> special_events_list = new ArrayList<>();
    TextView text;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);
        tv_no = findViewById(R.id.tv_no);
        text = findViewById(R.id.text);
         name = getIntent().getStringExtra("name");
        text.setText(name);
        recyclerView_all_special = findViewById(R.id.recyclerView_all_special);
        in_it();
        Get_Special_Events();

    }

    private void Get_Special_Events() {
        special_events_list.clear();
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Family_Wishes).child(Utils.Family_Details);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path", "" + prefix.getName());
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path", "" + item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    CategoryModel categoryModel = new CategoryModel();
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
        recyclerView_all_special.setLayoutManager(new LinearLayoutManager(FamilyDetails.this));
        special_event_category_adapters = new Special_Event_Category_Adapters(FamilyDetails.this, special_events_list);
        recyclerView_all_special.setAdapter(special_event_category_adapters);

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