package com.example.user.mobilebankingthesis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.MainActivity;
import com.example.user.mobilebankingthesis.data.models.AccountModel;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.events.ApiEvents;
import com.example.user.mobilebankingthesis.helpers.CryptographyHelper;
import com.example.user.mobilebankingthesis.networks.ConnectToPhp;
import com.example.user.mobilebankingthesis.networks.ExchangePublicKey;
import com.example.user.mobilebankingthesis.sessions.UserSession;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentOwnTransfer extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.own_transfer_from_account)
    Spinner own_transfer_from_account;

    @BindView(R.id.own_transfer_to_account)
    Spinner own_transfer_to_account;

    @BindView(R.id.own_transfer_amount)
    EditText own_transfer_amount;

    @BindView(R.id.own_transfer_description)
    EditText own_transfer_description;

    @BindView(R.id.own_transfer_paynow)
    Button own_transfer_paynow;

    Context context;

    String[] AccArr;

    private String fromAcc = "";
    private String toAcc = "";
    private String ammount = "";

    byte[] mPublicKey,mPrivateKey;

    // UserSession
    UserSession userSession;
    String userID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        userSession = new UserSession(context);
        userID = userSession.getUserInfoSession().getUserID();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_transfer, container, false);

        ButterKnife.bind(this,view);

        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSpinner();

        own_transfer_paynow.setOnClickListener(this);
    }

    private void setupSpinner() {

        String userID = userSession.getUserInfoSession().getUserID();

        String[] accArr;
        HashMap<String,List<AccountVO>> accountVoHash = AccountModel.getInstance(context).getAccountsHashMap();
        List<AccountVO> accountVOS = accountVoHash.get(AccountModel.getInstance(context).getHashMapKeyName());

        accArr = new String[accountVOS.size()];

        // Add account Numbers to spinner array
        for (int i = 0 ; i < accountVOS.size() ; i++) {
            AccountVO accountVO = accountVOS.get(i);
            accArr[i] = accountVO.getAccountNumber();
        }

//        accArr = new String[]{"a", "b", "c"};

        // Add Hint to spinner Array
        AccArr = new String[(accArr.length)+1];
        AccArr[0] = "Select Account";
        for (int i = 0 ; i < accArr.length ; i++) {
            int temp = i;
            temp++;
            AccArr[temp] = accArr[i];
        }

        // Initializing FROM ArrayAdapter
        ArrayAdapter<String> fromSpinnerArrayAdapter = new ArrayAdapter<String>(
                context,android.R.layout.simple_spinner_item,AccArr){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        fromSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        own_transfer_from_account.setAdapter(fromSpinnerArrayAdapter);
        own_transfer_from_account.setOnItemSelectedListener(this);
        own_transfer_from_account.setAdapter(fromSpinnerArrayAdapter);


        // Initializing TO ArrayAdapter
        ArrayAdapter<String> toSpinnerArrayAdapter = new ArrayAdapter<String>(
                context,android.R.layout.simple_spinner_item,AccArr){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        toSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        own_transfer_to_account.setAdapter(toSpinnerArrayAdapter);
        own_transfer_to_account.setOnItemSelectedListener(this);
        own_transfer_to_account.setAdapter(toSpinnerArrayAdapter);

    }


    // Spinners onSelect
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position > 0){

            int viewId = parent.getId();

            switch (viewId){
                case R.id.own_transfer_from_account:
                    fromAcc = AccArr[position];
                    break;
                case R.id.own_transfer_to_account:
                    toAcc =AccArr[position];
                    break;
            }
            Log.w(TAG,AccArr[position] + " selected");

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Transfer now button onClick
    @Override
    public void onClick(View v) {

        ammount = own_transfer_amount.getText().toString();

        Log.w(TAG,"fromAcc " + fromAcc);
        // Check validations
        if (fromAcc.equals("")) {
            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.error_needFromAccountNumber),Toast.LENGTH_SHORT).show();
        }

        else if (toAcc.equals("")) {
            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.error_needToAccountNumberError),Toast.LENGTH_SHORT).show();
        }

        else if (fromAcc.equals(toAcc)) {

            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.error_identicalAccountNumberError),Toast.LENGTH_SHORT).show();
        }

        else if (ammount.equals("")) {
            Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.error_ammountTVEmptyError),Toast.LENGTH_SHORT).show();
        }

        // Transfer ammount
        else {
            ExchangePublicKey.getInstance().Exchange(context,userID,CryptographyHelper.getInstance().getPublicKey());
        }

    }


    /*
    * On receive public key, send encrypted message to php
    * */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReceivePublicKey(ApiEvents.onReceivePublicKeyEvent onReceivePublicKeyEvent) {
        byte[] sharedKey = CryptographyHelper.getInstance()
                .getSharedKey(onReceivePublicKeyEvent.getPublicKey(),
                            CryptographyHelper.getInstance().getPrivateKey());

        String encryptedMessage = CryptographyHelper.getInstance().Encrypt(sharedKey, formatedStringText());
        String phpName = context.getResources().getString(R.string.php_transferOwn);
        ConnectToPhp.getInstance().Connect(context,phpName,encryptedMessage);
    }


    /*
    * On getting error, show error message with toast
    * */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onErrorEvent(ApiEvents.onErrorEvent onErrorEvent) {
        Toast.makeText(context,onErrorEvent.getError(),Toast.LENGTH_SHORT).show();
    }


    /*
    * On success, show success message with toast
    * */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onTransferSuccess(ApiEvents.onSuccessEvent onTransferOwnSuccessEvent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        Toast.makeText(context,onTransferOwnSuccessEvent.getMessage(),Toast.LENGTH_SHORT).show();
    }

    public String formatedStringText() {
        return userID + "," + fromAcc + "," + toAcc + "," + ammount;
    }
}
