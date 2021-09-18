package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.unibague.pazyregion.R;

public class HomeConveyorActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_conveyor);

    }

    private void loadProducts(){
        Intent intent = new Intent(HomeConveyorActivity.this,ProductsActivity.class);
        startActivity(intent);
    }

    private void loadDomiciliary(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}