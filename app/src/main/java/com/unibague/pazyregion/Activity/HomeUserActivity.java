package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.unibague.pazyregion.R;

public class HomeUserActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView card_view_user_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        card_view_user_products=findViewById(R.id.card_view_user_products);
        card_view_user_products.setOnClickListener(this);
    }

    private void loadProductGlobal(){
        Intent intent= new Intent(HomeUserActivity.this, ProductsGlobalActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view_user_products:
            loadProductGlobal();
            break;
        }
    }
}