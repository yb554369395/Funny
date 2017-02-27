package com.yb.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.adapter.ResourceListAdapter;
import com.yb.funny.entity.Resource;
import com.yb.funny.entity.User;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.AppManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 用户信息界面
 * Created by Administrator on 2017/1/21.
 */

@ContentView(R.layout.activity_user)
public class UserActivity extends AppCompatActivity{

    private ResourceListAdapter adapter;
    private int chooseitem;
    private int chooseitemid;
    @ViewInject(R.id.user_icon)
    SimpleDraweeView icon;
    @ViewInject(R.id.user_name)
    TextView name;
    @ViewInject(R.id.user_sex)
    ImageView sex;
    @ViewInject(R.id.user_integral)
    TextView integral;
    @ViewInject(R.id.user_introduction)
    TextView introduction;
    @ViewInject(R.id.user_title)
    TextView title;
    @ViewInject(R.id.user_resource)
    ListView lv_resource;
    @ViewInject(R.id.include)
    Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    TextView toolbar_title;

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

        User user = (User) getIntent().getSerializableExtra("user");
        initActivity(user);

        lv_resource.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserActivity.this, CommentsActivity.class);
                Bundle bundle = new Bundle();
                Resource resource = (Resource) adapter.getItem(position);
                chooseitem = position;
                chooseitemid = resource.getResourceid();
                bundle.putSerializable("resource",resource);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initActivity(User user){
        BitmapUtil.loadIcon(user.getIcon(),icon);
        name.setText(user.getName());
        if (user.getSex() == 1){
            sex.setImageDrawable(getResources().getDrawable(R.drawable.signs_man));
        }else {
            sex.setImageDrawable(getResources().getDrawable(R.drawable.signs_woman));
        }
        integral.setText("积分："+user.getIntegral());
        if (user.getIntroduction() != null){
            introduction.setText(user.getIntroduction());
        }else {
            introduction.setVisibility(View.GONE);
        }
        loadResource(user.getUserid());
    }


    private void loadResource(int publisher){
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method","get");
        params.addBodyParameter("type",3+"");
        params.addBodyParameter("publisher",publisher+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                JSONArray array = JSON.parseArray(s);
                List<Resource>list =  JSONArray.parseArray(s,Resource.class);
                title.setText(list.size()+"篇投稿");
                adapter = new ResourceListAdapter(UserActivity.this,list);
                lv_resource.setAdapter(adapter);
                setListViewHeightBasedOnChildren(lv_resource);
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
