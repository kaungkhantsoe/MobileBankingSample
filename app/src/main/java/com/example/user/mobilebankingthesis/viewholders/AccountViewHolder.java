package com.example.user.mobilebankingthesis.viewholders;

import android.view.View;
import android.widget.TextView;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.delegates.AccountDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountViewHolder extends BaseViewHolder<AccountVO> {

    AccountDelegate accountDelegate;

    @BindView(R.id.tv_home_accountAmmount)
    public TextView tv_home_accountAmmount;

    @BindView(R.id.tv_home_accountNumber)
    public TextView tv_home_accountNumber;

    public AccountViewHolder(View itemView, AccountDelegate accountDelegate) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.accountDelegate = accountDelegate;
    }


    @Override
    public void setData(AccountVO data) {
        mData = data;

        tv_home_accountAmmount.setText(mData.getAccountAmmount());
        tv_home_accountNumber.setText(mData.getAccountNumber() + " MMK");
    }

    @Override
    public void onClick(View v) {
        accountDelegate.onClickAccount(mData);
    }
}
