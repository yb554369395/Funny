package com.yb.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.funny.R;
import com.yb.funny.entity.Order;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 订单显示界面
 * Created by Yangbin on 2017/4/25.
 */

@ContentView(R.layout.activity_order)
public class OrderActivity extends AppCompatActivity{

    @ViewInject(R.id.order_id)
    private TextView id;
    @ViewInject(R.id.order_type)
    private TextView type;
    @ViewInject(R.id.order_number)
    private TextView number;
    @ViewInject(R.id.order_amount)
    private TextView amount;
    @ViewInject(R.id.order_person)
    private TextView person;
    @ViewInject(R.id.order_time)
    private TextView time;
    @ViewInject(R.id.order_head)
    private Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    private  TextView toolbar_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText("订单详情");
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = new Intent();
        intent.setAction("action.refreshUserInfo");
        sendBroadcast(intent);

        Toast.makeText(this, "订单生成成功,请等待充值到账！", Toast.LENGTH_LONG).show();

        int orderid = Integer.parseInt(getIntent().getStringExtra("orderid"));

        RequestParams params = new RequestParams(Constant.URI + "order");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("orderid", orderid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                String orderinfo = s.substring(1, s.length() - 1);
                Order order = JSON.parseObject(orderinfo,Order.class);
                id.setText(order.getOrderid()+"");
                type.setText(order.getOrdertype()==1?"话费":"流量");
                number.setText(order.getPhonenumber()+"");
                amount.setText(order.getOrderamount()+(type.getText().toString().equals("话费")?"元":"M"));
                person.setText(LoginUser.getInstance().getUser().getName());
                time.setText(TimeUtil.getDate(order.getStarttime()));
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

    /**
     * Actionbar返回上个页面功能
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Event(R.id.order_ok)
    private void ok(View view){
        AppManager.getAppManager().finishActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}


