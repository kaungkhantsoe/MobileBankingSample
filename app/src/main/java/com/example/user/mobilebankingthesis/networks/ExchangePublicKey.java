package com.example.user.mobilebankingthesis.networks;

import android.content.Context;
import android.util.Base64;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.LoginActivity;
import com.example.user.mobilebankingthesis.data.vo.UserVO;
import com.example.user.mobilebankingthesis.events.ApiEvents;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ExchangePublicKey {

    static ExchangePublicKey exchangePublicKey;

    public static ExchangePublicKey getInstance() {

        if (exchangePublicKey == null) {
            return new ExchangePublicKey();
        }
        return exchangePublicKey;
    }

    public void Exchange(Context context, String userID, byte[] publickKey) {

        // Convert byte[] to string
        String sPublicKey = Base64.encodeToString(publickKey, Base64.DEFAULT);

        // Connect Php
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(context.getResources().getString(R.string.key_userID), userID)
                    .add(context.getResources().getString(R.string.key_mPublicKey), sPublicKey)
                    .build();
            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.my_url_connection) + context.getResources().getString(R.string.php_keyExchange))
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody.equals("")){
                ApiEvents.onErrorEvent onErrorEvent = new ApiEvents.onErrorEvent("Error receiving public key");
                EventBus.getDefault().post(onErrorEvent);
            }else {
                String reply = parseResponseJson(responseBody.toString(),context);
                ApiEvents.onReceivePublicKeyEvent onReceivePublicKeyEvent = new ApiEvents.onReceivePublicKeyEvent(reply);
                EventBus.getDefault().post(onReceivePublicKeyEvent);
            }

        }catch (IOException e){
            e.printStackTrace();
            ApiEvents.onErrorEvent onErrorEvent = new ApiEvents.onErrorEvent("Could not connect to server");
            EventBus.getDefault().post(onErrorEvent);
        }
        catch (JSONException e) {
            e.printStackTrace();
            ApiEvents.onErrorEvent onErrorEvent = new ApiEvents.onErrorEvent("Error loading data");
            EventBus.getDefault().post(onErrorEvent);
        }

    }

    private String parseResponseJson(String jsonData,Context context) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonData);

        String rPublicKey = context.getResources().getString(R.string.key_rPublicKey);

        String reply;
        reply = jsonObject.getString(rPublicKey);

        return reply;
    }
}
