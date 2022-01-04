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

public class FragmentXiaomi extends Fragment {

    RecyclerView xiaomiRecyclerView;
    FragmentItemAdapter itemAdapter;
    List<PhoneDetails> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_xiaomi,container,false);

        xiaomiRecyclerView = view.findViewById(R.id.xiaomiRecyclerView);
        xiaomiRecyclerView.setHasFixedSize(true);
        xiaomiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        List<PhoneDetails> xiaomiList =(ArrayList) bundle.getSerializable("Data");
       itemAdapter = new FragmentItemAdapter(getContext(),xiaomiList);
        xiaomiRecyclerView.setAdapter(itemAdapter);


        return view;
    }
}
