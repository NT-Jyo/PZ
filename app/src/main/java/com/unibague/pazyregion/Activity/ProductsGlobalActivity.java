package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unibague.pazyregion.Adapters.RecycleViewAdapterGlobalProduct;
import com.unibague.pazyregion.Adapters.RecycleViewAdapterProducts;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;

public class ProductsGlobalActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;

    RecyclerView recyclerView_global_product;
    RecycleViewAdapterGlobalProduct recycleViewAdapterGlobalProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_global);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseFirestoreB= FirebaseFirestore.getInstance();
        firebaseCrashlyticsB = FirebaseCrashlytics.getInstance();

        recyclerView_global_product = findViewById(R.id.recycle_view_global_product);
        recyclerView_global_product.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadGlobalProduct();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadProductGlobal(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadProductGlobal(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        recycleViewAdapterGlobalProduct.startListening();
        FirebaseUser firebaseUser = firebaseAuthB.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.reload();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        recycleViewAdapterGlobalProduct.startListening();
    }

    private void loadGlobalProduct(){
        try{

            Query query = firebaseFirestoreB.collection("Product");
            FirestoreRecyclerOptions<Product> fireStoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
            recycleViewAdapterGlobalProduct = new RecycleViewAdapterGlobalProduct(fireStoreRecyclerOptions);
            recycleViewAdapterGlobalProduct.startListening();
            recyclerView_global_product.setAdapter(recycleViewAdapterGlobalProduct);


        }catch (Exception e){
            Log.w("TAG", "loadDataSubjects in failed", e);
            firebaseCrashlyticsB.log("loadDataSubjects");
            firebaseCrashlyticsB.recordException(e);

        }
    }



    private void loadProductGlobal(String querySearch) {
        Query query = firebaseFirestoreB.collection("Product")
                .orderBy("title").startAt(querySearch).endAt(querySearch + "\uf8ff");
        FirestoreRecyclerOptions<Product> fireStoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
        recycleViewAdapterGlobalProduct = new RecycleViewAdapterGlobalProduct(fireStoreRecyclerOptions);
        recycleViewAdapterGlobalProduct.startListening();
        recyclerView_global_product.setAdapter(recycleViewAdapterGlobalProduct);
    }

    /**
     * action exit = remove preferences user Data, finish App
     * @param item
     * @return
     */

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
}