package com.example.user.mobilebankingthesis.data.models;

import android.content.Context;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AccountModel {

    private final String TAG = this.getClass().getSimpleName();

    static Context context;
    static AccountModel accountModel;

    HashMap<String,List<AccountVO>> accountsHashMap;

    public static AccountModel getInstance(Context mcontext) {
        context = mcontext;
        if (accountModel == null) {
            return new AccountModel();
        }
        return accountModel;
    }

    public HashMap<String, List<AccountVO>> getAccountsHashMap() {
        return accountsHashMap;
    }

    public String getHashMapKeyName() {
        return "accounts";
    }

    public void loadAccounts(String userID) {

        String url = context.getResources().getString(R.string.url_connection);
        String php = context.getResources().getString(R.string.php_get_user_account);

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(context.getResources().getString(R.string.key_userID),userID)
                    .build();
            Request request = new Request.Builder()
                    .url(url + php)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String jsonData = responseBody.string();

            accountsHashMap = parseJsonData(jsonData);

        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private HashMap<String,List<AccountVO>> parseJsonData(String jsonData) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonData);

        HashMap<String,List<AccountVO>> parsedData= new HashMap<String,List<AccountVO>>();

        List<AccountVO> accountVOList = new ArrayList<>();

        for (int i = 0 ; i < jsonArray.length() ; i++) {

            AccountVO accountVO = new AccountVO();

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String accountNumber = jsonObject.getString("accountNumber");
            String accountAmmount = jsonObject.getString("accountAmmount");
            accountVO.setAccountNumber(accountNumber);
            accountVO.setAccountAmmount(accountAmmount);

            accountVOList.add(accountVO);
        }

        parsedData.put("accounts", accountVOList);

        return parsedData;
    }

}
