package com.example.mobiles.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.Activities.PhoneDetailActivity;
import com.example.mobiles.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneByBrandAdapter extends RecyclerView.Adapter<PhoneByBrandAdapter.ViewHolder> {

    Context context;
    List<PhoneDetails> list = new ArrayList<>();

    public PhoneByBrandAdapter(Context context, List<PhoneDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PhoneByBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_by_brand_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneByBrandAdapter.ViewHolder holder, int position) {
        PhoneDetails phoneDetails = list.get(position);
        Glide.with(context)
                .load(phoneDetails.getImages().get(2))
                .into(holder.phoneByBrandImage);
        holder.phoneByBrandName.setText(phoneDetails.getModel());
        holder.phoneByBrandPrice.setText(phoneDetails.getPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener {

        ImageView phoneByBrandImage;
        TextView phoneByBrandName,phoneByBrandPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            phoneByBrandImage = itemView.findViewById(R.id.phoneBrandImage);
            phoneByBrandName = itemView.findViewById(R.id.phoneBrandname);
            phoneByBrandPrice = itemView.findViewById(R.id.phoneBrandprice);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            PhoneDetails phoneDetails = list.get(getAdapterPosition());
            Intent intent = new Intent(context, PhoneDetailActivity.class);
            intent.putExtra("Data",phoneDetails);
            context.startActivity(intent);
        }
    }
}
