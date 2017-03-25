package com.yb.funny.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yb.funny.R;
import com.yb.funny.entity.Prize;
import com.yb.funny.entity.User;
import com.yb.funny.fragment.PrizeInfoFragment;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.Constant;
import com.yb.funny.util.IntegralUtil;
import com.yb.funny.util.LoginUser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 积分商城界面
 *
 * Created by Yangbin on 2017/1/22.
 */

@ContentView(R.layout.activity_prize)
public class PrizeActivity extends AppCompatActivity implements PrizeInfoFragment.OnPrizeSelectedListener{


    /**
     * 选项卡下划线长度
     */
    private static int lineWidth = 0;

    /**
     * 偏移量
     *		 （手机屏幕宽度/3-选项卡长度）/2
     */
    private static int offset = 0;

    /**
     * 选项卡总数
     */
    private static final int TAB_COUNT = 2;
    /**
     * 当前显示的选项卡位置
     */
    private int current_index = 0;

    private Prize prize;

    @ViewInject(R.id.prize_pager)
    private ViewPager vPager;
    @ViewInject(R.id.prize_tab1)
    private TextView bill;
    @ViewInject(R.id.prize_tab2)
    private TextView flow;
    @ViewInject(R.id.prize_name)
    private TextView name;
    @ViewInject(R.id.prize_integral)
    private TextView integral;
    @ViewInject(R.id.include2)
    private Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    private TextView toolbar_title;
//    @ViewInject(R.id.prize_ok)
//    private Button ok;
    @ViewInject(R.id.prize_number)
    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText(getString(R.string.integralMarket));
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initActivity();

        bill.setOnClickListener(new MyOnClickListener(0));
        flow.setOnClickListener(new MyOnClickListener(1));
        final TextView[] titles = {bill,flow};



        vPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return TAB_COUNT;
            }

            @Override
            public Fragment getItem(int index)//直接创建fragment对象并返回
            {
                switch (index) {
                    case 0:
                        return new PrizeInfoFragment(1);
                    case 1:
                        return new PrizeInfoFragment(2);
                }
                return null;
            }
        });
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int one = offset * 2 + lineWidth;// 页卡1 -> 页卡2 偏移量

            @Override
            public void onPageSelected(int index)//设置标题的颜色以及下划线的移动效果
            {
                Animation animation = new TranslateAnimation(one * current_index, one * index, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                titles[current_index].setTextColor(Color.BLACK);
                titles[index].setTextColor(Color.RED);
                current_index = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int index) {
            }
        });
        vPager.setCurrentItem(0);
        titles[1].setTextColor(Color.BLACK);
        titles[0].setTextColor(Color.RED);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshUserInfo");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    // broadcast receiver
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshUserInfo"))
            {
                initActivity();
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void initActivity(){
        User user = LoginUser.getInstance().getUser();
        name.setText(user.getName());
        integral.setText("积分：" + user.getIntegral());
    }

    /**
     * 回调函数，当Fragment中的奖品单选框被选中后，会调用回调函数将选中的奖品的参数传递到这个函数中进行处理
     * @param prize
     */
    @Override
    public void onPrizeSeletced(Prize prize) {
        this.prize = prize;
    }

    @Event(R.id.prize_ok)
    private void send(View view){
        if (prize==null){
            Toast.makeText(x.app(), getString(R.string.choosePrize), Toast.LENGTH_LONG).show();
        } else if (LoginUser.getInstance().getUser().getIntegral() < prize.getRequiredpoints()) {
            Toast.makeText(x.app(), getString(R.string.integralShortage), Toast.LENGTH_LONG).show();
        }else {
            RequestParams params = new RequestParams(Constant.URI + "order");
            params.setMultipart(true);
            params.addBodyParameter("method", "add");
            params.addBodyParameter("ordertype", prize.getPrizetype() + "");
            if (number.getText().toString().length() != 11) {
                Toast.makeText(x.app(), getString(R.string.inputnumber), Toast.LENGTH_LONG).show();
            } else {
                params.addBodyParameter("phonenumber", number.getText().toString());
            }
            params.addBodyParameter("orderperson", LoginUser.getInstance().getUser().getUserid() + "");
            params.addBodyParameter("orderamount", prize.getPrizeamount() + "");
            params.addBodyParameter("starttime", getTime());
            params.addBodyParameter("status", 1 + "");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    number.setText("");
                    AlertDialog alertDialog = new AlertDialog.Builder(PrizeActivity.this).setTitle(getString(R.string.alert))
                            .setMessage(getString(R.string.rechargeSuccess))
                            .setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    IntegralUtil.lessintegral(prize.getRequiredpoints(), LoginUser.getInstance().getUser().getUserid());
                                    Intent intent = new Intent();
                                    intent.setAction("action.refreshUserInfo");
                                    sendBroadcast(intent);
                                }
                            }).create(); // 创建对话框
                    alertDialog.show(); // 显示对话框
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

    }


    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0 ;
        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            vPager.setCurrentItem(index);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 获取当前时间的字符串表示形式
     * @return
     */
    private String getTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
        }
}
