package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unibague.pazyregion.R;

public class HomeCultivatorActivity extends AppCompatActivity  implements View.OnClickListener {

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;

    MaterialCardView card_view_cultivator_products;
    MaterialCardView card_view_cultivator_conveyors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cultivator);

        firebaseAuthB =FirebaseAuth.getInstance();
        firebaseFirestoreB=FirebaseFirestore.getInstance();
        firebaseCrashlyticsB = FirebaseCrashlytics.getInstance();

        card_view_cultivator_products = findViewById(R.id.card_view_cultivator_products);
        card_view_cultivator_conveyors=findViewById(R.id.card_view_cultivator_conveyors);

        card_view_cultivator_conveyors.setOnClickListener(this);
        card_view_cultivator_products.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_exit:
                firebaseAuthB.signOut();
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                editor.clear().apply();
                finishAffinity();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProducts(){
        Intent intent = new Intent(HomeCultivatorActivity.this, ProductsActivity.class);
        startActivity(intent);
    }

    private void loadConveyors() {
        Intent intent = new Intent(HomeCultivatorActivity.this, ConveyorsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.card_view_cultivator_products:
                loadProducts();
                break;
            case R.id.card_view_cultivator_conveyors:
                loadConveyors();
                break;
        }
    }


}