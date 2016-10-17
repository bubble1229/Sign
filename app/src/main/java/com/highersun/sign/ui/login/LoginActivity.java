package com.highersun.sign.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.highersun.sign.R;
import com.highersun.sign.ui.BaseActivity;
import com.highersun.sign.ui.MainActivity;
import com.highersun.sign.ui.sign.SignActivity;
import com.highersun.sign.utils.AppTools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/8/5.
 */

public class LoginActivity extends BaseActivity  implements LoginContract.View{
    private LoginPresenter mPresenter;
    private MaterialDialog dialog;
    @Bind(R.id.bt_login)
    Button login;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_pass)
    EditText et_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        dialog = new MaterialDialog.Builder(this).content("数据加载中").progress(true, 0).build();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login(et_phone.getText().toString().trim(),et_pass.getText().toString().trim());
            }
        });

        //自动登录
        if(AppTools.isLogin(this)){

            startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("userId",
                    AppTools.getUserId(this)).putExtra("userName",AppTools.getUserName(this)));
            finish();
        }
    }
    @Override
    public void showLoading() {
        if (!isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (!isFinishing() && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void startActivity(String userId,String userName) {
        AppTools.setUserId(this,userId);
        AppTools.setUserName(this,userName);
        startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("userId",userId).putExtra("userName",userName));
        finish();
    }

    @Override
    public void showUserNameError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showPassWordError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveLoginStatue(boolean isLogin) {
        AppTools.setIsLogin(this,isLogin);
    }
}
