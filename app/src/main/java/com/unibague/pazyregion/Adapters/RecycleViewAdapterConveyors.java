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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unibague.pazyregion.Model.Conveyor;
import com.unibague.pazyregion.R;

public class RecycleViewAdapterConveyors extends FirestoreRecyclerAdapter<Conveyor, RecycleViewAdapterConveyors.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecycleViewAdapterConveyors(@NonNull FirestoreRecyclerOptions<Conveyor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Conveyor conveyor) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String idDocument =documentSnapshot.getId();


        String email=conveyor.getEmail();
        String name=conveyor.getName();
        String surName=conveyor.getSurName();
        String direction=conveyor.getDirection();
        String phone=conveyor.getPhone();
        String description=conveyor.getDescription();
        String vehicle = conveyor.getVehicle();

        holder.text_view_conveyors_names.setText(name +" " + surName);
        holder.text_view_conveyors_vehicle.setText(vehicle);
        holder.text_view_conveyors_description.setText(description);
        holder.text_view_conveyors_direction.setText(direction);
        holder.text_view_conveyors_date.setText(phone);

        holder.material_card_conveyors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity= (AppCompatActivity)v.getContext();
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL,email)
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE,phone)
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.NAME,name+ " "+ surName);
                activity.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_conveyors,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView material_card_conveyors;
        ImageView image_view_list_conveyors;
        TextView text_view_conveyors_names;
        TextView text_view_conveyors_date;
        TextView text_view_conveyors_vehicle;
        TextView text_view_conveyors_direction;
        TextView text_view_conveyors_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            material_card_conveyors = itemView.findViewById(R.id.material_card_conveyors);
            image_view_list_conveyors=itemView.findViewById(R.id.image_view_list_conveyors);
            text_view_conveyors_names=itemView.findViewById(R.id.text_view_conveyors_names);
            text_view_conveyors_date=itemView.findViewById(R.id.text_view_conveyors_date);
            text_view_conveyors_vehicle=itemView.findViewById(R.id.text_view_conveyors_vehicle);
            text_view_conveyors_direction=itemView.findViewById(R.id.text_view_conveyors_direction);
            text_view_conveyors_description=itemView.findViewById(R.id.text_view_conveyors_description);
        }
    }
}
