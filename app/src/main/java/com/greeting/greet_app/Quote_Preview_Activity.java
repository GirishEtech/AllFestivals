package com.greeting.greet_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Quote_Preview_Activity extends AppCompatActivity {

    Activity activity;
    String Type="";
    String Link="";
    CardView carview;
    TextView tv_quotes;
    DatabaseReference UserSavedref;
    ArrayList<String> Saved_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_preview);
        activity=this;
        UserSavedref= FirebaseDatabase.getInstance().getReference(Utils.UserSavedItems);
        carview=findViewById(R.id.carview);
        tv_quotes=findViewById(R.id.tv_quotes);
        Type=Utils.Quotes;
        Link=getIntent().getStringExtra("link");
        tv_quotes.setText(Link);
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
                if (Saved_list.contains(Link)){
                    Toast.makeText(activity, "Already Saved", Toast.LENGTH_SHORT).show();
                    return;
                }
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String Key=UserSavedref.push().getKey();
                UserSavedref.child(android_id).child(Type).child(Key).setValue(Link);
                Toast.makeText(activity, "Downloaded", Toast.LENGTH_SHORT).show();
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
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Log.e("Key",dataSnapshot.getKey());
                        // Saved_list.add(dataSnapshot.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void share_img(Bitmap imgbitmap) {
        File imgfolder=new File(getCacheDir(),"images");
        Uri uri=null;
        try {
            imgfolder.mkdir();
            File file=new File(imgfolder,"shared_img.jpeg");
            FileOutputStream stream=new FileOutputStream(file);
            imgbitmap.compress(Bitmap.CompressFormat.JPEG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(getApplicationContext(),
                    "com.greeting.greet_app.fileprovider",file);
        }catch (IOException e){

        }
        String compUrl="Please Download App At : https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent shareintent=new Intent(Intent.ACTION_SEND);
        shareintent.setType("image/jpeg");
        shareintent.putExtra(Intent.EXTRA_STREAM,uri);
        shareintent.putExtra(Intent.EXTRA_TEXT,compUrl);
        shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareintent,"share"));
        Snackbar.make(findViewById(R.id.rv_main), "Shared",Snackbar.LENGTH_LONG).show();
    }

    private void viewToBitmap() {
        carview.invalidate();
        carview.refreshDrawableState();

        carview.setDrawingCacheEnabled(true);
        carview.buildDrawingCache();
        Bitmap imgbitmap=carview.getDrawingCache();
        share_img(imgbitmap);
        //   Toast.makeText(getApplicationContext(),"load",Toast.LENGTH_LONG).show();
    }
    private void Share_App(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Quotes");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\nDownload  App at: https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}