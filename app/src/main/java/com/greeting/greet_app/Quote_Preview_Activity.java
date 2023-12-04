package com.greeting.greet_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greeting.greet_app.Adapters.Quotation_AdaptersSlider;

import java.util.ArrayList;

public class Quote_Preview_Activity extends AppCompatActivity {

    Activity activity;
    private ViewPager2 viewPager2;
    String Type = "";
    String Link = "";
    CardView carview;
    TextView tv_quotes;
    DatabaseReference UserSavedref;
    ArrayList<String> Saved_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_preview);
        activity = this;
        UserSavedref = FirebaseDatabase.getInstance().getReference(Utils.UserSavedItems);
        Type = Utils.Quotes;
        Link = getIntent().getStringExtra("link");
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        //ArrayList<String> previewList = Stash.getArrayList("list_preview", String.class);
        ArrayList lst = getIntent().getStringArrayListExtra("lst_preview");
        Log.d("TAG", "onCreate: "+lst);
        viewPager2.setAdapter(new Quotation_AdaptersSlider(this,lst, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        int position = Stash.getInt("position");
        viewPager2.setCurrentItem(position);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (lst.size() <= position) {
                    viewPager2.setClipToPadding(true);
                    viewPager2.setClipChildren(true);
                    Log.e("TAG", "onPageSelected: Pos ->" + position);
                } else {
                    Log.i("TAG", "onPageSelected: Array Size ->" + lst.size());
                    Link = (String) lst.get(position);
                }
            }

        });
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        Get_Saved_Items();
        findViewById(R.id.ic_nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.llDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Saved_list.contains(Link)) {
                    Toast.makeText(activity, "Already Saved", Toast.LENGTH_SHORT).show();
                    return;
                }
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String Key = UserSavedref.push().getKey();
                UserSavedref.child(android_id).child(Type).child(Key).setValue(Link);
                Log.i("Test", "onClick: item is Saved ");
                Toast.makeText(Quote_Preview_Activity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                Saved_list.add(Link);
            }
        });
        findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewToBitmap();
                // Share_App(Link);
            }
        });
    }

    private void Get_Saved_Items() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        UserSavedref.child(android_id).child(Type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.e("Key", dataSnapshot.getKey());
                        // Saved_list.add(dataSnapshot.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void share_img(String imgbitmap) {
        String compUrl = "Please Download App At : https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_SUBJECT, "Share Quotes");
        shareintent.putExtra(Intent.EXTRA_TEXT, imgbitmap);
        //shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareintent, "share"));
        Snackbar.make(findViewById(R.id.rv_main), "Shared", Snackbar.LENGTH_LONG).show();
    }

    private void viewToBitmap() {
        share_img(Link);
    }

    private void Share_App(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Quotes");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + "\n\nDownload  App at: https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}