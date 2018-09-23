package com.example.user.mobilebankingthesis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.mobilebankingthesis.R;
import com.example.user.mobilebankingthesis.adapters.ViewPagerAdapter;
import com.example.user.mobilebankingthesis.fragments.FragmentAccountActivity;
import com.example.user.mobilebankingthesis.fragments.FragmentAccountInfo;

public class AccountDetailActivity extends AppCompatActivity {

    private String accID;
    private final String TAG = this.getClass().getSimpleName();

    private ViewPager viewPager;

    public static Intent getIntent(Context context, String accID ) {
        Intent intent = new Intent(context, AccountDetailActivity.class);
        intent.putExtra("accID",accID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        accID = getIntent().getStringExtra("accID");

        viewPager = (ViewPager)findViewById(R.id.acc_detail_tab_viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.acc_detail_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.w(TAG, String.valueOf(tab.getPosition()));
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(FragmentAccountInfo.newInstance(accID), "Info");
        adapter.addFrag(new FragmentAccountActivity(), "Activity");
        viewPager.setAdapter(adapter);
    }


}
