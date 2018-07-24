package com.example.hleks.digitalq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Hleks on 2018/06/27.
 */

public class SessionManagement{
    //Shared preference reference
    private SharedPreferences sPref = null;
    //Shared preference editor reference
    private SharedPreferences.Editor sPEditor = null;
    //Context
    private Context _context = null;
    //Preference file name
    private static final String SP_FILE_NAME = "userDetails";
    //Login status tracker
    private static final String LOGGED_IN = "isLoggedIn";
    //Web service API key
    private static final String API_KEY = "apiKey";
    //Base URL
    public static final String BASE_URL = "http://192.168.43.251/ci_project/index.php/mobile/";

    public SessionManagement(Context c){
        _context = c;
        sPref = _context.getSharedPreferences(SP_FILE_NAME, 0);
        sPEditor = sPref.edit();
    }

    //Session information recording
    public void userSession(String api){
        //Store login state
        sPEditor.putBoolean(LOGGED_IN, true);
        //Store the API Key
        sPEditor.putString(API_KEY, api);
        //Commit changes
        sPEditor.commit();
    }

    //Check user login status
    public void loggedIn(){
        if (!isLoggedIn()){
            Intent transition = new Intent(_context, LoginActivity.class);
            //Close all activities from the stack
            transition.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Add new activity in the stack
            transition.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(transition);
        }
    }

    public boolean isLoggedIn(){
        return sPref.getBoolean(LOGGED_IN, false);
    }

    public HashMap<String, String> getSessionData(){
        HashMap<String, String> info = new HashMap<>();

        info.put(API_KEY, sPref.getString(API_KEY, null));

        return info;
    }

    public void logout(){
        //Clear user data in shared preferences
        sPEditor.clear();
        sPEditor.commit();

        Intent transition = new Intent(_context, LoginActivity.class);
        //Close all activities from the stack
        transition.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Add new activity in the stack
        transition.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(transition);
    }
}
