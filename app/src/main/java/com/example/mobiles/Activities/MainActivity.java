package com.example.mobiles.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiles.Data.PhoneDetails;
import com.example.mobiles.Fragments.FragmentApple;
import com.example.mobiles.Fragments.FragmentSamsung;
import com.example.mobiles.Fragments.FragmentXiaomi;
import com.example.mobiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TextView name_navheader,email_nav_header,mobile_navheader;
    TabItem xiaomi,samsung,apple;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    CarouselView carouselView;
    ArrayList<PhoneDetails> allPhoneList = new ArrayList<>();
    List<PhoneDetails> xiaomiList = new ArrayList<>();
    List<PhoneDetails> appleList = new ArrayList<>();
    ArrayList<PhoneDetails> samsungList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<PhoneDetails> firestoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tabLayout = findViewById(R.id.tab_layout);
        xiaomi = findViewById(R.id.xiaomi);
        samsung = findViewById(R.id.samsung);
        apple = findViewById(R.id.apple);
        carouselView = findViewById(R.id.carouselView);
        setupCarousel();

        DataInBackground d = new DataInBackground();
        d.execute();


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer);

        View v = nav.getHeaderView(0);
        name_navheader = v.findViewById(R.id.name_navheader);
        email_nav_header = v.findViewById(R.id.email_navheader);
        mobile_navheader = v.findViewById(R.id.mobile_navheader);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = FirebaseFirestore .getInstance().collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name_navheader.setText(documentSnapshot.get("name").toString());
                email_nav_header.setText(documentSnapshot.get("email").toString());
                mobile_navheader.setText(documentSnapshot.get("mobile").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("NavHeader Data Fetch", "onFailure: "+e);
            }
        });



        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.menu_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_brand:
                        Intent intent = new Intent(MainActivity.this, PhoneMenu.class);
                        intent.putExtra("Data",allPhoneList);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_cart:
                        Intent intentBrand = new Intent(MainActivity.this, Cart.class);
                        intentBrand.putExtra("Data", samsungList);
                        startActivity(intentBrand);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.sign_out:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,Login.class));
                        finish();

                    case R.id.menu_myorders:
                        startActivity(new Intent(MainActivity.this,MyOrders.class));
                        

                }

                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Data",(ArrayList)xiaomiList);
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true).replace(R.id.fragment_container,FragmentXiaomi.class,bundle).commit();
                        break;
                    case 1:
                        Bundle samsungBundle = new Bundle();
                        samsungBundle.putSerializable("Data",(ArrayList)samsungList);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,FragmentSamsung.class,samsungBundle).commit();
                        break;
                    case 2:
                        Bundle bundleApple = new Bundle();
                        bundleApple.putSerializable("Data",(ArrayList)appleList);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentApple.class,bundleApple).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void setupCarousel()
    {
        carouselView.setPageCount(3);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                switch(position)
                {
                    case 0:
                        imageView.setImageResource(R.drawable.iphonesimage);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.allphoneimage);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.xiaomiphones);
                        break;
                }
            }
        });
    }


    class DataInBackground extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {


            db.collection("Mobiles").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {

                                Log.d("FireStoreData", "onSuccess: " + queryDocumentSnapshots);
                                PhoneDetails p = documentSnapshot1.toObject(PhoneDetails.class);
                                if (p.getBrand().equals("Xiaomi"))
                                    xiaomiList.add(p);
                                else if (p.getBrand().equals("Apple"))
                                    appleList.add(p);
                                else if (p.getBrand().equals("Samsung"))
                                    samsungList.add(p);

                                firestoreList.add(p);
                                allPhoneList.add(p);

                            }
                            for (PhoneDetails p : firestoreList)
                                Log.d("FirestoreList", "Firestore list " + p.getBrand() + " " + p.getModel() + " " + p.getImages().get(0));
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("Data", (ArrayList) xiaomiList);
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true).add(R.id.fragment_container, FragmentXiaomi.class, bundle).commit();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firestore Failure", "onFailure: " + e);
                }
            });


//            RequestQueue requestQueue;
//            requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://v1.nocodeapi.com/mast_munda/supabase/hUmloOpjvPhVEGKx?tableName=Mobiles", new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//
//                    for(int i=0;i<response.length();i++) {
//                        try {
//                            JSONObject obj = response.getJSONObject(i);
//                            System.out.println("OBJ"+obj);
//                            Gson gson = new Gson();
//                            PhoneDetails data = gson.fromJson(String.valueOf(obj),PhoneDetails.class);
//                            allPhoneList.add(data);
//                            if(data.getBrand().equals("Xiaomi"))
//                            xiaomiList.add(data);
//                            else if(data.getBrand().equals("Apple"))
//                                appleList.add(data);
//                            else if(data.getBrand().equals("Samsung"))
//                                samsungList.add(data);
//
//                          //  Log.d("Data", "onResponse: Data "+data.getImages()[0]);;
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList("Data", (ArrayList)xiaomiList);
//                    getSupportFragmentManager().beginTransaction().addToBackStack("Home")
//                            .setReorderingAllowed(true).add(R.id.fragment_container,FragmentXiaomi.class,bundle).commit();
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//            requestQueue.add(jsonArrayRequest);
//
//
//
            return "Done";
        }



    }

}