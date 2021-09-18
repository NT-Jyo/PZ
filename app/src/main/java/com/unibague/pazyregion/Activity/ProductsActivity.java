package com.unibague.pazyregion.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unibague.pazyregion.Adapters.RecycleViewAdapterProducts;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;
import com.unibague.pazyregion.Shareds.SharedPreferenceUserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView_product;
    RecycleViewAdapterProducts recycleViewAdapterProducts;

    FloatingActionButton button_floating_products;

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;

    SharedPreferenceUserData sharedPreferenceUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();
        firebaseCrashlyticsB= FirebaseCrashlytics.getInstance();

        button_floating_products = findViewById(R.id.button_floating_products);
        button_floating_products.setOnClickListener(this);

        recyclerView_product = findViewById(R.id.recycle_view_products);
        recyclerView_product.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sharedPreferenceUserData = new SharedPreferenceUserData(this);

        loadProduct();



    }

    @Override
    protected void onStart() {
        super.onStart();
        recycleViewAdapterProducts.startListening();
        FirebaseUser firebaseUser = firebaseAuthB.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.reload();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        recycleViewAdapterProducts.startListening();
    }


    private void loadRegisterProducts(){
        Intent intent = new Intent(ProductsActivity.this, ProductRegisterActivity.class);
        startActivity(intent);
    }

    private String loadEmail(){
        String emailUser= sharedPreferenceUserData.getEmailUser();
        return  emailUser;
    }

    private void loadProduct(){
        try{

            Query query = firebaseFirestoreB.collection("Product").whereEqualTo("email", loadEmail());
            FirestoreRecyclerOptions<Product> fireStoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
            recycleViewAdapterProducts = new RecycleViewAdapterProducts(fireStoreRecyclerOptions);
            recycleViewAdapterProducts.startListening();
            recyclerView_product.setAdapter(recycleViewAdapterProducts);


        }catch (Exception e){
            Log.w("TAG", "loadDataSubjects in failed", e);
            firebaseCrashlyticsB.log("loadDataSubjects");
            firebaseCrashlyticsB.recordException(e);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_floating_products:
                loadRegisterProducts();
                break;

        }
    }
}