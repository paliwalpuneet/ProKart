package com.example.mobiles.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiles.Data.BrandAdapterDetails;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.Activities.PhoneByBrand;
import com.example.mobiles.R;

import java.util.ArrayList;
import java.util.List;

public class MobileBrandAdapter extends RecyclerView.Adapter<MobileBrandAdapter.ViewHolder> {
    Context context;
    List<BrandAdapterDetails> list;
    List<PhoneDetails> allPhoneList = new ArrayList<>();
    ArrayList<PhoneDetails> appleList = new ArrayList<>();
    ArrayList<PhoneDetails> samsungList = new ArrayList<>();
    ArrayList<PhoneDetails> xiaomiList = new ArrayList<>();
    ArrayList<PhoneDetails> onePlusList = new ArrayList<>();
    ArrayList<PhoneDetails> huaweiList = new ArrayList<>();

    public MobileBrandAdapter(Context context, List<BrandAdapterDetails> list,List<PhoneDetails> allPhoneList) {
        this.context = context;
        this.list = list;
        this.allPhoneList = allPhoneList;

        for(PhoneDetails p:allPhoneList)
        {
            if(p.getBrand().equals("Apple"))
                appleList.add(p);

            else if(p.getBrand().equals("Samsung"))
                samsungList.add(p);

            else if(p.getBrand().equals("Xiaomi"))
                xiaomiList.add(p);
            else if(p.getBrand().equals("OnePlus"))
                onePlusList.add(p);
            else if(p.getBrand().equals("Huawei"))
                huaweiList.add(p);

        }
    }

    @NonNull
    @Override
    public MobileBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mobile_brand_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileBrandAdapter.ViewHolder holder, int position) {
        BrandAdapterDetails brand = list.get(position);
        holder.mobileBrand.setImageResource(brand.getBrandImage());
        holder.mobileBrandButton.setText(brand.getBrandButtonText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mobileBrand;
        Button mobileBrandButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mobileBrand = itemView.findViewById(R.id.brand_image);
            mobileBrandButton = itemView.findViewById(R.id.brandNameButton);
            itemView.setOnClickListener(this);
            mobileBrandButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PhoneByBrand.class);
                if(getAdapterPosition()==0)
                {
                    intent.putExtra("Data", appleList);
                }
                else if(getAdapterPosition()==1)
                {
                    intent.putExtra("Data",samsungList);
                }

                else if(getAdapterPosition()==2)
                {
                    intent.putExtra("Data",onePlusList);
                }
                else if(getAdapterPosition()==3)
                {
                    intent.putExtra("Data",xiaomiList);
                }

                else if(getAdapterPosition()==4)
                {
                    intent.putExtra("Data",huaweiList);
                }
                context.startActivity(intent);
        }
    }
}
