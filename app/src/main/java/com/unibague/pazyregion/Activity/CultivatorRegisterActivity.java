package com.unibague.pazyregion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unibague.pazyregion.Model.Cultivator;
import com.unibague.pazyregion.R;

import java.util.regex.Pattern;

public class CultivatorRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout text_layout_input_cultivator_email;
    TextInputLayout text_layout_input_cultivator_password;
    TextInputLayout text_layout_input_cultivator_name;
    TextInputLayout text_layout_input_cultivator_surName;
    TextInputLayout text_layout_input_cultivator_direction;
    TextInputLayout text_layout_input_cultivator_phone;
    TextInputLayout text_layout_input_cultivator_description;

    TextInputEditText text_input_cultivator_email;
    TextInputEditText text_input_cultivator_password;
    TextInputEditText text_input_cultivator_name;
    TextInputEditText text_input_cultivator_surName;
    TextInputEditText text_input_cultivator_direction;
    TextInputEditText text_input_cultivator_phone;
    TextInputEditText text_input_cultivator_description;


    MaterialButton material_button_cultivator;

    FirebaseAuth firebaseAuthB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    FirebaseFirestore firebaseFirestoreB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivator_register);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseCrashlyticsB=FirebaseCrashlytics.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();

        text_layout_input_cultivator_email =findViewById(R.id.text_layout_input_cultivator_email);
        text_layout_input_cultivator_password=findViewById(R.id.text_layout_input_cultivator_password);
        text_layout_input_cultivator_name=findViewById(R.id.text_layout_input_cultivator_name);
        text_layout_input_cultivator_surName=findViewById(R.id.text_layout_input_cultivator_surName);
        text_layout_input_cultivator_direction=findViewById(R.id.text_layout_input_cultivator_direction);
        text_layout_input_cultivator_phone=findViewById(R.id.text_layout_input_cultivator_phone);
        text_layout_input_cultivator_description=findViewById(R.id.text_layout_input_cultivator_description);

        text_input_cultivator_email=findViewById(R.id.text_input_cultivator_email);
        text_input_cultivator_password=findViewById(R.id.text_input_cultivator_password);
        text_input_cultivator_name=findViewById(R.id.text_input_cultivator_name);
        text_input_cultivator_surName=findViewById(R.id.text_input_cultivator_surName);
        text_input_cultivator_direction=findViewById(R.id.text_input_cultivator_direction);
        text_input_cultivator_phone=findViewById(R.id.text_input_cultivator_phone);
        text_input_cultivator_description=findViewById(R.id.text_input_cultivator_description);

        material_button_cultivator=findViewById(R.id.material_button_cultivator);
        material_button_cultivator.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuthB.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }



    private void RegisterCultivator() {

        try{
            String emailValidate = text_input_cultivator_email.getText().toString();
            String passwordValidate = text_input_cultivator_password.getText().toString();
            String nameValidate = text_input_cultivator_name.getText().toString();
            String surNameValidate = text_input_cultivator_surName.getText().toString();
            String directionValidate = text_input_cultivator_direction.getText().toString();
            String phoneValidate = text_input_cultivator_phone.getText().toString();
            String descriptionValidate = text_input_cultivator_description.getText().toString();


            if(!emailValidate.isEmpty() && !passwordValidate.isEmpty() && !nameValidate.isEmpty() &&  !surNameValidate.isEmpty() &&  !directionValidate.isEmpty() &&  !phoneValidate.isEmpty() &&  ! descriptionValidate.isEmpty()){

                text_layout_input_cultivator_email.setError(null);
                text_layout_input_cultivator_password.setError(null);
                text_layout_input_cultivator_name.setError(null);
                text_layout_input_cultivator_surName.setError(null);
                text_layout_input_cultivator_direction.setError(null);
                text_layout_input_cultivator_phone.setError(null);
                text_layout_input_cultivator_description.setError(null);


                if(!validateEmail(emailValidate)){
                    text_layout_input_cultivator_email.setError("Email no válido");
                }else {
                    Cultivator cultivator = new Cultivator(emailValidate,passwordValidate,nameValidate,surNameValidate,directionValidate,phoneValidate,descriptionValidate,"cultivator");
                    firebaseFirestoreB.collection("Users").document(emailValidate).set(cultivator).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firebaseFirestoreB.collection("Cultivator").document(emailValidate).set(cultivator).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    RegisterUserFirebase(emailValidate,passwordValidate);
                                }
                            });
                        }
                    });

                }

            }if(emailValidate.isEmpty()){
                text_layout_input_cultivator_email.setError("Este campo es necesario");
                text_layout_input_cultivator_email.requestFocus();
            }if(passwordValidate.isEmpty()){
                text_layout_input_cultivator_password.setError("Este campo es necesario");
                text_layout_input_cultivator_password.requestFocus();
            }if(nameValidate.isEmpty()){
                text_layout_input_cultivator_name.setError("Este campo es necesario");
                text_layout_input_cultivator_name.requestFocus();
            }if(surNameValidate.isEmpty()){
                text_layout_input_cultivator_surName.setError("Este campo es necesario");
                text_layout_input_cultivator_surName.requestFocus();
            }if(directionValidate.isEmpty()){
                text_layout_input_cultivator_direction.setError("Este campo es necesario");
                text_layout_input_cultivator_direction.requestFocus();
            }if(phoneValidate.isEmpty()){
                text_layout_input_cultivator_phone.setError("Este campo es necesario");
                text_layout_input_cultivator_phone.requestFocus();
            }if(descriptionValidate.isEmpty()){
                text_layout_input_cultivator_description.setError("Este campo es necesario");
                text_layout_input_cultivator_description.requestFocus();
            }
        }catch (Exception e){
            firebaseCrashlyticsB.log("RegisterCultivator");
            firebaseCrashlyticsB.recordException(e);
        }
    }


    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void RegisterUserFirebase(String email, String password){
        firebaseAuthB.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuthB.getCurrentUser();
                    updateUI(user);
                }else{
                    Toast.makeText(CultivatorRegisterActivity.this, "Este email ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(CultivatorRegisterActivity.this, HomeCultivatorActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_cultivator:
                RegisterCultivator();
                break;
        }
    }

}