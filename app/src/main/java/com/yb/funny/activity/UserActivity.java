package com.yb.funny.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.adapter.ResourceListAdapter;
import com.yb.funny.entity.Resource;
import com.yb.funny.entity.User;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息界面
 * Created by Yangbin on 2017/1/21.
 */

@ContentView(R.layout.activity_user)
public class UserActivity extends AppCompatActivity {

    private ResourceListAdapter adapter;
//    @ViewInject(R.id.user_icon)
    private ImageView icon;
//    @ViewInject(R.id.user_name)
    private  TextView name;
//    @ViewInject(R.id.user_sex)
    private ImageView sex;
//    @ViewInject(R.id.user_integral)
    private  TextView integral;
//    @ViewInject(R.id.user_introduction)
    private  TextView introduction;
//    @ViewInject(R.id.user_title)
    private  TextView title;
    @ViewInject(R.id.user_resource)
    private  ListView lv_resource;
    @ViewInject(R.id.include)
    private  Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    private  TextView toolbar_title;
//    @ViewInject(R.id.user_scroll)
//    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText("");
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final User user = (User) getIntent().getSerializableExtra("user");



        View headView = getLayoutInflater().inflate(R.layout.user_headview,null);
        icon = (ImageView) headView.findViewById(R.id.user_icon);
        name = (TextView) headView.findViewById(R.id.user_name);
        sex = (ImageView) headView.findViewById(R.id.user_sex);
        integral = (TextView) headView.findViewById(R.id.user_integral);
        introduction = (TextView) headView.findViewById(R.id.user_introduction);
        title = (TextView) headView.findViewById(R.id.user_title);
        lv_resource.addHeaderView(headView,null,false);

//        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,null);
//        emptyView.setVisibility(View.GONE);
//        ((ViewGroup)lv_resource.getParent()).addView(emptyView);
//        lv_resource.setEmptyView(emptyView);

        initActivity(user);

        lv_resource.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserActivity.this, CommentsActivity.class);
                Bundle bundle = new Bundle();
                Resource resource = adapter.getItem(position - 1);
                bundle.putSerializable("resource", resource);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        lv_resource.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        if (absListView.getCount() < LoginUser.getInstance().getUser().getResourcecount()) {
                            int startid = adapter.getItem(adapter.getCount() - 1).getResourceid();
                            loadData(user.getUserid(), startid);
                        }else{
                            if(user.getResourcecount() > 0) {
                                Toast.makeText(UserActivity.this, getString(R.string.loadAll), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 初始化用户个人信息
     * @param user
     */
    private void initActivity(User user){
        BitmapUtil.loadIcon(user.getIcon(), icon);
        name.setText(user.getName());
        if (user.getSex() == 1){
            sex.setImageDrawable(getResources().getDrawable(R.drawable.signs_man));
        }else {
            sex.setImageDrawable(getResources().getDrawable(R.drawable.signs_woman));
        }
        integral.setText(getString(R.string.integral) + user.getIntegral());
        if (user.getIntroduction() != null){
            introduction.setText(user.getIntroduction());
        }else {
            introduction.setVisibility(View.GONE);
        }
        loadResource(user.getUserid());
    }


    /**
     * 分批加载数据
     * @param publisher
     * @param startid
     */
    private void loadData(final int publisher,int startid){
        final int[] count = new int[1];
        Handler handler = new Handler();
        final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.alert), getString(R.string.Loading));
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("type",3+"");
        params.addBodyParameter("publisher",publisher+"");
        params.addBodyParameter("startid",startid+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (LoginUser.getInstance().getUser().getResourcecount() > 0) {
                    List<Resource> list = JSONArray.parseArray(s, Resource.class);
                    if (list.size() > 0) {
                        adapter.addToFoot(list);
                        count[0] = list.size();
                    } else {
                        Toast.makeText(UserActivity.this, getString(R.string.noResource), Toast.LENGTH_LONG).show();
                    }
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                Toast toast = Toast.makeText(x.app(),
                        "加载" + count[0] + "项内容，加载成功！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 400);
                toast.show();
            }
        }, 2000);
    }

    private void loadResource(int publisher){
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("type", 3 + "");
        params.addBodyParameter("publisher", publisher + "");
        params.addBodyParameter("startid", 0 + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                title.setText(LoginUser.getInstance().getUser().getResourcecount() + "篇投稿");
                if (LoginUser.getInstance().getUser().getResourcecount()>0){
                    List<Resource> list = JSONArray.parseArray(s, Resource.class);
                    adapter = new ResourceListAdapter(UserActivity.this, list);
                    lv_resource.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv_resource);
                }else{
                    List<Resource> list = new ArrayList<>();
                    adapter = new ResourceListAdapter(UserActivity.this,list);
                    lv_resource.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv_resource);
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

    /**
     * 重新计算ListView的高度
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
