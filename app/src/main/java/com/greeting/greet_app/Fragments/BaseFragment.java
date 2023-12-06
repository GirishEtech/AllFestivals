package com.greeting.greet_app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.greeting.greet_app.R;

import java.util.ArrayList;
import java.util.Objects;

public class BaseFragment extends Fragment {



    public BaseFragment() {
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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        show();
        super.onAttach(context);
    }

    private void show() {
        Fragment frg = this;
        final FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.show(this);
        ft.commit();
    }
    private void hide() {
        Fragment frg = this;
        final FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.hide(this);
        ft.commit();
    }

    @Override
    public void onDetach()
    {hide();
        super.onDetach();
    }
}
