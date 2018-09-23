package com.example.user.mobilebankingthesis.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.mobilebankingthesis.networks.ConnectionCheckAsync;
import com.example.user.mobilebankingthesis.R;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = LoadingActivity.class.getSimpleName();
    ProgressBar progressBar;
    Boolean connectionCondition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_layout);

        progressBar = findViewById(R.id.myProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        checkConnection();

    }

    private void checkConnection() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        Object object = LoadingActivity.this;

        if (netInfo != null ) {
            if (netInfo.isConnectedOrConnecting()) {

                ConnectionCheckAsync connectionCheckAsync = new ConnectionCheckAsync();
                connectionCheckAsync.execute(object);
            }
        }
    }
}
