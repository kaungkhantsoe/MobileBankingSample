package com.example.user.mobilebankingthesis.data.vo;

import android.content.Context;

/**
 * Created by User on 3/1/2018.
 */

public class UserVO {

    private final String TAG = this.getClass().getSimpleName();

    String userName,userID;

    Context context;

    public UserVO(Context context) {
        this.context = context;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
