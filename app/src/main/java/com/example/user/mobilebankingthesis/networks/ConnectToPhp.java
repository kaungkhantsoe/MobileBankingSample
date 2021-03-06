package com.example.user.mobilebankingthesis.networks;

import android.content.Context;

import com.example.user.mobilebankingthesis.R;
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

public class ConnectToPhp {

    static ConnectToPhp connectToPhp;

    public static ConnectToPhp getInstance() {

        if (connectToPhp == null) {
            return new ConnectToPhp();
        }
        return connectToPhp;
    }


    public void Connect(Context context, String phpScriptName, String encryptedMessage) {

        // Connect Php
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(context.getResources().getString(R.string.key_encryptedMessage), encryptedMessage)
                    .build();
            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.url_connection) + phpScriptName)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();


            if (responseBody.equals("")){
                ApiEvents.onErrorEvent onErrorEvent = new ApiEvents.onErrorEvent("Error receiving response");
                EventBus.getDefault().post(onErrorEvent);
            }else {

                String[] reply = parseResponseJson(responseBody.toString(),context);
                if (reply[0].equals("0")){
                    ApiEvents.onErrorEvent onErrorEvent = new ApiEvents.onErrorEvent("Error receiving response");
                    EventBus.getDefault().post(onErrorEvent);

                }else {
                    ApiEvents.onSuccessEvent onSuccessEvent = new ApiEvents.onSuccessEvent(reply[1]);
                    EventBus.getDefault().post(onSuccessEvent);

                }
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

    private String[] parseResponseJson(String jsonData,Context context) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonData);

        String replyCondition = context.getResources().getString(R.string.key_replyCondition);
        String replyMessage = context.getResources().getString(R.string.key_replyMessage);

        String[] reply = new String[2];
        reply[0] = jsonObject.getString(replyCondition);
        reply[1] = jsonObject.getString(replyMessage);

        return reply;
    }
}
