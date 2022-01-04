package com.example.mobiles.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiles.Adapters.FragmentItemAdapter;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FragmentApple extends Fragment {
    RecyclerView appleRecyclerView;
    FragmentItemAdapter itemAdapter;
    List<PhoneDetails> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_apple,container,false);

        appleRecyclerView = view.findViewById(R.id.appleRecyclerView);
        appleRecyclerView.setHasFixedSize(true);
        appleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = getArguments();
        list = (ArrayList)getArguments().getSerializable("Data");
        itemAdapter = new FragmentItemAdapter(getContext(),list);
        appleRecyclerView.setAdapter(itemAdapter);


        return view;
    }
}
