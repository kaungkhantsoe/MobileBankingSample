package com.example.user.mobilebankingthesis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mobilebankingthesis.data.models.AccountModel;
import com.example.user.mobilebankingthesis.sessions.UserSession;

public class FragmentHome extends Fragment {

    Context context;

    UserSession userSession;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
        userSession = new UserSession(context);

        AccountModel.getInstance(context).loadAccounts(userSession.getUserInfoSession().getUserID());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
