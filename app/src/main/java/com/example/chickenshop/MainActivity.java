package com.example.chickenshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.chickenshop.model.Cart;
import com.example.chickenshop.model.category_item;
import com.example.chickenshop.model.model_chickenitem;
import com.example.chickenshop.viewholders.product_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseFirestore db;
    private List<model_chickenitem> listitems = new ArrayList<>();
    private List<SlideModel> slideModels = new ArrayList<>();
    private List<category_item> category_items = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private FirebaseAuth.AuthStateListener authStateListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(user==null)
        {
            startActivity(new Intent(getApplicationContext(),login_activity.class));
            finish();
        }
        else{

        setslideritems();
        setcategoryitems();
        setbestseller();








        //setting navigation drawer

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_window);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        final TextView headertext = headerview.findViewById(R.id.header_text);
        headertext.setText(user.getDisplayName());





        //setting best sellers





        ImageButton menu = findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });






        ImageButton cartbutton = findViewById(R.id.cart_button);
        ImageSlider imageSlider = findViewById(R.id.imageSlider);

        imageSlider.setImageList(slideModels,true);



        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,cart_activity.class));

            }
        });







    }
    }

    private void setbestseller() {



        DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("products");

        FirebaseRecyclerOptions<model_chickenitem> options= new FirebaseRecyclerOptions
                .Builder<model_chickenitem>().setQuery(products,model_chickenitem.class)
                .build();

        FirebaseRecyclerAdapter<model_chickenitem, product_viewholder> adapter =
                new FirebaseRecyclerAdapter<model_chickenitem, product_viewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final product_viewholder holder, int position, @NonNull final model_chickenitem item) {

                        holder.price.setText(String.format("\u20B9%s", item.getPrice()));
                        holder.chickenitem.setText(item.getItem_name());
                        holder.chickenimage.setImageResource(item.getImage());
                        holder.chickenimage.setTag(item.getImage());

                        holder.addcart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Integer.parseInt(holder.spinner.getText().toString()) == 0)
                                {
                                    Toast.makeText(getApplicationContext(),"Please add some quantity",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //getting date and time
                                else{
                                String currentdate,currenttime;
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
                                SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss aa");
                                currentdate = dateFormat.format(calendar.getTime());
                                currenttime = timeformat.format(calendar.getTime());



                                assert user != null;
                                DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("userview").child("cart list")
                                        .child(Objects.requireNonNull(user.getPhoneNumber()));
                                Cart cart = new Cart();
                                cart.setChicken_image(Integer.parseInt(String.valueOf(holder.chickenimage.getTag())));
                                cart.setItemname(holder.chickenitem.getText().toString());
                                cart.setPid(currentdate + " " + currenttime);
                                cart.setPrice(Float.parseFloat(item.getPrice()));
                                cart.setQty(Float.parseFloat(holder.spinner.getText().toString()));
                                cart.setTime_added(currenttime);
                                cart.setDate_added(currentdate);
                                cart.setTotal_price(Float.parseFloat(item.getPrice())*Float.parseFloat(holder.spinner.getText().toString()));
                                cartlist.child(currentdate + " " +currenttime).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                            }}
                        });

                    }

                    @NonNull
                    @Override
                    public product_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chicken_item,parent,false);
                        return new product_viewholder(view);
                    }
                };
        recyclerView = findViewById(R.id.bestseller_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }




    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }}



    private void setcategoryitems() {
        category_items.add(new category_item(R.drawable.chicken,"Chicken"));
        category_items.add(new category_item(R.drawable.egg_icon,"Eggs"));
        category_items.add(new category_item(R.drawable.fish_icon,"Fish"));
        category_items.add(new category_item(R.drawable.chicken,"Chicken"));
        category_items.add(new category_item(R.drawable.egg_icon,"Eggs"));
        category_items.add(new category_item(R.drawable.fish_icon,"Fish"));

        RecyclerView category_recyclerview = findViewById(R.id.category_recyclerview);
        category_recycleradapter categoryRecycleradapter = new category_recycleradapter(this,category_items);
        category_recyclerview.setHasFixedSize(true);
        category_recyclerview.setLayoutManager(new GridLayoutManager(this,3));
        category_recyclerview.setAdapter(categoryRecycleradapter);
        categoryRecycleradapter.notifyDataSetChanged();


    }

    private void setslideritems() {

        slideModels.add(new SlideModel("https://image.freepik.com/free-photo/raw-chicken-fillet-with-garlic-tomato-sauce_114579-11885.jpg"));
        slideModels.add(new SlideModel("https://image.freepik.com/free-photo/skillet-with-chicken-fillets-various-spices_1220-562.jpg"
                ));
        slideModels.add(new SlideModel("https://image.freepik.com/free-photo/chicken-wings-barbecue-sweetly-sour-sauce-picnic-summer-menu-tasty-food-top-view-flat-lay_2829-6471.jpg"
        ));

    }

    @Override
    protected void onStart() {
        super.onStart();




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mycart_nav:
                startActivity(new Intent(MainActivity.this,cart_activity.class));
                break;
            case R.id.orders_nav:
                startActivity(new Intent(MainActivity.this,order_activity.class));
                break;
            case R.id.logout_nav:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
                overridePendingTransition(0,0);
                break;

        }
        return false;
    }
}