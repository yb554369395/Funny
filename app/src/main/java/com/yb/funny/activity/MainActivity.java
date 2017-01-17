package com.yb.funny.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.entity.User;
import com.yb.funny.fragment.ResourceFragment;
import com.yb.funny.fragment.PrizeFragment;
import com.yb.funny.fragment.FragmentParent3;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.SharedpreferencesUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tbHeadBar)
    Toolbar mTbHeadBar;


    /*侧滑菜单ListView放置菜单项*/
    @ViewInject(R.id.lvMenu)
    ListView mLvMenu;

    /*侧滑菜单用户信息布局*/
    @ViewInject(R.id.menu_login)
    Button login;


    @ViewInject(R.id.menu_icon)
    SimpleDraweeView icon;

    @ViewInject(R.id.menu_name)
    TextView name;

    @ViewInject(R.id.menu_integral)
    TextView integral;

    /* 侧滑菜单底部安按钮*/
    @ViewInject(R.id.logout)
    Button logout;

    @ViewInject(R.id.close)
    Button close;

    @ViewInject(R.id.drawerLayout)
    DrawerLayout mMyDrawable;

    ActionBarDrawerToggle mToggle;


    private List<Fragment> imageList = new ArrayList<Fragment>() {{
        add(new ResourceFragment());
        add(new PrizeFragment());
        add(new FragmentParent3());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

  /*初始化Toolbar与DrawableLayout*/
        initToolBarAndDrawableLayout();
        initUserInfo();

        mLvMenu.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.menu_array)));
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              addFragmentToStack(imageList.get(position));
                mMyDrawable.closeDrawers();/*收起抽屉*/

            }
        });

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
                initUserInfo();
            }
        }
    };

    @Event(R.id.menu_login)
    private void login(View view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Event(R.id.logout)
    private void logout(View view){
        SharedpreferencesUtil.removeSharedPreference(x.app(),Constant.USER_INFO);
        mMyDrawable.closeDrawers();/*收起抽屉*/
        Toast.makeText(x.app(), "注销成功", Toast.LENGTH_SHORT).show();
        initUserInfo();
    }

    @Event(R.id.close)
    private void close(View view){
        mMyDrawable.closeDrawers();
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提醒")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }

    private void initToolBarAndDrawableLayout() {
        setSupportActionBar(mTbHeadBar);
  /*以下俩方法设置返回键可用*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  /*设置标题文字不可显示*/
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToggle = new ActionBarDrawerToggle(this, mMyDrawable, mTbHeadBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(MainActivity.this, R.string.open, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(MainActivity.this, R.string.close, Toast.LENGTH_SHORT).show();
            }
        };



  /*mMyDrawable.setDrawerListener(mToggle);不推荐*/
        mMyDrawable.addDrawerListener(mToggle);
        mToggle.syncState();/*同步状态*/

        addFragmentToStack(imageList.get(0));

    }

    private void addFragmentToStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    public  void initUserInfo(){
        if (SharedpreferencesUtil.getSahrePreference(x.app(), Constant.USER_INFO).length() <= 2){
            login.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            integral.setVisibility(View.GONE);

        }else {
            login.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            integral.setVisibility(View.VISIBLE);

            //从Sharedpreferences中取出的json字符串最外层为中括号，应去掉，不然无法解析
            String userinfo = SharedpreferencesUtil.getSahrePreference(x.app(),Constant.USER_INFO);
            User user = JSON.parseObject(userinfo.substring(1,userinfo.length()-1), User.class);
            BitmapUtil.loadBitmap(user.getIcon(),icon);
            name.setText(user.getName());
            integral.setText("积分："+user.getIntegral());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}