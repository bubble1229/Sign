package com.highersun.sign.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.highersun.sign.R;
import com.highersun.sign.ui.sign.SignActivity;
import com.highersun.sign.ui.sign1.Sign1Activity;
import com.highersun.sign.ui.status.StatusActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends TabActivity implements View.OnClickListener {
    TabHost tabHost;

    String userName;
    String userId;


    @Bind(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.ll_1)
    LinearLayout ll1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.ll_2)
    LinearLayout ll2;
    @Bind(R.id.ll_tab)
    LinearLayout llTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        initDate();


    }

    protected void initDate() {
        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent,intent1;
        intent = new Intent().setClass(this, Sign1Activity.class).putExtra("userId",userId).putExtra("userName",userName);//SignActivity
        spec = tabHost.newTabSpec("签到").setIndicator("签到").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, StatusActivity.class).putExtra("userId",userId);
        spec = tabHost.newTabSpec("统计").setIndicator("统计").setContent(intent);
        tabHost.addTab(spec);
        //m
        tabHost.setCurrentTab(0);
        changeView(1);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_1:
                tabHost.setCurrentTabByTag("签到");
                changeView(1);
                break;
            case R.id.ll_2:
                tabHost.setCurrentTabByTag("统计");
                changeView(2);
                break;
        }
    }

    private void changeView(int position) {
        switch (position) {
            case 1:
                iv1.setImageResource(R.drawable.icon_tab1_sel);
                tv1.setTextColor(getResources().getColor(R.color.font_menu));
                iv2.setImageResource(R.drawable.icon_tab2);
                tv2.setTextColor(getResources().getColor(R.color.font_gray_one));
                break;
            case 2:
                iv1.setImageResource(R.drawable.icon_tab1);
                tv1.setTextColor(getResources().getColor(R.color.font_gray_one));
                iv2.setImageResource(R.drawable.icon_tab2_sel);
                tv2.setTextColor(getResources().getColor(R.color.font_menu));
                break;
        }
    }
}
