package com.example.user.mobilebankingthesis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.activities.AccountDetailActivity;
import com.example.user.mobilebankingthesis.activities.AccountsOverViewActivity;
import com.example.user.mobilebankingthesis.activities.OwnTransferActivity;
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

public class FragmentHome extends Fragment implements View.OnClickListener {

    @BindView(R.id.fragment_home_accounts_img)
    ImageView fragment_home_accounts_img;

    @BindView(R.id.fragment_home_beneficiary_img)
    ImageView fragment_home_beneficiary_img;

    @BindView(R.id.fragment_home_help_img)
    ImageView fragment_home_help_img;

    @BindView(R.id.fragment_home_own_transfer_img)
    ImageView fragment_home_own_transfer_img;

    @BindView(R.id.fragment_home_transfer_other_img)
    ImageView fragment_home_transfer_other_img;

    @BindView(R.id.fragment_home_ammount_tv)
    TextView fragment_home_ammount_tv;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment_home_accounts_img.setOnClickListener(this);
        fragment_home_beneficiary_img.setOnClickListener(this);
        fragment_home_help_img.setOnClickListener(this);
        fragment_home_own_transfer_img.setOnClickListener(this);
        fragment_home_transfer_other_img.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccLoadSuccess(ApiEvents.onAccountsLoadSuccessEvent onAccountLoadSuccessEvent) {
        double ammount = 0;
        for (AccountVO accountVO : onAccountLoadSuccessEvent.getAccountVOList()) {
            ammount += Double.parseDouble(accountVO.getAccountAmmount());
        }
        fragment_home_ammount_tv.setText(String.valueOf(ammount));
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

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.fragment_home_accounts_img:
                context.startActivity(AccountsOverViewActivity.getIntent(context));
                break;
            case R.id.fragment_home_beneficiary_img:
                break;
            case R.id.fragment_home_help_img:
                break;
            case R.id.fragment_home_own_transfer_img:
                context.startActivity(OwnTransferActivity.getIntent(context));
                break;
            case R.id.fragment_home_transfer_other_img:
                break;
        }
    }
}
