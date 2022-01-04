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

import java.util.ArrayList;
import java.util.List;

public class FragmentSamsung extends Fragment {

    RecyclerView samsungRecyclerView;
    FragmentItemAdapter itemAdapter;
    List<PhoneDetails> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_samsung,container,false);

        samsungRecyclerView = view.findViewById(R.id.samsungRecyclerView);
        samsungRecyclerView.setHasFixedSize(true);
        samsungRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = getArguments();
        list =(ArrayList) bundle.getSerializable("Data");
        itemAdapter = new FragmentItemAdapter(getContext(),list);
        samsungRecyclerView.setAdapter(itemAdapter);


        return view;
    }
}
