package com.example.mobiles.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobiles.Adapters.PhoneByBrandAdapter;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;

import java.util.ArrayList;

public class PhoneByBrand extends AppCompatActivity {

    RecyclerView phoneByBrandView;
    PhoneByBrandAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_by_brand);


        ArrayList<PhoneDetails> list = (ArrayList)getIntent().getSerializableExtra("Data");
        phoneByBrandView = findViewById(R.id.phonebrandView);
        phoneByBrandView.setHasFixedSize(true);
        phoneByBrandView.setLayoutManager(new GridLayoutManager(PhoneByBrand.this,2));

        adapter = new PhoneByBrandAdapter(this,list);

        phoneByBrandView.setAdapter(adapter);
    }
}