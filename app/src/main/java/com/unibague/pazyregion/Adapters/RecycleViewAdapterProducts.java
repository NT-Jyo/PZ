package com.unibague.pazyregion.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;
import com.unibague.pazyregion.Activity.UpdateProductActivity;

public class RecycleViewAdapterProducts extends FirestoreRecyclerAdapter<Product,RecycleViewAdapterProducts.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecycleViewAdapterProducts(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product product) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String documentId = documentSnapshot.getId();
        String title = product.getTitle();
        String date = product.getDate();
        String availability = product.getAvailability();
        String description =product.getDescription();
        String price =product.getPrice();
        String email=product.getEmail();
        String phone=product.getPhone();
        String photoUrl= product.getUri();

        holder.text_view_product_title.setText(title);
        holder.text_view_product_date.setText(date);
        holder.text_view_product_availability.setText(availability);
        holder.text_view_product_description.setText(description);
        holder.text_view_product_price.setText(price);



        Glide.with(holder.image_view_list_product.getContext()).load(photoUrl).circleCrop().into(holder.image_view_list_product);
        holder.material_card_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity= (AppCompatActivity)v.getContext();
                Intent intent= new Intent(appCompatActivity, UpdateProductActivity.class);
                intent.putExtra("documentId",documentId);
                intent.putExtra("title",title);
                intent.putExtra("availability",availability);
                intent.putExtra("description",description);
                intent.putExtra("price",price);
                intent.putExtra("photoUrl",photoUrl);
                appCompatActivity.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_product,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView material_card_product;
        TextView text_view_product_title;
        TextView text_view_product_date;
        TextView text_view_product_availability;
        TextView text_view_product_description;
        TextView text_view_product_price;
        ImageView image_view_list_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            material_card_product =itemView.findViewById(R.id.material_card_product);
            text_view_product_title=itemView.findViewById(R.id.text_view_product_title);
            text_view_product_date=itemView.findViewById(R.id.text_view_product_date);
            text_view_product_availability=itemView.findViewById(R.id.text_view_product_availability);
            text_view_product_description=itemView.findViewById(R.id.text_view_product_description);
            text_view_product_price=itemView.findViewById(R.id.text_view_product_price);
            image_view_list_product=itemView.findViewById(R.id.image_view_list_product);
        }
    }
}
