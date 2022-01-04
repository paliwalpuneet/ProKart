package com.example.mobiles.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobiles.Adapters.MobileBrandAdapter;
import com.example.mobiles.Data.BrandAdapterDetails;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneMenu extends AppCompatActivity {

    RecyclerView mobileBrandView;
    MobileBrandAdapter adapter;
    List<BrandAdapterDetails> brandList = new ArrayList<>();
    List<PhoneDetails> allPhoneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_menu);

        mobileBrandView = findViewById(R.id.mobileBrandView);
        mobileBrandView.setHasFixedSize(true);
        mobileBrandView.setLayoutManager(new LinearLayoutManager(this));

        allPhoneList = (ArrayList) getIntent().getSerializableExtra("Data");

        String explore="Explore";
        BrandAdapterDetails apple = new BrandAdapterDetails(R.drawable.applebrand,explore);
        BrandAdapterDetails samsung = new BrandAdapterDetails(R.drawable.samsungbrand,explore);
        BrandAdapterDetails oneplus = new BrandAdapterDetails(R.drawable.oneplusbrand,explore);
        BrandAdapterDetails mi = new BrandAdapterDetails(R.drawable.mibrand,explore);
        BrandAdapterDetails huawei = new BrandAdapterDetails(R.drawable.huaweibrand,explore);

        brandList.add(apple);
        brandList.add(samsung);
        brandList.add(oneplus);
        brandList.add(mi);
        brandList.add(huawei);

        adapter = new MobileBrandAdapter(this,brandList,allPhoneList);

        mobileBrandView.setAdapter(adapter);

    }
}