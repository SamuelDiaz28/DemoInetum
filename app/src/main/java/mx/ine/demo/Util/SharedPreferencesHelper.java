package mx.ine.demo.Util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(@NonNull Context context){
        sharedPreferences = context.getSharedPreferences("InetumMX", Context.MODE_PRIVATE);
    }

    public boolean checkFirstRun(){
        return sharedPreferences.getBoolean("firstRun", true);
    }

    public void changeFirstRunValue(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstRun", value);
        editor.apply();
    }

    public void putToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token",token);
        editor.apply();
    }

    public void putIsTokenUpdated(Boolean updated) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTokenUpdated", updated);
        editor.apply();
    }


    public void removeToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Token");
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("Token", null);
    }

    public void onUserLogin(Integer id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUser", id);
        editor.putBoolean("session", true);
        editor.apply();
    }

    public void onUserLogout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUser", -1);
        editor.putBoolean("session", false);
        editor.apply();
    }

    public boolean isUserLogged() {
        return sharedPreferences.getBoolean("session",false);
    }

    public int getUserLoggedId() {
        return sharedPreferences.getInt("idUser",-1);
    }


}
