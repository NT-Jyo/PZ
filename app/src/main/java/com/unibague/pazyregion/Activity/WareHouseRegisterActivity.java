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
import com.unibague.pazyregion.Model.WareHouse;
import com.unibague.pazyregion.R;

import java.util.regex.Pattern;

public class WareHouseRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout text_layout_input_ware_house_email;
    TextInputLayout text_layout_input_ware_house_password;
    TextInputLayout text_layout_input_ware_house_name;
    TextInputLayout text_layout_input_ware_house_direction;
    TextInputLayout text_layout_input_ware_house_phone;
    TextInputLayout text_layout_input_ware_house_description;

    TextInputEditText text_input_ware_house_email;
    TextInputEditText text_input_ware_house_password;
    TextInputEditText text_input_ware_house_name;
    TextInputEditText text_input_ware_house_direction;
    TextInputEditText text_input_ware_house_phone;
    TextInputEditText text_input_ware_house_description;

    MaterialButton material_button_ware_house;

    FirebaseAuth firebaseAuthB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    FirebaseFirestore firebaseFirestoreB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_register);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseCrashlyticsB=FirebaseCrashlytics.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();

        text_layout_input_ware_house_email =findViewById(R.id.text_layout_input_ware_house_email);
        text_layout_input_ware_house_password=findViewById(R.id.text_layout_input_ware_house_password);
        text_layout_input_ware_house_name=findViewById(R.id.text_layout_input_ware_house_name);
        text_layout_input_ware_house_direction=findViewById(R.id.text_layout_input_ware_house_direction);
        text_layout_input_ware_house_phone=findViewById(R.id.text_layout_input_ware_house_phone);
        text_layout_input_ware_house_description=findViewById(R.id.text_layout_input_ware_house_description);

        text_input_ware_house_email=findViewById(R.id.text_input_ware_house_email);
        text_input_ware_house_password=findViewById(R.id.text_input_ware_house_password);
        text_input_ware_house_name=findViewById(R.id.text_input_ware_house_name);
        text_input_ware_house_direction=findViewById(R.id.text_input_ware_house_direction);
        text_input_ware_house_phone=findViewById(R.id.text_input_ware_house_phone);
        text_input_ware_house_description=findViewById(R.id.text_input_ware_house_description);

        material_button_ware_house=findViewById(R.id.material_button_ware_house);
        material_button_ware_house.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuthB.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void RegisterWareHouse() {

        try{
            String emailValidate = text_input_ware_house_email.getText().toString();
            String passwordValidate = text_input_ware_house_password.getText().toString();
            String nameValidate = text_input_ware_house_name.getText().toString();
            String directionValidate = text_input_ware_house_direction.getText().toString();
            String phoneValidate = text_input_ware_house_phone.getText().toString();
            String descriptionValidate = text_input_ware_house_description.getText().toString();


            if(!emailValidate.isEmpty() && !passwordValidate.isEmpty() && !nameValidate.isEmpty() &&  !directionValidate.isEmpty() &&  !phoneValidate.isEmpty() &&  ! descriptionValidate.isEmpty()){

                text_layout_input_ware_house_email.setError(null);
                text_layout_input_ware_house_password.setError(null);
                text_layout_input_ware_house_name.setError(null);
                text_layout_input_ware_house_direction.setError(null);
                text_layout_input_ware_house_phone.setError(null);
                text_layout_input_ware_house_description.setError(null);



                if(!validateEmail(emailValidate)){
                    text_layout_input_ware_house_email.setError("Email no válido");
                }else {
                    WareHouse wareHouse = new WareHouse(emailValidate,passwordValidate,nameValidate,directionValidate,phoneValidate,descriptionValidate,"wareHouse");

                    firebaseFirestoreB.collection("WareHouse").document(emailValidate).set(wareHouse);
                    RegisterUserFirebase(emailValidate,passwordValidate);
                }

            }if(emailValidate.isEmpty()){
                text_layout_input_ware_house_email.setError("Este campo es necesario");
                text_layout_input_ware_house_email.requestFocus();
            }if(passwordValidate.isEmpty()){
                text_layout_input_ware_house_password.setError("Este campo es necesario");
                text_layout_input_ware_house_password.requestFocus();
            }if(nameValidate.isEmpty()){
                text_layout_input_ware_house_name.setError("Este campo es necesario");
                text_layout_input_ware_house_name.requestFocus();
            }if(directionValidate.isEmpty()){
                text_layout_input_ware_house_direction.setError("Este campo es necesario");
                text_layout_input_ware_house_direction.requestFocus();
            }if(phoneValidate.isEmpty()){
                text_layout_input_ware_house_phone.setError("Este campo es necesario");
                text_layout_input_ware_house_phone.requestFocus();
            }if(descriptionValidate.isEmpty()){
                text_layout_input_ware_house_description.setError("Este campo es necesario");
                text_layout_input_ware_house_description.requestFocus();
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
                    Toast.makeText(WareHouseRegisterActivity.this, "Este email ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(WareHouseRegisterActivity.this, HomeCultivatorActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_ware_house:
                RegisterWareHouse();
                break;
        }
    }
}