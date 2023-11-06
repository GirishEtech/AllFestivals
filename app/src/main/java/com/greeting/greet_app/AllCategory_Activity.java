package com.greeting.greet_app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.greeting.greet_app.Fragments.Cards_Fragment;
import com.greeting.greet_app.Fragments.Frames_Fragment;
import com.greeting.greet_app.Fragments.Gif_Fragment;
import com.greeting.greet_app.Fragments.Quotation_Fragment;
import com.greeting.greet_app.Fragments.Saved_Fragment;
import com.greeting.greet_app.Fragments.Stickers_Fragment;
import com.greeting.greet_app.Model.CategoryModel;

import java.util.ArrayList;

public class AllCategory_Activity extends AppCompatActivity {

    Activity activity;
    AppLangSessionManager appLangSessionManager;
    Toolbar toolbar;
    String UserMobileId="";
    String tab_name ="";
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    ArrayList<String> tab_name_list=new ArrayList<>();
    ArrayList<Integer> tab_icon_list=new ArrayList<>();
    ImageView create_img;
    CategoryModel categoryModel;
    public static TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_all_category);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        activity=this;
        UserMobileId=Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(Utils.Gifs);
        categoryModel= (CategoryModel) getIntent().getSerializableExtra("model");
        Utils.categoryModel=categoryModel;

        create_img = findViewById(R.id.create_img);
        tab_name_list.add(getString(R.string.Gifs));
        tab_name_list.add(getString(R.string.Cards));
        tab_name_list.add(getString(R.string.All_Quotes));
        tab_name_list.add(getString(R.string.Frames));
        tab_name_list.add(getString(R.string.Stickers));
        tab_icon_list.add(R.drawable.gif);
        tab_icon_list.add(R.drawable.cards);
        tab_icon_list.add(R.drawable.quotes);
        tab_icon_list.add(R.drawable.frames);
        tab_icon_list.add(R.drawable.stickers_tab);
        Set_Tab_layout();
        findViewById(R.id.card_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,Create_Activity.class);
                intent.putExtra(Utils.TAB_NAME,Utils.categoryModel.getName());
                intent.putExtra(Utils.CAT_TYPE,Utils.categoryModel.getMainCategoryName());
                startActivity(intent);
            }
        });
        findViewById(R.id.ic_nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(this).asGif().load(R.raw.create).into(create_img);
      }

    private void Set_Tab_layout() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new Gif_Fragment(), getString(R.string.Gifs));
        viewPageAdapter.addFragment(new Cards_Fragment(), getString(R.string.Cards));
        viewPageAdapter.addFragment(new Quotation_Fragment(), getString(R.string.All_Quotes));
        // viewPageAdapter.addFragment(new Gif_Fragment(), getString(R.string.Emoji));
        viewPageAdapter.addFragment(new Frames_Fragment(), getString(R.string.Frames));
        viewPageAdapter.addFragment(new Stickers_Fragment(), getString(R.string.Stickers));
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        Set_Tab();
        Set_Tab1();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Set_Tab1();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // for (int i = 0; i < tabLayout.getTabCount(); i++) {tabLayout.getTabAt(i).setIcon(image[i]);}
    }

    private void Set_Tab() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(viewPageAdapter.getTabView(i));
        }
    }
    private void Set_Tab1() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab.isSelected()){

                ((RelativeLayout)tab.getCustomView().findViewById(R.id.rv)).setBackground(getResources().getDrawable(R.drawable.bg_tab_yellow));
                ((CardView)tab.getCustomView().findViewById(R.id.card)).setVisibility(View.VISIBLE);
                ((ImageView)tab.getCustomView().findViewById(R.id.tab_img)).setImageResource(tab_icon_list.get(i));
                ((ImageView)tab.getCustomView().findViewById(R.id.tab_img)).setColorFilter(getResources().getColor(R.color.yellow));
                ((TextView)tab.getCustomView().findViewById(R.id.text)).setText(tab_name_list.get(i));
            }else {
                ((RelativeLayout)tab.getCustomView().findViewById(R.id.rv)).setBackground(getResources().getDrawable(R.drawable.bg_tab_black));
                ((CardView)tab.getCustomView().findViewById(R.id.card)).setVisibility(View.GONE);
                ((ImageView)tab.getCustomView().findViewById(R.id.tab_img)).setImageResource(tab_icon_list.get(i));
                ((ImageView)tab.getCustomView().findViewById(R.id.tab_img)).setColorFilter(getResources().getColor(R.color.white));
                ((TextView)tab.getCustomView().findViewById(R.id.text)).setText(tab_name_list.get(i));
            }
            //  tab.setCustomView(viewPageAdapter.getTabView(i));
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

    class ViewPageAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);

        }

        @Override
        public int getItemPosition(@NonNull  Object object) {
            return super.getItemPosition(object);
        }

/*        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }*/
        private String tabTitles[] = new String[] { getString(R.string.All_Festivals), getString(R.string.Daily_Wishes) };
        private int[] imageResId = { R.drawable.greeting_logo, R.drawable.greeting_logo };

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(activity).inflate(R.layout.tab_item, null);
            CardView cardView = (CardView) v.findViewById(R.id.carview);
            if (tabLayout.getSelectedTabPosition()==position){
            //    cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
            }
            return v;
        }
    }
}