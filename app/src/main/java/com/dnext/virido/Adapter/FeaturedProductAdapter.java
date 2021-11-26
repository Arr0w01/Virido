package com.dnext.virido.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnext.virido.ProductView;
import com.dnext.virido.R;
import com.dnext.virido.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FeaturedProductAdapter extends FirebaseRecyclerAdapter<ProductModel, FeaturedProductAdapter.myviewholder>
{

    public FeaturedProductAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ProductModel product ){
        holder.name.setText(product.getName());
//
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Boolean isFresh = Boolean.parseBoolean(product.getFresh());
        Boolean isDiscount = Boolean.parseBoolean(product.getIsdiscounted());
        Boolean prepaid = Boolean.parseBoolean(product.getPrepaid());
        String imageurl = product.getImageurl();
        String imageurl2 = product.getImageurl2();
        String name = product.getName();
        String rating = product.getRating();
        String ratingCount  = product.getRatingCount();
        String description = product.getDescription();
        String price = product.getPrice();
        String discountPrice = product.getDiscountprice();
        String discount = product.getDiscountpercent();
        String type = product.getType();
        String quantity = product.getQuantity();
        String category = product.getCategory();

        if(isFresh){
         holder.fresh.setText("Fresh");
        }else {
            holder.fresh.setText("Stored");
        }
        if (isDiscount){
            holder.discountView.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.discountPrice.setText("₹"+product.getDiscountprice());
            holder.price.setText("₹"+product.getPrice());
        }else {
            holder.discountView.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            holder.discountPrice.setText("₹"+product.getPrice());
        }

        holder.rating.setText(product.getRating());
        holder.discount.setText(product.getDiscountpercent());
        holder.type.setText(product.getType());

        Glide.with(holder.image.getContext()).load(imageurl).into(holder.image);

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             holder.bookmarkIcon.setColorFilter(Color.BLACK);
             holder.bookmarkIcon.setImageResource(R.drawable.favorite_selected);
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ProductView.class);
//                intent.putExtra("Type",Type);
                intent.putExtra("Name", name);
                intent.putExtra("Type", type);
                intent.putExtra("Rating", rating);
                intent.putExtra("RatingCount", ratingCount );
                intent.putExtra("Description", description);
                intent.putExtra("Image", imageurl);
                intent.putExtra("Image2", imageurl2);
                intent.putExtra("Price", price);
                intent.putExtra("Discount Price", discountPrice);
                intent.putExtra("Discount",discount );
                intent.putExtra("isDiscount",isDiscount);
                intent.putExtra("isFresh", isFresh);
                intent.putExtra("isPrepaid", prepaid);
                intent.putExtra("Category", category);
                intent.putExtra("Quantity", quantity);

                holder.itemView.getContext().startActivity(intent);

            }
        });

    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featuredproduct_layout, parent, false);
        return new myviewholder(view);

    }



    class myviewholder extends RecyclerView.ViewHolder
    {

        TextView name, price, discountPrice, discount ,rating, type, fresh;
        ImageView image, bookmarkIcon;
        CardView discountView,bookmark;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.f_productName);
            price = itemView.findViewById(R.id.f_price);
            discountPrice =itemView.findViewById(R.id.f_discountPrice);
            discount = itemView.findViewById(R.id.f_discountPercent);
            rating = itemView.findViewById(R.id.f_rating);
            image = itemView.findViewById(R.id.f_productImg);
            type = itemView.findViewById(R.id.f_type);
            fresh = itemView.findViewById(R.id.f_isFresh);
            discountView = itemView.findViewById(R.id.f_discountView);
            bookmark =itemView.findViewById(R.id.f_bookmark);
            bookmarkIcon =itemView.findViewById(R.id.bookmarkIcon);
        }
    }
}