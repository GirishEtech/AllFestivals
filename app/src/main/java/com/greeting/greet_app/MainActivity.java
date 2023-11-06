package com.greeting.greet_app;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.greeting.greet_app.Fragments.Best_Quotation_Fragment;
import com.greeting.greet_app.Fragments.Quotation_Fragment;
import com.greeting.greet_app.Fragments.Saved_Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    private int[] image = {
            R.drawable.ic_games_24,
            R.drawable.ic_clientss,
            R.drawable.icon_acc };
    AppLangSessionManager appLangSessionManager;
    Toolbar toolbar;
    String UserMobileId="";
    ImageView nav0_img,nav1_img,nav2_img,nav3_img,nav4_img,ic_darawer_back,ic_nav_menu;
    TextView nav0_text0,nav1_text1,nav2_text1,nav3_text1,nav4_text1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        activity=this;
        UserMobileId=Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        nav0_img=findViewById(R.id.nav0_img);
        nav0_text0=findViewById(R.id.nav0_text0);
        ic_nav_menu=findViewById(R.id.ic_nav_menu);
        ic_darawer_back=findViewById(R.id.ic_darawer_back);
        nav1_img=findViewById(R.id.nav1_img);
        nav2_img=findViewById(R.id.nav2_img);
        nav3_img=findViewById(R.id.nav3_img);
        nav4_img=findViewById(R.id.nav4_img);
        nav1_text1=findViewById(R.id.nav1_text1);
        nav2_text1=findViewById(R.id.nav2_text1);
        nav3_text1=findViewById(R.id.nav3_text1);
        nav4_text1=findViewById(R.id.nav4_text1);

        getSupportFragmentManager().beginTransaction().add(R.id.frame, new Home_Category_Fragment()).commit();
        Set_Bottom_Nav(1);
        findViewById(R.id.nav1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Categories_Fragment()).commit();
                Set_Bottom_Nav(2);
            }
        });
        findViewById(R.id.nav2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Special_Events_Fragment()).commit();
                Set_Bottom_Nav(3);
            }
        });
        findViewById(R.id.nav3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Best_Quotation_Fragment()).commit();
                Set_Bottom_Nav(4);
            }
        });
        findViewById(R.id.nav4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Saved_Fragment()).commit();
                Set_Bottom_Nav(5);
            }
        });
        findViewById(R.id.llHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Home_Category_Fragment()).commit();
                Set_Bottom_Nav(1);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.llprivacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,Policy_Activity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.llmoreapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                More_Apps();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.llrateus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.llshareapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share_App();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        ic_darawer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        ic_nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer((int) GravityCompat.START);
            }
        });
      //  Set_Tab_layout();
    }
    private void Share_App() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this App!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download  App at: https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void More_Apps() {
        String market_uri = "https://play.google.com/store/search?q=pub:AZ Tech Developer&hl=en";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(market_uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
    private void Set_Bottom_Nav(int i) {
        if (i==1){
            nav0_img.setImageResource(R.drawable.homefull);
            nav1_img.setImageResource(R.drawable.ic_categories1);
            nav2_img.setImageResource(R.drawable.ic_special_events1);
            nav3_img.setImageResource(R.drawable.ic_best_quotes1);
            nav4_img.setImageResource(R.drawable.ic_saved_items1);
            nav0_text0.setTextColor(getResources().getColor(R.color.yellow));
            nav1_text1.setTextColor(getResources().getColor(R.color.white));
            nav2_text1.setTextColor(getResources().getColor(R.color.white));
            nav3_text1.setTextColor(getResources().getColor(R.color.white));
            nav4_text1.setTextColor(getResources().getColor(R.color.white));
        }
        if (i==2){
            nav0_img.setImageResource(R.drawable.home);

            nav1_img.setImageResource(R.drawable.categories_filled);
            nav2_img.setImageResource(R.drawable.ic_special_events1);
            nav3_img.setImageResource(R.drawable.ic_best_quotes1);
            nav4_img.setImageResource(R.drawable.ic_saved_items1);
            nav0_text0.setTextColor(getResources().getColor(R.color.white));

            nav1_text1.setTextColor(getResources().getColor(R.color.yellow));
            nav2_text1.setTextColor(getResources().getColor(R.color.white));
            nav3_text1.setTextColor(getResources().getColor(R.color.white));
            nav4_text1.setTextColor(getResources().getColor(R.color.white));
        }
        if (i==3){
            nav0_img.setImageResource(R.drawable.home);

            nav1_img.setImageResource(R.drawable.ic_categories1);
            nav2_img.setImageResource(R.drawable.special_events_filled);
            nav3_img.setImageResource(R.drawable.ic_best_quotes1);
            nav4_img.setImageResource(R.drawable.ic_saved_items1);
            nav0_text0.setTextColor(getResources().getColor(R.color.white));

            nav1_text1.setTextColor(getResources().getColor(R.color.white));
            nav2_text1.setTextColor(getResources().getColor(R.color.yellow));
            nav3_text1.setTextColor(getResources().getColor(R.color.white));
            nav4_text1.setTextColor(getResources().getColor(R.color.white));
        }
        if (i==4){
            nav0_img.setImageResource(R.drawable.home);

            nav1_img.setImageResource(R.drawable.ic_categories1);
            nav2_img.setImageResource(R.drawable.ic_special_events1);
            nav3_img.setImageResource(R.drawable.best_quotes_filled);
            nav4_img.setImageResource(R.drawable.ic_saved_items1);
            nav0_text0.setTextColor(getResources().getColor(R.color.white));

            nav1_text1.setTextColor(getResources().getColor(R.color.white));
            nav2_text1.setTextColor(getResources().getColor(R.color.white));
            nav3_text1.setTextColor(getResources().getColor(R.color.yellow));
            nav4_text1.setTextColor(getResources().getColor(R.color.white));
        }
        if (i==5){
            nav0_img.setImageResource(R.drawable.baseline_home_24);

            nav1_img.setImageResource(R.drawable.ic_categories1);
            nav2_img.setImageResource(R.drawable.ic_special_events1);
            nav3_img.setImageResource(R.drawable.ic_best_quotes1);
            nav4_img.setImageResource(R.drawable.saved_items_filled);
            nav0_text0.setTextColor(getResources().getColor(R.color.white));

            nav1_text1.setTextColor(getResources().getColor(R.color.white));
            nav2_text1.setTextColor(getResources().getColor(R.color.white));
            nav3_text1.setTextColor(getResources().getColor(R.color.white));
            nav4_text1.setTextColor(getResources().getColor(R.color.yellow));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.del, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermission(){
        String exread= Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite= Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this,exread) !=
                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,exwrite) !=
                PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else {
            return true;
        }
    }

    public void getpermission(){
        String exread= Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite= Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[] {exread,exwrite},201);
        }
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      }

}