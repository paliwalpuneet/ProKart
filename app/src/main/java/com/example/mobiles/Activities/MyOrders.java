package com.example.mobiles.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.mobiles.Adapters.FragmentItemAdapter;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    RecyclerView myOrderView;
    FragmentItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        myOrderView = findViewById(R.id.myOrdersView);
        myOrderView.setHasFixedSize(true);
        myOrderView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = mySharedPreference.getString("MyOrderItems", "");

        Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
        List<PhoneDetails> myOrders;
        myOrders = gson.fromJson(json,type);

        if(myOrders!=null)
        {
            adapter = new FragmentItemAdapter(this,myOrders);
            myOrderView.setAdapter(adapter);
        }
    }
}