package com.yb.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yb.funny.R;
import com.yb.funny.entity.User;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.SharedpreferencesUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 登录界面
 * Created by Yangbin on 2017/1/8.
 */

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    @ViewInject(R.id.login_username)
    private  EditText username;
    @ViewInject(R.id.login_password)
    private  EditText password;
    @ViewInject(R.id.tbHeadBar)
    private  Toolbar mTbHeadBar;

//
//    @ViewInject(R.id.login)
//    private  Button login;
//    @ViewInject(R.id.login_register)
//    private  Button register;
    @ViewInject(R.id.toolbar_title)
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText(getString(R.string.login));
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 用户登录功能
     * @param view
     */
    @Event(R.id.login)
    private void login(View view){
        //将url直接添加到参数里面
        RequestParams params = new RequestParams(Constant.URI+"user");
        params.setMultipart(true);
               params.addBodyParameter("method","login");
        params.addBodyParameter("username",username.getText().toString() );
        params.addBodyParameter("password", password.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                String userinfo = s.substring(1, s.length() - 1);
                if (userinfo.length()>0) {
                    Toast.makeText(x.app(), getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                    LoginUser.getInstance().setUser(JSON.parseObject(userinfo, User.class));
                    SharedpreferencesUtil.setSharedPreference(x.app(), Constant.USER_INFO, userinfo);
                    Intent intent = new Intent();
                    intent.setAction("action.refreshUserInfo");
                    sendBroadcast(intent);
                    finish();
                }else {
                    Toast.makeText(x.app(), getString(R.string.LoginError), Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Event(R.id.login_register)
    private void register(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
