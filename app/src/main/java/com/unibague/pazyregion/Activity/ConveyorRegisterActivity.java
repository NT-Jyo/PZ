package com.unibague.pazyregion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.unibague.pazyregion.Model.Conveyor;
import com.unibague.pazyregion.R;

import java.util.regex.Pattern;

public class ConveyorRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout text_layout_input_conveyor_email;
    TextInputLayout text_layout_input_conveyor_password;
    TextInputLayout text_layout_input_conveyor_name;
    TextInputLayout text_layout_input_conveyor_surName;
    TextInputLayout text_layout_input_conveyor_direction;
    TextInputLayout text_layout_input_conveyor_phone;
    TextInputLayout text_layout_input_conveyor_vehicle;
    TextInputLayout text_layout_input_conveyor_description;

    TextInputEditText text_input_conveyor_email;
    TextInputEditText text_input_conveyor_password;
    TextInputEditText text_input_conveyor_name;
    TextInputEditText text_input_conveyor_surName;
    TextInputEditText text_input_conveyor_direction;
    TextInputEditText text_input_conveyor_phone;
    TextInputEditText text_input_conveyor_vehicle;
    TextInputEditText text_input_conveyor_description;

    MaterialButton material_button_conveyor;

    FirebaseAuth firebaseAuthB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    FirebaseFirestore firebaseFirestoreB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveyor_register);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseCrashlyticsB=FirebaseCrashlytics.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();

        text_layout_input_conveyor_email =findViewById(R.id.text_layout_input_conveyor_email);
        text_layout_input_conveyor_password=findViewById(R.id.text_layout_input_conveyor_password);
        text_layout_input_conveyor_name=findViewById(R.id.text_layout_input_conveyor_name);
        text_layout_input_conveyor_surName=findViewById(R.id.text_layout_input_conveyor_surName);
        text_layout_input_conveyor_direction=findViewById(R.id.text_layout_input_conveyor_direction);
        text_layout_input_conveyor_phone=findViewById(R.id.text_layout_input_conveyor_phone);
        text_layout_input_conveyor_vehicle=findViewById(R.id.text_layout_input_conveyor_vehicle);
        text_layout_input_conveyor_description=findViewById(R.id.text_layout_input_conveyor_description);

        text_input_conveyor_email=findViewById(R.id.text_input_conveyor_email);
        text_input_conveyor_password=findViewById(R.id.text_input_conveyor_password);
        text_input_conveyor_name=findViewById(R.id.text_input_conveyor_name);
        text_input_conveyor_surName=findViewById(R.id.text_input_conveyor_surName);
        text_input_conveyor_direction=findViewById(R.id.text_input_conveyor_direction);
        text_input_conveyor_phone=findViewById(R.id.text_input_conveyor_phone);
        text_input_conveyor_vehicle=findViewById(R.id.text_input_conveyor_vehicle);
        text_input_conveyor_description=findViewById(R.id.text_input_conveyor_description);

        material_button_conveyor=findViewById(R.id.material_button_conveyor);
        material_button_conveyor.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuthB.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }


    private void RegisterConveyor(){
        try{
            String emailValidate = text_input_conveyor_email.getText().toString();
            String passwordValidate = text_input_conveyor_password.getText().toString();
            String nameValidate = text_input_conveyor_name.getText().toString();
            String surNameValidate = text_input_conveyor_surName.getText().toString();
            String directionValidate = text_input_conveyor_direction.getText().toString();
            String phoneValidate = text_input_conveyor_phone.getText().toString();
            String vehicleValidate = text_input_conveyor_vehicle.getText().toString();
            String descriptionValidate = text_input_conveyor_description.getText().toString();


            if(!emailValidate.isEmpty() && !passwordValidate.isEmpty() && !nameValidate.isEmpty() &&  !surNameValidate.isEmpty() &&  !directionValidate.isEmpty() &&  !phoneValidate.isEmpty() &&  !vehicleValidate.isEmpty() &&  ! descriptionValidate.isEmpty()){

                text_layout_input_conveyor_email.setError(null);
                text_layout_input_conveyor_password.setError(null);
                text_layout_input_conveyor_name.setError(null);
                text_layout_input_conveyor_surName.setError(null);
                text_layout_input_conveyor_direction.setError(null);
                text_layout_input_conveyor_phone.setError(null);
                text_layout_input_conveyor_vehicle.setError(null);
                text_layout_input_conveyor_description.setError(null);

                if(!validateEmail(emailValidate)){
                    text_layout_input_conveyor_email.setError("Email no válido");
                }else {

                    Conveyor conveyor = new Conveyor(emailValidate,passwordValidate,nameValidate,surNameValidate,directionValidate,phoneValidate,vehicleValidate,descriptionValidate,"conveyor");
                    firebaseFirestoreB.collection("Users").document(emailValidate).set(conveyor);
                    firebaseFirestoreB.collection("Conveyor").document(emailValidate).set(conveyor);
                    RegisterUserFirebase(emailValidate,passwordValidate);
                }

            }if(emailValidate.isEmpty()){
                text_layout_input_conveyor_email.setError("Este campo es necesario");
                text_layout_input_conveyor_email.requestFocus();
            }if(passwordValidate.isEmpty()){
                text_layout_input_conveyor_password.setError("Este campo es necesario");
                text_layout_input_conveyor_password.requestFocus();
            }if(nameValidate.isEmpty()){
                text_layout_input_conveyor_name.setError("Este campo es necesario");
                text_layout_input_conveyor_name.requestFocus();
            }if(surNameValidate.isEmpty()){
                text_input_conveyor_surName.setError("Este campo es necesario");
                text_input_conveyor_surName.requestFocus();
            }if(directionValidate.isEmpty()){
                text_input_conveyor_direction.setError("Este campo es necesario");
                text_input_conveyor_direction.requestFocus();
            }if(phoneValidate.isEmpty()){
                text_input_conveyor_phone.setError("Este campo es necesario");
                text_input_conveyor_phone.requestFocus();
            }if(vehicleValidate.isEmpty()){
                text_input_conveyor_vehicle.setError("Este campo es necesario");
                text_input_conveyor_vehicle.requestFocus();
            }if(descriptionValidate.isEmpty()){
                text_input_conveyor_description.setError("Este campo es necesario");
                text_input_conveyor_description.requestFocus();
            }
        }catch (Exception e){
            firebaseCrashlyticsB.log("RegisterConveyor");
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
                    Toast.makeText(ConveyorRegisterActivity.this, "Este email ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(ConveyorRegisterActivity.this, "Bienvenido.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_conveyor:
                RegisterConveyor();
                break;
        }
    }
}