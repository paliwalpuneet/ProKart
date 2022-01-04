package com.example.mobiles.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {

    Context context;
    List<PhoneDetails> list = new ArrayList<>();

    public CartItemsAdapter(Context context, List<PhoneDetails> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CartItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsAdapter.ViewHolder holder, int position) {

        PhoneDetails phoneDetails = list.get(position);
        Glide.with(context)
                .load(phoneDetails.getImages().get(2))
                .into(holder.phoneImage);
        holder.phoneName.setText(phoneDetails.getModel());
        holder.phonePrice.setText(phoneDetails.getPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener {

        ImageView phoneImage;
        TextView phoneName,phoneSpec,phonePrice;
        Button remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneImage = itemView.findViewById(R.id.cart_phone_image);
            phoneName = itemView.findViewById(R.id.cart_phone_name);
            phonePrice = itemView.findViewById(R.id.cart_phone_price);
            remove = itemView.findViewById(R.id.cart_remove);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeFromCart(getAdapterPosition());
        }
    }

    public void removeFromCart(int position)
    {
        SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreference.edit();
        Gson gson = new Gson();
        String json = mySharedPreference.getString("CartItems", "");

        Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
        List<PhoneDetails> list;
        list = gson.fromJson(json,type);
        PhoneDetails product = new PhoneDetails();
        for(PhoneDetails p:list)
        {
            if(p.getModel().equals(this.list.get(position).getModel()))
            {
                product=p;
            }
        }

        list.remove(product);

        String jsonPut = gson.toJson(list);
        editor.putString("CartItems",jsonPut);
        editor.commit();
        this.list=list;
        notifyDataSetChanged();


    }

    public void updateList(List<PhoneDetails> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }
}

