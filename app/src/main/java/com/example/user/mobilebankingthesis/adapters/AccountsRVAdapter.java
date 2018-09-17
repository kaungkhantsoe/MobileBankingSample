package com.example.user.mobilebankingthesis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.data.vo.AccountVO;
import com.example.user.mobilebankingthesis.delegates.AccountDelegate;
import com.example.user.mobilebankingthesis.viewholders.AccountViewHolder;

public class AccountsRVAdapter extends BaseRecyclerAdapter<AccountViewHolder, AccountVO> {

    AccountDelegate accountDelegate;

    public AccountsRVAdapter(Context context, AccountDelegate accountDelegate) {
        super(context);
        this.accountDelegate = accountDelegate;
    }


    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new AccountViewHolder(view, accountDelegate);
    }
}
