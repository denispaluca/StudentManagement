package com.harryfultz.studentmanager.appConfigAndDB;


import android.content.Context;
import android.content.SharedPreferences;

 public class MessageStorage {


    private SharedPreferences sharedPreferences;

     public MessageStorage(Context context){
        this.sharedPreferences = context.getSharedPreferences("Messages", Context.MODE_PRIVATE);
    }

    public void setAbsenceMessage(String message){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AbsenceMessage", message);
        editor.apply();
    }

    public void setParentsMessage(String message){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ParentsMessage", message);
        editor.apply();
    }

    String getAbsenceMessage(){
        return sharedPreferences.getString("AbsenceMessage", "");
    }

    public String getParentsMessage(){
        return sharedPreferences.getString("ParentsMessage", "");
    }

}
