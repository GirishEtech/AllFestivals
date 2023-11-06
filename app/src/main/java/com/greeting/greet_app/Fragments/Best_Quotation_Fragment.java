package com.greeting.greet_app.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greeting.greet_app.Adapters.Quotation_Adapters;
import com.greeting.greet_app.AllCategory_Activity;
import com.greeting.greet_app.R;
import com.greeting.greet_app.Utils;
import com.greeting.greet_app.ViewDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Best_Quotation_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Best_Quotation_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Best_Quotation_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quotation_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Best_Quotation_Fragment newInstance(String param1, String param2) {
        Best_Quotation_Fragment fragment = new Best_Quotation_Fragment();
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

    View view;
    RecyclerView recyclerView;
    Activity activity;
    ArrayList<String> list=new ArrayList<>();
    Quotation_Adapters quotation_adapters;
    DatabaseReference BestQuotesRef;
    ViewDialog viewDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_quotation, container, false);
        activity=getActivity();
        viewDialog=new ViewDialog(activity);
        if (AllCategory_Activity.tv_title!=null){
            AllCategory_Activity.tv_title.setText(Utils.Quotes);
        }
        BestQuotesRef= FirebaseDatabase.getInstance().getReference(Utils.Best_Quotes);
      //  In_it_List();
        recyclerView=view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        quotation_adapters=new Quotation_Adapters(activity,list);
        recyclerView.setAdapter(quotation_adapters);
        view.findViewById(R.id.tv_no).setVisibility(View.GONE);
        Get_Best_Quotes();
        return view;
    }

    private void Get_Best_Quotes() {
        viewDialog.showDialog();
        list.clear();
        BestQuotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    viewDialog.hideDialog();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        list.add(dataSnapshot.getValue(String.class));
                        quotation_adapters.notifyDataSetChanged();
                    }
                }else {
                    viewDialog.hideDialog();
                    view.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void In_it_List() {
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
        list.add(getString(R.string.text_templete));
    }
}