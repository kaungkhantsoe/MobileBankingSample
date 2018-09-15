package com.example.user.mobilebankingthesis.sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.user.mobilebankingthesis.activities.LoginActivity;
import com.example.user.mobilebankingthesis.data.vo.UserVO;

/**
 * Created by User on 2/26/2018.
 */

public class UserSession {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREFER_NAME = "LoginActivity";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String USER_NAME_KEY = "userName";

    public static final String USER_ID_KEY = "userID";

    // Constructor
    public UserSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Create User Session
    public void createUserInfoSession(String userid,String username) {
        editor.putString(USER_ID_KEY,userid);
        editor.putString(USER_NAME_KEY,username);
        editor.commit();
    }

    // Get User Info
    public UserVO getUserInfoSession() {
        UserVO userInfo = new UserVO(_context);
        userInfo.setUserName(pref.getString(USER_NAME_KEY,""));
        userInfo.setUserID(pref.getString(USER_ID_KEY,""));
        return userInfo;
    }


    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to LoginActivity Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Staring LoginActivity Activity
            _context.startActivity(i);

            return false;
        }
        return true;
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){

        // set login false
        editor.putBoolean(IS_USER_LOGIN,false);
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring LoginActivity Activity
        _context.startActivity(i);


    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }



}
