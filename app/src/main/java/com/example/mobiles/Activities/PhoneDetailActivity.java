package com.example.mobiles.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhoneDetailActivity extends AppCompatActivity {

    TextView description,phoneName,phonePrice;
    CarouselView phonePics;
    PhoneDetails phoneDetails;
    Button addToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mySharedPreference.edit();
        Gson gson = new Gson();
        String json = mySharedPreference.getString("CartItems", "");

        phoneDetails = (PhoneDetails) getIntent().getSerializableExtra("Data");

        Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
        List<PhoneDetails> list;
        list = gson.fromJson(json,type);


        description = findViewById(R.id.desc);
        phoneName = findViewById(R.id.phoneName);
        phonePrice = findViewById(R.id.phonePrice);
        phonePics = findViewById(R.id.phonePics);
        addToCart = findViewById(R.id.addToCart);


        description.setText(phoneDetails.getDetails());
        phoneName.setText(phoneDetails.getModel());
        phonePrice.setText(phoneDetails.getPrice());




        phonePics.setPageCount(phoneDetails.getImages().size());

        phonePics.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                switch(position)
                {
                    case 0:
                        Glide.with(getApplicationContext())
                                .load(phoneDetails.getImages().get(0))
                                .into(imageView);
                        break;
                    case 1:
                        Glide.with(getApplicationContext())
                                .load(phoneDetails.getImages().get(1))
                                .into(imageView);
                        break;
                    case 2:
                        Glide.with(getApplicationContext())
                                .load(phoneDetails.getImages().get(2))
                                .into(imageView);
                        break;
                }
            }
        });

        if(list!=null)
        {
            if(!list.isEmpty())
            {
                boolean present = false;
                for(PhoneDetails p:list)
                    if(p.getModel().equals(phoneDetails.getModel()))
                    {
                        present = true;
                        break;
                    }
                if(present) {
                    addToCart.setText("Added To Cart");
                    addToCart.setTextColor(getResources().getColor(R.color.white));
                    addToCart.setEnabled(false);
                }


            }
        }


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PhoneDetails> list;
                SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = mySharedPreference.edit();
                Gson gson = new Gson();
                String json = mySharedPreference.getString("CartItems", "");
                Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
                list = gson.fromJson(json,type);
                if(list!=null)
                {
                    if(!list.isEmpty())
                    {
                        boolean present = false;
                        for(PhoneDetails p:list)
                        if(p.getModel().equals(phoneDetails.getModel()))
                        {
                            present = true;
                            break;
                        }
                        if(!present)
                            list.add(phoneDetails);
                    }
                    else
                    {
                        list.add(phoneDetails);
                    }
                }
                else
                {
                    list = new ArrayList<>();
                    list.add(phoneDetails);
                }
                String jsonPut = gson.toJson(list);
                editor.putString("CartItems",jsonPut);
                editor.commit();
                addToCart.setText("Added To Cart");
                addToCart.setTextColor(getResources().getColor(R.color.white));
                addToCart.setEnabled(false);
                Toast.makeText(PhoneDetailActivity.this, "Added to the Cart", Toast.LENGTH_SHORT).show();
            }
        });


    }
}