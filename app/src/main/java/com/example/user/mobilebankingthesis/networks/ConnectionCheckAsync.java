package com.example.user.mobilebankingthesis.networks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.LoginActivity;
import com.example.user.mobilebankingthesis.activities.NoConnectionActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ConnectionCheckAsync extends AsyncTask<Object,Void,Boolean> {

    private Context context;

    private static final String TAG = ConnectionCheckAsync.class.getSimpleName();

    @Override
    protected Boolean doInBackground(Object... objects) {
        context = (Context) objects[0];
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.url_connection) + context.getResources().getString(R.string.php_dummy))
                    .post(body)
                    .build();

            client.newCall(request).execute();
            return true;

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Intent mainIntent = new Intent(context,LoginActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(mainIntent);

        }else {
            Intent mainIntent = new Intent(context,NoConnectionActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(mainIntent);
        }
    }
}
