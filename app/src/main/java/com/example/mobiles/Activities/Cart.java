package com.example.mobiles.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiles.Adapters.CartItemsAdapter;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Cart extends AppCompatActivity implements PaymentResultListener {

    RecyclerView cartView;
    CartItemsAdapter adapter;
    ArrayList<PhoneDetails> list =new ArrayList<>();
    TextView items,nothing,amount;
    Button checkout;
    int checkoutAmount;
    String name="",email="",address="",userId="",mobile="";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = mySharedPreferences.getString("CartItems", "");


        Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
        list = gson.fromJson(json,type);

        items = findViewById(R.id.items);
        cartView = findViewById(R.id.cartView);
        nothing = findViewById(R.id.nothing);
        amount = findViewById(R.id.amount);
        checkout = findViewById(R.id.checkout);
        cartView.setHasFixedSize(true);
        cartView.setLayoutManager(new LinearLayoutManager(this));



        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference =db.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                 name = documentSnapshot.get("name").toString();
                 email = documentSnapshot.get("email").toString();
                 address = documentSnapshot.get("address").toString();
                 mobile = documentSnapshot.get("mobile").toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User Fetch Cart", "onFailure: "+e);
            }
        });

        if(list!=null) {
            cartView.setVisibility(View.VISIBLE);
            nothing.setVisibility(View.GONE);
            adapter = new CartItemsAdapter(this, list);
            cartView.setAdapter(adapter);
            items.setText(list.size()+"");
            int sum=0;
            for(PhoneDetails p:list) {
                String phonePrice = p.getPrice();
                String numberOnly = phonePrice.replaceAll("[^0-9]", "");
                int n = Integer.parseInt(numberOnly);
                sum+=n;
            }
            amount.setText("Rs."+sum);
            checkoutAmount = sum;

        }
        else{
            cartView.setVisibility(View.GONE);
            nothing.setVisibility(View.VISIBLE);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list!=null)
                {
                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_gawUFX6L7Pmn1j");

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name","Mast Munda");
                        jsonObject.put("description","Test Payment");
                        jsonObject.put("theme.color","#0093DD");
                        jsonObject.put("currency","INR");
                        jsonObject.put("amount",checkoutAmount*100);
                        jsonObject.put("prefill.contact",mobile);
                        jsonObject.put("prefill.email","mastmunda1164@gmail.com");
                        checkout.open(Cart.this,jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                }

                else
                {
                    Toast.makeText(Cart.this, "Your cart is empty! Get some Shopping done", Toast.LENGTH_SHORT).show();
                }
            }
        });


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }

    @Override
    public void onPaymentSuccess(String s) {

        updateOrders();
        DatainBackground d = new DatainBackground();
        d.execute();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment is Done:- Payment Id is given below");
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        


    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getApplicationContext(),"Some Error Occurred while Doing Payment"+s,Toast.LENGTH_LONG).show();
    }


    public void updateOrders()
    {
        String orderDetails="";
        for(PhoneDetails p:list)
        {
            orderDetails+=p.getModel()+"\r\n";
        }

        String orderAmount = checkoutAmount+"";

        Map<String,Object> order = new HashMap<>();
        order.put("OrderDetails",orderDetails);
        order.put("Amount",orderAmount);
        order.put("Name",name);
        order.put("Email",email);
        order.put("Address",address);
        db.collection("Orders")
                .add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Order Placed", "onSuccess: "+documentReference.getId());
                updateMyOrders();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Order Failure", "onFailure: "+e);
            }
        });




    }

    public void updateMyOrders()
    {
        SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mySharedPreference.edit();
        Gson gson = new Gson();
        String json = mySharedPreference.getString("CartItems", "");

        Type type = new TypeToken<List<PhoneDetails>>(){}.getType();
        List<PhoneDetails> list;
        list = gson.fromJson(json,type);
        List<PhoneDetails> myOrders = new ArrayList<>();

        String json2 = mySharedPreference.getString("MyOrderItems", "");

        Type type2 = new TypeToken<List<PhoneDetails>>(){}.getType();

        myOrders = gson.fromJson(json2,type2);


        if(list!=null)
        {
            for(PhoneDetails p:list)
            {
                if(myOrders!=null)
                myOrders.add(p);
                else
                {
                    myOrders = new ArrayList<>();
                    myOrders.add(p);
                }
            }
        }
        String jsonPut = gson.toJson(myOrders);
        editor.putString("MyOrderItems",jsonPut);
        editor.commit();

        list.clear();


        SharedPreferences mySharedPreference2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor2 = mySharedPreference2.edit();

        String jsonPut2 = gson.toJson(list);
        editor2.putString("CartItems",jsonPut2);
        editor2.commit();

        Log.d("MyOrders", "updateMyOrders: ");
        for(PhoneDetails p:myOrders)
            Log.d("MyOrders", "updateMyOrders: "+p.getModel());

        Log.d("CartItems", "updateMyOrders: ");
        for(PhoneDetails p:list)
            Log.d("CartItems", "updateMyOrders: "+p.getModel());


        adapter.updateList(list);

        items.setText(list.size()+"");
        amount.setText("Rs. 0");

    }



    class DatainBackground extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            final String username = "munda1164@gmail.com";
            final String passMail = "9728804157";
            String orderDetails="";
            for(PhoneDetails p:list)
            {
                orderDetails+=p.getModel()+"\t";
            }

            String orderAmount = checkoutAmount+"";


            String Message = "Hey \r\n Your Order for " +orderDetails+" of Amount "+orderAmount+" has been placed Successfully\r\n Thankyou For shopping with Us\r\nRegards ProKart";

            Properties props = new Properties();
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.port","587");

            Session session = Session.getInstance(props,new javax.mail.Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,passMail);
                }
            });

            try{
                javax.mail.Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Order Placed From ProKart");
                message.setText(Message);
                Transport.send(message);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return "Done";
        }
    }

}