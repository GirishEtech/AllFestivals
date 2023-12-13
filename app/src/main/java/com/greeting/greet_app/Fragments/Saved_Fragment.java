package com.greeting.greet_app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransactionKt;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.greeting.greet_app.Home_Category_Fragment;
import com.greeting.greet_app.MainActivity;
import com.greeting.greet_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Saved_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Saved_Fragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup container;

    private LayoutInflater inflater;


    public Saved_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Saved_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Saved_Fragment newInstance(String param1, String param2) {
        Saved_Fragment fragment = new Saved_Fragment();
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

    Activity activity;
    View view;

    Bundle savedInstanceState;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;
    ArrayList<String> tab_name_list = new ArrayList<>();
    ArrayList<Integer> tab_icon_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Toast.makeText(requireContext(), "this is CreateView", Toast.LENGTH_SHORT).show();
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;
        view = inflater.inflate(R.layout.fragment_saved, container, false);
        Set_Tab_layout();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (view != null) {
            refresh();
        }
        super.onAttach(context);
    }

    private void refresh() {
        // Fragment frg  = (BaseFragment) getActivity().getSupportFragmentManager().findViewById(R.id.frame);
        final FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.frame);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
        Set_Tab_layout();
    }

    @Override
    public void onDetach() {
        final FragmentManager main = requireActivity().getSupportFragmentManager();
        main.popBackStack();
        onDestroy();
        Toast.makeText(requireContext(), "this is Detach", Toast.LENGTH_SHORT).show();
        super.onDetach();
    }

    private void Set_Tab_layout() {
        activity = getActivity();
        tab_name_list.clear();
        tab_icon_list.clear();
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
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager());
        viewPageAdapter.addFragment(new Saved_Gif_Fragment(), getString(R.string.Gifs));
        viewPageAdapter.addFragment(new Saved_Cards_Fragment(), getString(R.string.Cards));
        viewPageAdapter.addFragment(new Saved_Quotation_Fragment(), getString(R.string.All_Quotes));
        viewPageAdapter.addFragment(new Saved_Frames_Fragment(), getString(R.string.Frames));
        viewPageAdapter.addFragment(new Saved_Sticker_Fragment(), getString(R.string.Stickers));
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        Set_Tab();
        Set_Tab1();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Set_Tab1();
                Log.i("a", "Tab Text " + tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void Set_Tab() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(viewPageAdapter.getTabView(i));
            Log.i("a", "as");
        }
    }

    private void Set_Tab1() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab.isSelected()) {
                Log.i("a", "ass");
                ((RelativeLayout) tab.getCustomView().findViewById(R.id.rv)).setBackground(getResources().getDrawable(R.drawable.bg_tab_yellow));
                ((CardView) tab.getCustomView().findViewById(R.id.card)).setVisibility(View.VISIBLE);
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_img)).setImageResource(tab_icon_list.get(i));
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_img)).setColorFilter(getResources().getColor(R.color.yellow));
                ((TextView) tab.getCustomView().findViewById(R.id.text)).setText(tab_name_list.get(i));
            } else {
                ((RelativeLayout) tab.getCustomView().findViewById(R.id.rv)).setBackground(getResources().getDrawable(R.drawable.bg_tab_black));
                ((CardView) tab.getCustomView().findViewById(R.id.card)).setVisibility(View.GONE);
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_img)).setImageResource(tab_icon_list.get(i));
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_img)).setColorFilter(getResources().getColor(R.color.white));
                ((TextView) tab.getCustomView().findViewById(R.id.text)).setText(tab_name_list.get(i));
            }
            //  tab.setCustomView(viewPageAdapter.getTabView(i));
        }
    }

    class ViewPageAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public void clearAll() {
            fragments.clear();
            notifyDataSetChanged();
        }

        private FragmentManager fragmentManager;

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
            fragmentManager = fm;
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
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }


        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(activity).inflate(R.layout.tab_item, null);
            return v;
        }
    }
}