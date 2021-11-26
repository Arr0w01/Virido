package com.dnext.virido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dnext.virido.Adapter.CartAdapter;
import com.dnext.virido.ProfileActivities.AddressActivity;
import com.dnext.virido.fragment.ProfileFragment;
import com.dnext.virido.model.CartModel;
import com.dnext.virido.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import static com.dnext.virido.MainActivity.cartCount;
import static com.dnext.virido.MainActivity.cartCountCard;
import static com.dnext.virido.MainActivity.cartCountview;
import static com.dnext.virido.fragment.HomeFragment.finalAddress;
import static com.dnext.virido.fragment.HomeFragment.finalPincode;
import static com.dnext.virido.fragment.HomeFragment.finalSubadd;

public class CartActivity extends AppCompatActivity {
    ImageView back;
    TextView changeAdress, delivery_name, delivery_number, delivery_pincode, delivery_address;
    RecyclerView cartView;
    CartAdapter cartAdapter;
    OrderModel orderModel;
    Calendar calForDate;
    SimpleDateFormat currentDate, currentTime;
    String saveCurrentDate, saveCurrentTIme;
    FirebaseUser firebaseUser;
    String userID;
    ArrayList<CartModel> cartModels;
    FirebaseDatabase db;
    DatabaseReference dr;
    TextView checkoutValue;
    Button checkoutBtn, priceDetailBtn;
    Spinner timeSlot;
    CardView numberError, nameError, addressError, priceDetailCard;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        ui
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

//        id Assign
        cartView = findViewById(R.id.cartRV);
        back = findViewById(R.id.cart_back);
        checkoutValue = findViewById(R.id.cart_checkoutPrice);
        checkoutBtn = findViewById(R.id.checkoutOrder_btn);
        timeSlot = findViewById(R.id.setTimeslot);
        changeAdress =findViewById(R.id.change_deliveryAddress);
        delivery_name =findViewById(R.id.delivery_name);
        delivery_number =findViewById(R.id.delivery_number);
        delivery_pincode =findViewById(R.id.delivery_pincode);
        delivery_address =findViewById(R.id.delivery_adress);
        numberError = findViewById(R.id.number_error);
        nameError = findViewById(R.id.name_error);
        addressError = findViewById(R.id.address_error);
        priceDetailBtn = findViewById(R.id.priceDetail_btn);
        scrollView = findViewById(R.id.cartSV);
        priceDetailCard = findViewById(R.id.price_detail_card);
//        back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cartView.setLayoutManager(new LinearLayoutManager(this));
        cartView.setNestedScrollingEnabled(false);

//        Database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        db = FirebaseDatabase.getInstance();
        dr = db.getReference("Cart").child(userID);

        orderModel = new OrderModel();
//        Datetime
        calForDate = Calendar.getInstance();
        currentDate = new SimpleDateFormat("dd MMM");
        currentTime = new SimpleDateFormat("HH:mm:ss a");


//        firebase data
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Cart").child(userID), CartModel.class)
                        .build();

        cartAdapter = new CartAdapter(options);
        cartView.setAdapter(cartAdapter);

        cartModels = new ArrayList<CartModel>();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot cartSnapshot: snapshot.getChildren()) {
                  cartModels.add(cartSnapshot.getValue(CartModel.class));
                  CartModel cartItems = cartSnapshot.getValue(CartModel.class);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Delivery Address
        changeAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivity(intent);
            }
        });

        if (firebaseUser.getDisplayName()!=null && !firebaseUser.getDisplayName().equals("")) {
            delivery_name.setText(firebaseUser.getDisplayName());
            nameError.setVisibility(View.GONE);
        }else {
        delivery_name.setText("");
        nameError.setVisibility(View.VISIBLE);
        }

        if (firebaseUser.getPhoneNumber() != null && !firebaseUser.getPhoneNumber().equals("") ) {
            delivery_number.setText(firebaseUser.getPhoneNumber());
            numberError.setVisibility(View.GONE);
        }else {
            delivery_number.setText("");
            numberError.setVisibility(View.VISIBLE);
        }
        if (finalPincode!= null && finalAddress != null) {
            delivery_pincode.setText(finalPincode);
            delivery_address.setText(finalAddress);
            addressError.setVisibility(View.GONE);
        }else {
            addressError.setVisibility(View.VISIBLE);
        }
        if (firebaseUser.getDisplayName() != null && firebaseUser.getPhoneNumber() != null && finalAddress!= null && finalPincode !=null && !firebaseUser.getPhoneNumber().equals("")){
            checkoutBtn.setEnabled(true);
            checkoutBtn.setBackgroundColor(getResources().getColor(R.color.theme));
        }else {
            checkoutBtn.setEnabled(false);
            checkoutBtn.setBackgroundColor(Color.GRAY);
        }


//    Delivery Slot
        ArrayList<String> slotList = new ArrayList<String>();
        slotList.add("6:00am - 8:00am");
        slotList.add("8:00am - 10:00am");
        slotList.add("6:00pm - 9:00pm");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,slotList);

        timeSlot.setAdapter(adapter);


//        checkout
       priceDetailBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               scrollView.smoothScrollTo(0, (int) priceDetailCard.getY());
           }
       });

       checkoutBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (cartCount!= null && cartCount != 0){
                   PlaceOrder();

               }
               else {
                   Snackbar.make(checkoutBtn,"Cart is empty!", Snackbar.LENGTH_LONG).setAction("Continue Shopping", new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                           startActivity(intent);
                       }
                   }).show();
               }
           }
       });

    }

    private void PlaceOrder() {
        saveCurrentDate = currentDate.format(calForDate.getTime());
        saveCurrentTIme = currentTime.format(calForDate.getTime());

        orderModel.setCustomerName(delivery_name.getText().toString());
        orderModel.setCustomerNumber(delivery_number.getText().toString());
        orderModel.setCustomerAddress(delivery_address.getText().toString());
        orderModel.setCustomerPincode(delivery_pincode.getText().toString());
        orderModel.setProducts(cartModels);

        String orderid = "OD"+ UUID.randomUUID().toString().replace("-","");
        orderModel.setOrderId(orderid);
        dr = db.getReference("Order").child(userID).child(orderid);
        if (orderModel != null) {
            dr.setValue(orderModel)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CartActivity.this, "Your Order has been placed successfully", Toast.LENGTH_SHORT).show();
                                dr =db.getReference("Cart").child(userID);
                                dr.removeValue();
                            } else {
                                Toast.makeText(CartActivity.this, "failed to place the order", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "data not fetched", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.startListening();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer sum = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("totalprice");
                    Integer value = Integer.parseInt(String.valueOf(price));
                    sum += value;
                    if (sum != 0  ) {
                        checkoutValue.setText("₹"+sum.toString());
                    }
                    else {
                        checkoutValue.setText("₹0");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStop(){
        super.onStop();
        cartAdapter.stopListening();
    }
}