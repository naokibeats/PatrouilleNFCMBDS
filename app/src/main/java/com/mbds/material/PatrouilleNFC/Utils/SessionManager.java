package com.mbds.material.PatrouilleNFC.Utils;

/**
 * Created by Safidimahefa Razafimahafaly on 17-Aug-15.
 */
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mbds.material.PatrouilleNFC.LoginActivity;

public class SessionManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PatrouillePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    public static final String KEY_MATR = "matricule";

    public static final String KEY_ID = "id";

    public static final String KEY_USER = "iduser";
    public  static final String KEY_PRIVILEGE  = "privilege";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String matricule, String idpersonne, String privilege, String iduser){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, name);

        editor.putString(KEY_MATR, matricule);

        editor.putString(KEY_ID, idpersonne)   ;

        editor.putString(KEY_PRIVILEGE, privilege)   ;

        editor.putString(KEY_USER,iduser);

        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user matricule
        user.put(KEY_MATR, pref.getString(KEY_MATR, null));

        //user id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        user.put(KEY_PRIVILEGE, pref.getString(KEY_PRIVILEGE, null));

        user.put(KEY_USER, pref.getString(KEY_USER, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



}