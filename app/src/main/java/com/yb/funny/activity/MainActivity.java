package com.yb.funny.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.yb.funny.R;
import com.yb.funny.entity.User;
import com.yb.funny.fragment.ResourceFragment;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.SharedpreferencesUtil;
import com.yb.funny.util.TimeUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 主界面，显示资源列表
 *
 * Created by Yangbin on 2017/1/7.
 */

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private boolean isOpen = false;

    @ViewInject(R.id.tbHeadBar)
    private Toolbar mTbHeadBar;

    /*侧滑菜单ListView放置菜单项*/
    @ViewInject(R.id.lvMenu)
    private ListView mLvMenu;

    @ViewInject(R.id.menu_login)
    private Button login;

    @ViewInject(R.id.menu_icon)
    private ImageView icon;

    @ViewInject(R.id.menu_name)
    private TextView name;

    @ViewInject(R.id.menu_integral)
    private TextView integral;

    /* 侧滑菜单底部安按钮*/
    @ViewInject(R.id.logout)
    private  Button logout;

    @ViewInject(R.id.drawerLayout)
    private  DrawerLayout mMyDrawable;

    ActionBarDrawerToggle mToggle;


    private List<Activity> imageList = new ArrayList<Activity>() {{
        add(new UserActivity());
        add(new PrizeActivity());
    }};


    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

         /*初始化Toolbar与DrawableLayout*/
        initToolBarAndDrawableLayout();
        checkUser();
        initUserInfo();

        mLvMenu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.menu_array)));
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMyDrawable.closeDrawers();/*收起抽屉*/
                if (LoginUser.getInstance().getUser() != null) {
                    Intent intent = new Intent(MainActivity.this, imageList.get(position).getClass());
                    Bundle bundle = new Bundle();
                    User user = LoginUser.getInstance().getUser();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.alert))
                            .setMessage(getString(R.string.notLogin))
                            .setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }

                            }).setNegativeButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create(); // 创建对话框
                    alertDialog.show(); // 显示对话框
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshUserInfo");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            if (LoginUser.getInstance().getUser()!= null){
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
            }else {
                Toast.makeText(x.app(), getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isOpen) {
                mMyDrawable.closeDrawers();
            }else{
                exitBy2Click();      //调用双击退出函数
            }
        }
        return false;
    }


    /**
     * 广播接收器
     */
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

    /**
     * 跳转到登录界面
     * @param view
     */
    @Event(R.id.menu_login)
    private void login(View view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 注销功能
     * @param view
     */
    @Event(R.id.logout)
    private void logout(View view){
        SharedpreferencesUtil.removeSharedPreference(x.app(),Constant.USER_INFO);
        mMyDrawable.closeDrawers();/*收起抽屉*/
        Toast.makeText(x.app(), getString(R.string.logoutSuccess), Toast.LENGTH_SHORT).show();
        LoginUser.getInstance().setUser(null);
        initUserInfo();
    }

    /**
     * 跳转到用户个人信息界面
     * @param view
     */
//    @Event(value = R.id.user_info,type = RelativeLayout.OnClickListener.class)
//    private void setUserinfo(View view){
//        Intent intent = new Intent(MainActivity.this,UserActivity.class);
//        Bundle bundle = new Bundle();
//        User user = LoginUser.getInstance().getUser();
//        bundle.putSerializable("user", user);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }


    /**
     * 退出功能
     * @param view
     */
    @Event(R.id.close)
    private void close(View view){
        mMyDrawable.closeDrawers();
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.out))
                .setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        AppManager.getAppManager().AppExit(MainActivity.this);
                    }

                }).setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }


    /**
     * 初始化ToolBar
     */
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
                isOpen = true;
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isOpen = false;
            }
        };



  /*mMyDrawable.setDrawerListener(mToggle);不推荐*/
//        mMyDrawable.addDrawerListener(mToggle);
        mMyDrawable.setDrawerListener(mToggle);
        mToggle.syncState();/*同步状态*/

        addFragmentToStack(new ResourceFragment());

    }

    private void addFragmentToStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    /**
     * 初始化侧滑菜单的用户信息
     */
    public  void initUserInfo(){
        User user = LoginUser.getInstance().getUser();
        if (user == null){
            login.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            integral.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }else {
            login.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            integral.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);

            BitmapUtil.loadIcon(user.getIcon(), icon);
            name.setText(user.getName());
            integral.setText(getString(R.string.integral)+user.getIntegral());
        }
    }

    /**
     * 检查是否有用户信息保存在本地
     */
    public void checkUser(){
        //从Sharedpreferences中取出的json字符串最外层为中括号，应去掉，不然无法解析
        String userinfo = SharedpreferencesUtil.getSahrePreference(x.app(), Constant.USER_INFO);
        if (userinfo.length()>5) {
            User user = JSON.parseObject(userinfo, User.class);
            if (checkIsTimeOut(user.getLastlogin())){
                //将保存下来的已登陆的用户的信息保存到单例模型中
                LoginUser.getInstance().setUser(user);
            }else{
                Toast.makeText(x.app(), R.string.timeout, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
        AppManager.getAppManager().finishActivity(this);
    }



    /**
     * 检查用户上次登录时间是否超过48小时
     */
    private boolean checkIsTimeOut(Date lastlogin){
        Calendar last = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(TimeUtil.getDate(TimeUtil.getTimeMinute()));
        last.setTime(lastlogin);
        long diff = now.getTimeInMillis() - last.getTimeInMillis();
        return diff < 48 * 60 * 60 * 1000;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, getString(R.string.twoExit), Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
           AppManager.getAppManager().AppExit(this);
        }
    }


}