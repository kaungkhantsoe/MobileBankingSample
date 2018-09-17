package com.example.user.mobilebankingthesis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.events.ApiEvents;
import com.example.user.mobilebankingthesis.helpers.CryptographyHelper;
import com.example.user.mobilebankingthesis.networks.ExchangePublicKey;
import com.example.user.mobilebankingthesis.sessions.UserSession;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.example.user.mobilebankingthesis.data.vo.UserVO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //User Session
    UserSession userSession;

    // Widget Session
    Button loginBtn;
    EditText userNameTxtview,passwordTextview;
    TextView forgotTxtview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        userSession = new UserSession(LoginActivity.this);

        userNameTxtview = findViewById(R.id.login_userNameTxtview);
        passwordTextview = findViewById(R.id.login_passwordTxtview);
        loginBtn = findViewById(R.id.login_btn);
        forgotTxtview = findViewById(R.id.login_forgetPasswordTxtview);

        loginBtn.setOnClickListener(this);
        forgotTxtview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                LogIn(userNameTxtview.getText().toString(),passwordTextview.getText().toString());
                break;
            case R.id.login_forgetPasswordTxtview:
                break;
        }
    }

    private void LogIn(String userName,String password) {

        if (!(userName.length()>0)) {
            Toast.makeText(LoginActivity.this,"Username Required !",Toast.LENGTH_SHORT).show();
        }else if (!(password.length()>0)) {
            Toast.makeText(LoginActivity.this,"Password Required !",Toast.LENGTH_SHORT).show();
        }else {
            try {
                OkHttpClient client = new OkHttpClient();
                Log.w(TAG,"executing");
                RequestBody body = new FormBody.Builder()
                        .add(this.getResources().getString(R.string.key_userName),userName)
                        .add(this.getResources().getString(R.string.key_userPassword),password)
                        .build();
                Request request = new Request.Builder()
                        .url(LoginActivity.this.getResources().getString(R.string.url_connection) + this.getResources().getString(R.string.php_login))
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String jsonData = responseBody.string();
                if (jsonData.equals("")) {
                    Toast.makeText(LoginActivity.this,"Username or password incorrect",Toast.LENGTH_SHORT).show();
                }else {
                    try {

                        UserVO parsedData = parseResponseJson(jsonData);
                        userSession.createUserInfoSession(parsedData.getUserID(),parsedData.getUserName());

                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }catch (IOException e){
                e.printStackTrace();

                // No Connection
                Intent mainIntent = new Intent(LoginActivity.this,NoConnectionActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }
        }
    }

    private UserVO parseResponseJson(String jsonData) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonData);

        String userID = this.getResources().getString(R.string.key_userID);
        String username = this.getResources().getString(R.string.key_userName);

        UserVO user = new UserVO(LoginActivity.this);
        user.setUserID(jsonObject.getString(userID));
        user.setUserName(jsonObject.getString(username));
        return user;
    }

}
