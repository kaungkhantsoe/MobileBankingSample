package com.example.user.mobilebankingthesis.data.models;

import android.content.Context;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.events.ApiEvents;
import com.example.user.mobilebankingthesis.helpers.CryptographyHelper;
import com.example.user.mobilebankingthesis.networks.ExchangePublicKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
    static int userID;

    static byte[] mPrivateKey,mPublicKey, sharedKey;

    static HashMap<String, AccountVO> accountsHashMap;

    public AccountModel() {
        EventBus.getDefault().register(this);
        accountsHashMap = new HashMap<>();
    }

    public static AccountModel getInstance(Context mcontext) {
        context = mcontext;
        if (accountModel == null) {
            return new AccountModel();
        }
        return accountModel;
    }

    public static HashMap<String, AccountVO> getAccountsHashMap() {
        return accountsHashMap;
    }

    public void loadAccounts(String userID) {

        CryptographyHelper.prepareCurve();

        mPrivateKey = CryptographyHelper.getInstance().getPrivateKey();
        mPublicKey = CryptographyHelper.getInstance().getPublicKey();

        ExchangePublicKey.getInstance().Exchange(context,userID,mPublicKey);

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReceivePublickKey(ApiEvents.onReceivePublicKeyEvent onReceivePublicKeyEvent) {
        sharedKey = CryptographyHelper.getInstance()
                .getSharedKey(onReceivePublicKeyEvent.getPublicKey(),
                        mPrivateKey);

        String encryptedMessage = CryptographyHelper.getInstance().Encrypt(sharedKey, formatedStringText());

        String url = context.getResources().getString(R.string.url_connection);
        String php = context.getResources().getString(R.string.php_get_user_account);

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(context.getResources().getString(R.string.key_userID),encryptedMessage)
                    .build();
            Request request = new Request.Builder()
                    .url(url + php)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String jsonData = responseBody.string();

            List<AccountVO> list = parseJsonData(jsonData);

            ApiEvents.onAccountsLoadSuccessEvent onAccountLoadSuccessEvent = new ApiEvents.onAccountsLoadSuccessEvent(list);

            EventBus.getDefault().post(onAccountLoadSuccessEvent);

        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private List<AccountVO> parseJsonData(String jsonData) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonData);

        List<AccountVO> accountVOList = new ArrayList<>();

        for (int i = 0 ; i < jsonArray.length() ; i++) {

            AccountVO accountVO = new AccountVO();

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String accountNumber = CryptographyHelper.getInstance().Decrypt(sharedKey,jsonObject.getString(context.getResources().getString(R.string.key_accountNumber)));
            String accountAmmount = CryptographyHelper.getInstance().Decrypt(sharedKey,jsonObject.getString(context.getResources().getString(R.string.key_accountAmount)));
            String accountType = CryptographyHelper.getInstance().Decrypt(sharedKey,jsonObject.getString(context.getResources().getString(R.string.key_accountType)));
            String accountOpeningDate = CryptographyHelper.getInstance().Decrypt(sharedKey,jsonObject.getString(context.getResources().getString(R.string.key_accountOpeningDate)));

            accountVO.setAccountNumber(accountNumber);
            accountVO.setAccountAmmount(accountAmmount);
            accountVO.setAccountType(accountType);
            accountVO.setAccountOpeningDate(accountOpeningDate);

            accountVOList.add(accountVO);
            accountsHashMap.put(accountVO.getAccountNumber(),accountVO);
        }

        return accountVOList;
    }

    public String formatedStringText() {
        return String.valueOf(userID);
    }

}
