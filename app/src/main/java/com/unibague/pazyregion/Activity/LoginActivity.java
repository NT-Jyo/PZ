package com.unibague.pazyregion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.unibague.pazyregion.R;
import com.unibague.pazyregion.Shareds.SharedPreferenceUserData;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    TextInputLayout text_input_layout_email;
    TextInputLayout text_input_layout_password;

    TextInputEditText text_input_edit_email;
    TextInputEditText text_input_edit_password;

    TextView text_view_recover_password;

    MaterialButton button_material_login;
    MaterialButton button_register;
    MaterialButton button_login_user_test;

    FirebaseAuth firebaseAuthB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    FirebaseFirestore firebaseFirestoreB;

    SharedPreferenceUserData sharedPreferenceUserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseCrashlyticsB=FirebaseCrashlytics.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();

        sharedPreferenceUserData = new SharedPreferenceUserData(this);
        text_input_layout_email=findViewById(R.id.text_input_layout_email);
        text_input_layout_password=findViewById(R.id.text_input_layout_password);

        text_input_edit_email=findViewById(R.id.text_input_edit_email);
        text_input_edit_password=findViewById(R.id.text_input_edit_password);

        button_register= findViewById(R.id.button_login_register);
        button_material_login = findViewById(R.id.button_login_sign_in);
        button_login_user_test=findViewById(R.id.button_login_user_test);

        text_view_recover_password=findViewById(R.id.text_view_recover_password);

        button_material_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
        button_login_user_test.setOnClickListener(this);
        text_view_recover_password.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuthB.getCurrentUser();

    }

    private void LoginFirebase(){
        try{
            String emailValidate = text_input_edit_email.getText().toString().trim();
            String passwordValidate = text_input_edit_password.getText().toString().trim();

            if (!emailValidate.isEmpty() && !passwordValidate.isEmpty()) {
                text_input_layout_email.setError(null);
                text_input_layout_password.setError(null);

                if(!validateEmail(emailValidate)){
                    text_input_layout_email.setError("Email no válido");
                }else {
                    firebaseAuthB.signInWithEmailAndPassword(emailValidate,passwordValidate).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                CollectionReference user = firebaseFirestoreB.collection("Users");
                                Query query = user.whereEqualTo("email", emailValidate);
                                query.addSnapshotListener((queryDocumentSnapshots, e) -> {
                                    if(e!=null){
                                        FirebaseCrashlytics.getInstance().recordException(e);
                                    }else{
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String typeUser = documentSnapshot.get("userType").toString();

                                            if(typeUser.equals("cultivator")){
                                                sharedPreferenceUserData.setNameUser(documentSnapshot.get("name").toString());
                                                sharedPreferenceUserData.setTypeUser(typeUser);
                                                Intent intent = new Intent(LoginActivity.this, HomeCultivatorActivity.class);
                                                sharedPreferenceUserData.setEmailUser(documentSnapshot.get("email").toString());
                                                sharedPreferenceUserData.setPhoneUser(documentSnapshot.get("phone").toString());
                                                startActivity(intent);
                                            }if(typeUser.equals("conveyor")){
                                                Intent intent = new Intent(LoginActivity.this, HomeConveyorActivity.class);
                                                sharedPreferenceUserData.setEmailUser(documentSnapshot.get("email").toString());
                                                sharedPreferenceUserData.setPhoneUser(documentSnapshot.get("phone").toString());
                                                startActivity(intent);
                                            }if(typeUser.equals("user")){
                                                Intent intent = new Intent(LoginActivity.this, HomeUserActivity.class);
                                                sharedPreferenceUserData.setEmailUser(documentSnapshot.get("email").toString());
                                                sharedPreferenceUserData.setPhoneUser(documentSnapshot.get("phone").toString());
                                                startActivity(intent);
                                            }if(typeUser.equals("wareHouse")){
                                                sharedPreferenceUserData.setNameUser(documentSnapshot.get("name").toString());
                                                Intent intent = new Intent(LoginActivity.this, HomeCultivatorActivity.class);
                                                sharedPreferenceUserData.setTypeUser(typeUser);
                                                sharedPreferenceUserData.setEmailUser(documentSnapshot.get("email").toString());
                                                sharedPreferenceUserData.setPhoneUser(documentSnapshot.get("phone").toString());
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(LoginActivity.this,"Verifica los datos",  Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            } else {
                Toast.makeText(LoginActivity.this, "Ingrese los campos", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }



    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void loadRegister(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    private void recoverPassword() {
        try{
            String emailReset = text_input_edit_email.getText().toString().trim();

            if(!validateEmail(emailReset)){
                text_input_layout_email.setError("Email no válido");
            }else {
                firebaseAuthB.sendPasswordResetEmail(emailReset).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(LoginActivity.this,"Revisa la bandeja de entrada de tu correo electrónico", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this,"Correo electrónico no encontrado ", Toast.LENGTH_SHORT).show();
                            firebaseCrashlyticsB.recordException(task.getException());
                        }
                    }
                });
            }
        }catch (Exception e){
            firebaseCrashlyticsB.log("recoverPassword");
            firebaseCrashlyticsB.recordException(e);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_login_register:
                loadRegister();
                break;
            case R.id.button_login_sign_in:
                LoginFirebase();
                break;
            case R.id.text_view_recover_password:
                recoverPassword();
                break;
            case R.id.button_login_user_test:
                Intent intent = new Intent(LoginActivity.this,HomeUserActivity.class);
                startActivity(intent);
                break;
        }

    }
}