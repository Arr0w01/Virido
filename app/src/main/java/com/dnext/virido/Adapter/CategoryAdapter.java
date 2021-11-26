package com.dnext.virido.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnext.virido.Categoryview;
import com.dnext.virido.R;
import com.dnext.virido.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CategoryAdapter extends FirebaseRecyclerAdapter<ProductModel, CategoryAdapter.myviewholder>
{

    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ProductModel product ){

        holder.name.setText(product.getName());
//

        Glide.with(holder.image.getContext()).load(product.getImageurl()).circleCrop().into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Categoryview.class);
                intent.putExtra("Category", product.getName());

                holder.itemView.getContext().startActivity(intent);
            }
        });



    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);

        return new myviewholder(view);

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();

    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        TextView name;
        ImageView image;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cat_name);
            image = itemView.findViewById(R.id.cat_img);

        }
    }
}

