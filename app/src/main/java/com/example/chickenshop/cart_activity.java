package com.example.chickenshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chickenshop.model.Cart;
import com.example.chickenshop.model.order;
import com.example.chickenshop.model.user;
import com.example.chickenshop.viewholders.cart_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class cart_activity extends AppCompatActivity {
    private TextView total_amount,empty_cart;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference cartlist;
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);



        final RecyclerView recyclerView = findViewById(R.id.cart_recycleview);
        total_amount = findViewById(R.id.totalamount);
        empty_cart = findViewById(R.id.emptycart);
        ImageButton backbutton = findViewById(R.id.back_button);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         cartlist = FirebaseDatabase.getInstance().getReference()
                .child("userview").child("cart list").child(Objects.requireNonNull(currentuser.getPhoneNumber()));

        cartlist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()  == 0){
                    total_amount.setText(R.string.rs);
                    empty_cart.setVisibility(View.VISIBLE);
                }
                else{
                    FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions
                            .Builder<Cart>().setQuery(cartlist,Cart.class).build();

                  FirebaseRecyclerAdapter<Cart,cart_viewholder> adapter = new FirebaseRecyclerAdapter<Cart, cart_viewholder>(options) {
                      @Override
                      protected void onBindViewHolder(@NonNull cart_viewholder holder, final int position, @NonNull final Cart model) {
                          holder.itemname.setText(model.getItemname());
                          holder.chickenimage.setImageResource(model.getChicken_image());
                          holder.qty.setText(String.format("%s kg", String.valueOf(model.getQty())));
                          holder.remove_item.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  cartlist.child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         Toast.makeText(getApplicationContext(),"Item Removed",
                                                 Toast.LENGTH_SHORT).show();
                                         notifyItemRemoved(position);
                                     }
                                     else{
                                         Toast.makeText(getApplicationContext(),"Try again later",
                                                 Toast.LENGTH_SHORT).show();
                                     }
                                      }
                                  });
                              }
                          });

                      }

                      @NonNull
                      @Override
                      public cart_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
                          return new cart_viewholder(view);
                      }
                  };
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    float total = 0;
                    for(DataSnapshot singledata : snapshot.getChildren())
                    {
                        Cart cart = singledata.getValue(Cart.class);
                        assert cart != null;

                        total += cart.getTotal_price();

                    }
                    Log.d("stuff", "onDataChange: "+total);
                    total_amount.setText(String.format("Rs %s", total));


                    Button placeorder = findViewById(R.id.placeorder_button);
                    placeorder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            users.child(Objects.requireNonNull(currentuser.getPhoneNumber())).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot usersnapshot) {
                                    usersnapshot.getValue();
                                    if(usersnapshot.exists()){
                                        user user = usersnapshot.getValue(com.example.chickenshop.model.user.class);

                                        assert user != null;
                                        createpopupwithdetails(user,snapshot);
                                    }
                                    else
                                    {
                                        createpopup(snapshot);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      }

    private void createpopupwithdetails(final user user, final DataSnapshot snapshot) {
        Button placeorder_popup;
        final EditText name,ph,door,street,area,district,pincode;
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.user_details,null);
        placeorder_popup = view.findViewById(R.id.placeorder_popup);
        name = view.findViewById(R.id.name_edittext);
        ph = view.findViewById(R.id.phone_edittext);
        door = view.findViewById(R.id.Doorno_edittext);
        street = view.findViewById(R.id.Street_edittext);
        area = view.findViewById(R.id.area_edittext);
        district = view.findViewById(R.id.district_edittext);
        pincode = view.findViewById(R.id.pincode_edittext);
        name.setText(user.getName());
        ph.setText(user.getPhone());
        door.setText(user.getDoor_no());
        street.setText(user.getStreet());
        area.setText(user.getArea());
        district.setText(R.string.madurai);
        district.setOnClickListener(null);
        pincode.setText(user.getPincode());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        placeorder_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> usermap = new HashMap<>();
                if(!TextUtils.equals(name.getText().toString().trim(),user.getName())) {
                    usermap.put("name",name.getText().toString().trim());
                }
                if(!TextUtils.equals(ph.getText().toString().trim(),user.getPhone())){
                    usermap.put("phone",ph.getText().toString().trim());


                }
                if(!TextUtils.equals(door.getText().toString().trim(),user.getDoor_no())){
                    usermap.put("door_no",door.getText().toString().trim());


                }
                if(!TextUtils.equals(street.getText().toString().trim(),user.getStreet())){
                    usermap.put("street",street.getText().toString().trim());

                }
                if(!TextUtils.equals(area.getText().toString().trim(),user.getArea())){
                    usermap.put("area",area.getText().toString().trim());

                }
                if(!TextUtils.equals(pincode.getText().toString().trim(),user.getPincode())){
                    usermap.put("pincode",pincode.getText().toString().trim());

                }

                users.child(Objects.requireNonNull(currentuser.getPhoneNumber())).updateChildren(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            saveorder(snapshot);

                        }
                        else{
                            alertDialog.cancel();
                            Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



    }

    private void saveorder(DataSnapshot snapshot) {
        DatabaseReference Orders_ref = FirebaseDatabase.getInstance().getReference().child("userview")
                .child("orders").child(Objects.requireNonNull(currentuser.getPhoneNumber()));

        for(DataSnapshot singlecart : snapshot.getChildren()){
            Cart cart = singlecart.getValue(Cart.class);
            order order = new order();
            assert cart != null;
            order.setChicken_image(cart.getChicken_image());
            order.setItemname(cart.getItemname());
            order.setPrice(cart.getPrice());
            order.setTotal_price(cart.getTotal_price());
            order.setQty(cart.getQty());
            order.setStatus("Delivery pending");
            String currentdate,currenttime;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
            SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss aa");
            currentdate = dateFormat.format(calendar.getTime());
            currenttime = timeformat.format(calendar.getTime());
            String pid = currentdate + " " + currenttime;

            order.setDate_added(currentdate);
            order.setTime_added(currenttime);
            order.setPid(pid);

            Orders_ref.child(cart.getPid()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        alertDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(cart_activity.this,cart_activity.class));
                        finish();
                        overridePendingTransition(0,0);

                    }
                }
            });
        }
        cartlist.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Order placed",Toast.LENGTH_SHORT)
                        .show();
                alertDialog.cancel();
                startActivity(new Intent(cart_activity.this,cart_activity.class));
                finish();
                overridePendingTransition(0,0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.cancel();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void createpopup(final DataSnapshot snapshot) {
        Button placeorder_popup;
        final EditText name,ph,door,street,area,district,pincode;
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.user_details,null);
        placeorder_popup = view.findViewById(R.id.placeorder_popup);
        name = view.findViewById(R.id.name_edittext);
        ph = view.findViewById(R.id.phone_edittext);
        door = view.findViewById(R.id.Doorno_edittext);
        street = view.findViewById(R.id.Street_edittext);
        area = view.findViewById(R.id.area_edittext);
        district = view.findViewById(R.id.district_edittext);
        district.setText(R.string.madurai);
        district.setOnClickListener(null);
        pincode = view.findViewById(R.id.pincode_edittext);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        placeorder_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user user = new user();
                user.setName(name.getText().toString().trim());
                user.setPhone(ph.getText().toString().trim());
                user.setDoor_no(door.getText().toString().trim());
                user.setStreet(street.getText().toString().trim());
                user.setArea(area.getText().toString().trim());
                user.setPincode(pincode.getText().toString().trim());
                users.child(Objects.requireNonNull(currentuser.getPhoneNumber()))
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            saveorder(snapshot);
                        }
                        else
                        {
                            alertDialog.cancel();
                            Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });




    }

//    class cartlist_adapter extends RecyclerView.Adapter<cart_activity.cartlist_adapter.ViewHolder> {
//
//        private Context context;
//        private List<Cart> cartList;
//        private float total = 0;
//
//        public cartlist_adapter(Context context, List<Cart> cartList) {
//            this.context = context;
//            this.cartList = cartList;
//        }
//
//
//        public cartlist_adapter() {
//        }
//
//        @NonNull
//        @Override
//        public cart_activity.cartlist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull cart_activity.cartlist_adapter.ViewHolder holder, int position) {
//
//
////            Cart cartitem = cartList.get(position);
////
//
//
//        }
//
//        public float gettotalamount() {
//            for (Cart cart : cartList) {
//                total += cart.getPrice();
//            }
//            return total;
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return cartList.size();
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//
//            public ViewHolder(@NonNull View itemView, final Context ctx) {
//                super(itemView);
//                context = ctx;
//
//
//
////                remove_item.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        int position = getAdapterPosition();
////                        cartList.remove(position);
////                        notifyItemRemoved(position);
////                        startActivity(new Intent(cart_activity.this,cart_activity.class));
////                        finish();
////                        overridePendingTransition(0,0);
////
////
////                    }
////                });
//            }
//        }
//
//    }
}