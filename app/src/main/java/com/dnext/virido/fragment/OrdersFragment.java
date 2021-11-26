package com.dnext.virido.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dnext.virido.Adapter.CartAdapter;
import com.dnext.virido.Adapter.OrderAdapter;
import com.dnext.virido.R;
import com.dnext.virido.model.CartModel;
import com.dnext.virido.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    RecyclerView activeOrders;

    FirebaseUser firebaseUser;
    FirebaseDatabase db;
    DatabaseReference dr;
    String userID;
    OrderAdapter ordersAdapter;
    ArrayList<OrderModel> orderModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        //addign ids
        activeOrders = view.findViewById(R.id.activeOrdersRV);

//      Database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        db = FirebaseDatabase.getInstance();
        dr = db.getReference().child("Order").child(userID);
        orderModels = new ArrayList<OrderModel>();

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cartSnapshot: snapshot.getChildren()) {
                    orderModels.add(cartSnapshot.getValue(OrderModel.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



// active orders recyclerview
        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Order").child(userID), OrderModel.class)
                        .build();
        ordersAdapter = new OrderAdapter(options);
        activeOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        activeOrders.setNestedScrollingEnabled(false);
        activeOrders.setAdapter(ordersAdapter);




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ordersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ordersAdapter.stopListening();
    }
}