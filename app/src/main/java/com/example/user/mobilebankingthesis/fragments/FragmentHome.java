package com.example.user.mobilebankingthesis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.AccountDetailActivity;
import com.example.user.mobilebankingthesis.adapters.AccountsRVAdapter;
import com.example.user.mobilebankingthesis.data.models.AccountModel;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.delegates.AccountDelegate;
import com.example.user.mobilebankingthesis.events.ApiEvents;
import com.example.user.mobilebankingthesis.sessions.UserSession;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHome extends Fragment implements AccountDelegate {

    @BindView(R.id.rv_home)
    RecyclerView rv_home;

    Context context;

    UserSession userSession;

    AccountsRVAdapter accountsRVAdapter;

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

        ButterKnife.bind(this,view);

        rv_home.setHasFixedSize(true);
        rv_home.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        accountsRVAdapter = new AccountsRVAdapter(context, this);
        rv_home.setAdapter(accountsRVAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccLoadSuccess(ApiEvents.onAccountsLoadSuccessEvent onAccountLoadSuccessEvent) {
        accountsRVAdapter.appendNewData(onAccountLoadSuccessEvent.getAccountVOList());
    }

    @Override
    public void onClickAccount(AccountVO accountVO) {

        Intent intent = new Intent(context, AccountDetailActivity.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
