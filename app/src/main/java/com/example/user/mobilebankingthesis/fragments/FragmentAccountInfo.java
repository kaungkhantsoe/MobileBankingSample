package com.example.user.mobilebankingthesis.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.AccountDetailActivity;
import com.example.user.mobilebankingthesis.data.models.AccountModel;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.sessions.UserSession;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAccountInfo extends Fragment {

    @BindView(R.id.tab_account_balance)
    TextView tab_account_balance;

    @BindView(R.id.tab_account_number)
    TextView tab_account_number;

    @BindView(R.id.tab_customerID)
    TextView tab_customerID;

    @BindView(R.id.tab_customer_name)
    TextView tab_customer_name;

    @BindView(R.id.tab_account_type)
    TextView tab_account_type;

    @BindView(R.id.tab_account_opening_date)
    TextView tab_account_opening_date;

    private Context context;

    private String accID;
    private UserSession userSession;

    public static FragmentAccountInfo newInstance(String parameter) {

        Bundle args = new Bundle();
        args.putString("parameter", parameter);
        FragmentAccountInfo fragment = new FragmentAccountInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        userSession = new UserSession(context);

        if (getArguments() != null) {
            accID = getArguments().getString("parameter");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);

        ButterKnife.bind(this,rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HashMap<String,AccountVO> hashMap = AccountModel.getAccountsHashMap();

        tab_account_balance.setText(hashMap.get(accID).getAccountAmmount());
        tab_account_number.setText(accID);
        tab_account_opening_date.setText(hashMap.get(accID).getAccountOpeningDate());
        tab_account_type.setText(hashMap.get(accID).getAccountType());
        tab_customer_name.setText(userSession.getUserInfoSession().getUserName());
        tab_customerID.setText(userSession.getUserInfoSession().getUserID());
    }
}
