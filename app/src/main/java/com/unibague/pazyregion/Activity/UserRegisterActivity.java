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
import com.unibague.pazyregion.Model.User;
import com.unibague.pazyregion.R;

import java.util.regex.Pattern;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout text_layout_input_user_email;
    TextInputLayout text_layout_input_user_password;
    TextInputLayout text_layout_input_user_name;
    TextInputLayout text_layout_input_user_phone;

    TextInputEditText text_input_user_email;
    TextInputEditText text_input_user_password;
    TextInputEditText text_input_user_name;
    TextInputEditText text_input_user_phone;


    MaterialButton material_button_user;

    FirebaseAuth firebaseAuthB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    FirebaseFirestore firebaseFirestoreB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseCrashlyticsB=FirebaseCrashlytics.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();

        text_layout_input_user_email =findViewById(R.id.text_layout_input_user_email);
        text_layout_input_user_password=findViewById(R.id.text_layout_input_user_password);
        text_layout_input_user_name=findViewById(R.id.text_layout_input_user_name);
        text_layout_input_user_phone=findViewById(R.id.text_layout_input_user_phone);

        text_input_user_email=findViewById(R.id.text_input_user_email);
        text_input_user_password=findViewById(R.id.text_input_user_password);
        text_input_user_name=findViewById(R.id.text_input_user_name);
        text_input_user_phone=findViewById(R.id.text_input_user_phone);

        material_button_user=findViewById(R.id.material_button_user);
        material_button_user.setOnClickListener(this);
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
            String emailValidate = text_input_user_email.getText().toString();
            String passwordValidate = text_input_user_password.getText().toString();
            String nameValidate = text_input_user_name.getText().toString();
            String phoneValidate = text_input_user_phone.getText().toString();


            if(!emailValidate.isEmpty() && !passwordValidate.isEmpty() && !nameValidate.isEmpty() && !phoneValidate.isEmpty()){

                text_layout_input_user_email.setError(null);
                text_layout_input_user_password.setError(null);
                text_layout_input_user_name.setError(null);
                text_layout_input_user_phone.setError(null);



                if(!validateEmail(emailValidate)){
                    text_layout_input_user_email.setError("Email no válido");
                }else {
                    User user = new User(emailValidate,passwordValidate,nameValidate,phoneValidate,"user");
                    firebaseFirestoreB.collection("Users").document(emailValidate).set(user);
                    firebaseFirestoreB.collection("User").document(emailValidate).set(user);

                    RegisterUserFirebase(emailValidate,passwordValidate);
                }

            }if(emailValidate.isEmpty()){
                text_layout_input_user_email.setError("Este campo es necesario");
                text_layout_input_user_email.requestFocus();
            }if(passwordValidate.isEmpty()){
                text_layout_input_user_password.setError("Este campo es necesario");
                text_layout_input_user_password.requestFocus();
            }if(nameValidate.isEmpty()){
                text_layout_input_user_name.setError("Este campo es necesario");
                text_layout_input_user_name.requestFocus();
            }if(phoneValidate.isEmpty()){
                text_layout_input_user_phone.setError("Este campo es necesario");
                text_layout_input_user_phone.requestFocus();
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
                    Toast.makeText(UserRegisterActivity.this, "Este email ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(UserRegisterActivity.this, HomeUserActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_user:
                RegisterWareHouse();
                break;
        }
    }
}