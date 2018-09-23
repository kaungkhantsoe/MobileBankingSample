package com.example.user.mobilebankingthesis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.user.mobilebankingthesis.R;
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

public class AccountsOverViewActivity extends AppCompatActivity implements AccountDelegate {


    @BindView(R.id.rv_home)
    RecyclerView rv_home;

    UserSession userSession;

    AccountsRVAdapter accountsRVAdapter;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AccountsOverViewActivity.class);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_overview);

        userSession = new UserSession(this);

        ButterKnife.bind(this);
        AccountModel.getInstance(this).loadAccounts(userSession.getUserInfoSession().getUserID());


        rv_home.setHasFixedSize(true);
        rv_home.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        accountsRVAdapter = new AccountsRVAdapter(this, this);
        rv_home.setAdapter(accountsRVAdapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccLoadSuccess(ApiEvents.onAccountsLoadSuccessEvent onAccountLoadSuccessEvent) {
        accountsRVAdapter.appendNewData(onAccountLoadSuccessEvent.getAccountVOList());
    }

    /*
     * On getting error, show error message with toast
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ApiEvents.onErrorEvent onErrorEvent) {
        Toast.makeText(this,onErrorEvent.getError(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickAccount(AccountVO accountVO) {

        startActivity(AccountDetailActivity.getIntent(this,accountVO.getAccountNumber()));

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
