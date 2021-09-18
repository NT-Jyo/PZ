package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unibague.pazyregion.Adapters.RecycleViewAdapterConveyors;
import com.unibague.pazyregion.Adapters.RecycleViewAdapterProducts;
import com.unibague.pazyregion.Model.Conveyor;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;

public class ConveyorsActivity extends AppCompatActivity {

    RecyclerView recyclerView_conveyors;
    RecycleViewAdapterConveyors recycleViewAdapterConveyors;

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveyors);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();
        firebaseCrashlyticsB= FirebaseCrashlytics.getInstance();

        recyclerView_conveyors = findViewById(R.id.recycle_view_conveyors);
        recyclerView_conveyors.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadConveyors();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recycleViewAdapterConveyors.startListening();
        FirebaseUser firebaseUser = firebaseAuthB.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.reload();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        recycleViewAdapterConveyors.startListening();
    }


    private void loadConveyors() {

        try{

            Query query = firebaseFirestoreB.collection("Conveyor");
            FirestoreRecyclerOptions<Conveyor> fireStoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Conveyor>().setQuery(query,Conveyor.class).build();
            recycleViewAdapterConveyors = new RecycleViewAdapterConveyors(fireStoreRecyclerOptions);
            recycleViewAdapterConveyors.startListening();
            recyclerView_conveyors.setAdapter(recycleViewAdapterConveyors);


        }catch (Exception e){
            Log.w("TAG", "loadDataSubjects in failed", e);
            firebaseCrashlyticsB.log("loadDataSubjects");
            firebaseCrashlyticsB.recordException(e);

        }
    }


}