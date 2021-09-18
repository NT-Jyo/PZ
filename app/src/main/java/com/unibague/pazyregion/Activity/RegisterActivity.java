package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.unibague.pazyregion.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner_register;
    Button button_register_user;

    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner_register = findViewById(R.id.spinner_register);
        button_register_user=findViewById(R.id.button_register_user);

        button_register_user.setOnClickListener(this);

        loadSpinner();

    }


    private void loadSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.user, android.R.layout.simple_spinner_item);
        spinner_register.setAdapter(adapter);

        spinner_register.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(),"Selecciona un tipo de usuario", Toast.LENGTH_LONG).show();
            }
        });

    }



    private void loadRegister(){
        if(user.equals("Almacen")){
            Intent intent = new Intent(RegisterActivity.this, WareHouseRegisterActivity.class);
            startActivity(intent);
        }if(user.equals("Agricultor")){
            Intent intent = new Intent(RegisterActivity.this, CultivatorRegisterActivity.class);
            startActivity(intent);
        } if(user.equals("Transportador")){
            Intent intent = new Intent(RegisterActivity.this, ConveyorRegisterActivity.class);
            startActivity(intent);
        }if(user.equals("Comprador")){
            Intent intent = new Intent(RegisterActivity.this, UserRegisterActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(RegisterActivity.this,"Debes seleccionar un tipo de usuario" , Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_register_user:
                loadRegister();
                break;
        }

    }
}