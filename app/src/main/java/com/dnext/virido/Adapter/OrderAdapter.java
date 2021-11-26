package com.dnext.virido.Adapter;


import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnext.virido.R;
import com.dnext.virido.model.CartModel;
import com.dnext.virido.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.protobuf.StringValue;

import java.text.DecimalFormat;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel, OrderAdapter.myviewholder>
{
    Integer itemC, newCount;
    Integer  newPrice;
    String finalc;

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull OrderModel order ){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Order").child(userID).child(order.getOrderId()).child("products");

        holder.name.setText(order.getOrderId());
        holder.price.setText("₹"+order.getTotalAmount());
//        holder.discountPrice.setText("₹"+cart.getPrice());
        holder.discountPrice.setPaintFlags(holder.discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.rating.setText(cart.getRating());
//        holder.ratingCount.setText("("+cart.getRatingCount()+")");
//        holder.itemCount.setText(cart.getItemcount());
//        holder.type.setText(cart.getType());
//        holder.discount.setText(cart.getDiscountpercent()+"% off");
//        Glide.with(holder.image.getContext()).load(cart.getImageurl()).centerCrop().into(holder.image);
//        Boolean isDiscounted = Boolean.parseBoolean(cart.getIsdiscounted());
//        String type = cart.getType();
//        String imageurl = cart.getImageurl();
//        String name = cart.getName();
//        String rating = cart.getRating();
//        String description = cart.getDescription();
//        String price = cart.getPrice();
//        String discountPrice = cart.getDiscountprice();
//
//        if (isDiscounted){
//            holder.discountPrice.setVisibility(View.VISIBLE);
//            holder.discount.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.discount.setVisibility(View.GONE);
//            holder.discountPrice.setVisibility(View.GONE);
//        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
//                itemC =   Integer.parseInt(cart.getItemcount());
                newCount = itemC + 1;
                finalc = decimalFormat.format(newCount);
                databaseReference.child("itemcount").setValue(finalc);
//                newPrice = Integer.parseInt(discountPrice) * newCount;
                databaseReference.child("totalprice").setValue(newPrice.toString());


            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
//                itemC = Integer.parseInt(cart.getItemcount());
                newCount = itemC -1;
                finalc = decimalFormat.format(newCount);
                databaseReference.child("itemcount").setValue(finalc);
//                newPrice = Integer.parseInt(discountPrice) * newCount;
                databaseReference.child("totalprice").setValue(newPrice.toString());
                if (newCount <= 0){
                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
//                                Toast.makeText(holder.itemView.getContext(), cart.getName()+" has been removed from cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("totalprice").setValue("0");
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            Toast.makeText(holder.itemView.getContext(), cart.getName()+" has been removed from cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_model, parent, false);
        return new myviewholder(view);

    }


    class myviewholder extends RecyclerView.ViewHolder
    {

        TextView name, price,discountPrice,discount ,rating, ratingCount,type, itemCount;
        ImageView image;
        CardView delete, add, remove;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.c_food_name);
            itemCount = itemView.findViewById(R.id.c_item_count);
            add = itemView.findViewById(R.id.c_ItemAdd);
            remove = itemView.findViewById(R.id.c_itemRemove);
            price = itemView.findViewById(R.id.c_price);
            discountPrice = itemView.findViewById(R.id.c_discountPrice);
            discount = itemView.findViewById(R.id.c_discount);
            delete = itemView.findViewById(R.id.c_deleteCard);
            rating = itemView.findViewById(R.id.c_rating);
            ratingCount = itemView.findViewById(R.id.cart_ratingCount);
            type = itemView.findViewById(R.id.c_type);
            image = itemView.findViewById(R.id.c_image);

        }
    }
}
