package com.unibague.pazyregion.Shareds;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUserData {

    SharedPreferences sharedPreferences;

    public SharedPreferenceUserData(Context context){
        sharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
    }

    public void setEmailUser(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailUser", name);
        editor.commit();
    }

    public void setNameUser(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameUser", name);
        editor.commit();
    }



    public void setPhoneUser(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneUser", name);
        editor.commit();
    }

    public void setTypeUser(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("typeUser", name);
        editor.commit();
    }

    public String getNameUser(){
        String nameUser=sharedPreferences.getString("nameUser","");
        return nameUser;
    }

    public String getEmailUser(){
        String nameUser=sharedPreferences.getString("emailUser","");
        return nameUser;
    }

    public String getTypeUser(){
        String nameUser=sharedPreferences.getString("typeUser","");
        return nameUser;
    }

    public String getPhoneUser(){
        String nameUser=sharedPreferences.getString("phoneUser","");
        return nameUser;
    }
}
