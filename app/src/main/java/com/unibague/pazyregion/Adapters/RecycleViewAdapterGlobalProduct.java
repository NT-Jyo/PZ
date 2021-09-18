package com.unibague.pazyregion.Adapters;

import android.content.Intent;
import android.provider.ContactsContract;
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
import com.unibague.pazyregion.Activity.UpdateProductActivity;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;

public class RecycleViewAdapterGlobalProduct extends FirestoreRecyclerAdapter<Product,RecycleViewAdapterGlobalProduct.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecycleViewAdapterGlobalProduct(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecycleViewAdapterGlobalProduct.ViewHolder holder, int position, @NonNull Product product) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String documentId = documentSnapshot.getId();
        String title = product.getTitle();
        String date = product.getDate();
        String availability = product.getAvailability();
        String description =product.getDescription();
        String price =product.getPrice();
        String email=product.getEmail();
        String phone=product.getPhone();
        String name= product.getNameUser();
        String photoUrl= product.getUri();

        holder.text_view_product_global_title.setText(title);
        holder.text_view_product_global_date.setText(date);
        holder.text_view_product_global_availability.setText(availability);
        holder.text_view_product_global_description.setText(description);
        holder.text_view_product_global_price.setText(price);



        Glide.with(holder.image_view_list_global_product.getContext()).load(photoUrl).circleCrop().into(holder.image_view_list_global_product);
        holder.material_card_global_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity= (AppCompatActivity)v.getContext();
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL,email)
                        .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .putExtra(ContactsContract.Intents.Insert.PHONE,phone)
                        .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .putExtra(ContactsContract.Intents.Insert.NAME,name);
                appCompatActivity.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public RecycleViewAdapterGlobalProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_global_product,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView material_card_global_product;
        TextView text_view_product_global_title;
        TextView text_view_product_global_date;
        TextView text_view_product_global_availability;
        TextView text_view_product_global_description;
        TextView text_view_product_global_price;
        ImageView image_view_list_global_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            material_card_global_product =itemView.findViewById(R.id.material_card_global_product);
            text_view_product_global_title=itemView.findViewById(R.id.text_view_product_global_title);
            text_view_product_global_date=itemView.findViewById(R.id.text_view_product_global_date);
            text_view_product_global_availability=itemView.findViewById(R.id.text_view_product_global_availability);
            text_view_product_global_description=itemView.findViewById(R.id.text_view_product_global_description);
            text_view_product_global_price=itemView.findViewById(R.id.text_view_product_global_price);
            image_view_list_global_product=itemView.findViewById(R.id.image_view_list_global_product);
        }
    }
}
