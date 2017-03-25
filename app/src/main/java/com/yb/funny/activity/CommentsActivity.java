package com.yb.funny.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.adapter.CommentsListAdapter;
import com.yb.funny.entity.Comment;
import com.yb.funny.entity.Resource;
import com.yb.funny.entity.User;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.IntegralUtil;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 资源详情界面
 *
 * Created by Yangbin on 2017/1/17.
 */


@ContentView(R.layout.activity_comments)
public class CommentsActivity extends AppCompatActivity {

    private static final int RESULT_SHARE = 1;
    private CommentsListAdapter adapter;
    private Resource resource;

    @ViewInject(R.id.comments_iv_icon)
    private  ImageView icon;
    @ViewInject(R.id.comments_iv_image)
    private SimpleDraweeView image;
    @ViewInject(R.id.comments_tv_name)
    private TextView name;
    @ViewInject(R.id.comments_tv_text)
    private  TextView text;
    @ViewInject(R.id.comments_tv_good)
    private  TextView goodno;
    @ViewInject(R.id.comments_tv_comments)
    private   TextView commentno;
    @ViewInject(R.id.comments_et_comment)
    private   EditText et_comment;
    @ViewInject(R.id.comments_send)
    private  Button send;
    @ViewInject(R.id.lv_comments)
    private   ListView comments;
    @ViewInject(R.id.tbHeadBar)
    private  Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    private  TextView toolbar_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText(getString(R.string.detail));
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* 获取从MainActivity中传过来的resource */
        Intent intent = this.getIntent();
        resource= (Resource) intent.getSerializableExtra("resource");
        initActivity(resource);

        comments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        if ((absListView.getCount() < resource.getCommentno())&&resource.getCommentno()!=0) {
                            int startid = adapter.getItem(adapter.getCount() - 1).getCommentid();
                            loadComments(resource.getResourceid(),startid);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 初始化上半部分的界面内容
     * @param resource
     */
    private void initActivity(Resource resource){
        BitmapUtil.loadIcon(resource.getPublishericon(), icon);
        if ( resource.getResourcepic() == null){
            image.setVisibility(View.GONE);
        }else {
            BitmapUtil.loadPicture(image, resource.getResourcepic(),BitmapUtil.getScreenWidth(this));
        }
        if (resource.getResourcetext() == null){
            text.setVisibility(View.GONE);
        }else{
            text.setText(resource.getResourcetext());
        }
        name.setText(resource.getPublishername());
        if (LoginUser.getInstance().getUser() != null) {
            checkgood(resource.getResourceid(), inithandler);
        }
        goodno.setText(resource.getPointpraiseno() + "");
        commentno.setText(resource.getCommentno() + "");

        //加载ListView空视图
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)comments.getParent()).addView(emptyView);
        comments.setEmptyView(emptyView);
        if (resource.getCommentno()>0) {
            loadComments(resource.getResourceid(), 0);
        }
    }

    /**
     * 通过点击头像，可以跳转到任意用户的个人信息界面
     * @param view
     */
    @Event(value = R.id.comments_iv_icon,type = ImageView.OnClickListener.class)
    private void loadUserInfo(View view){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("publisher", resource.getPublisher() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                List<User> list = JSONArray.parseArray(s, User.class);
                User user = list.get(0);
                Intent intent = new Intent(CommentsActivity.this, UserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
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
     * 点赞功能
     * @param view
     */
    @Event(value = R.id.comments_tv_good,type = TextView.OnClickListener.class)
    private void good(View view){
        if (LoginUser.getInstance().getUser() != null) {
            checkgood(resource.getResourceid(), clickhandler);
        }else{
            Toast.makeText(x.app(), getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 通过访问后台接口，传递参数，添加点赞数据
     */
    private void addGood(){
        RequestParams params = new RequestParams(Constant.URI + "like");
        params.setMultipart(true);
        params.addBodyParameter("method", "add");
        params.addBodyParameter("likeofresource", resource.getResourceid() + "");
        params.addBodyParameter("likeofpeople", LoginUser.getInstance().getUser().getUserid() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                refreshno();
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
     * 当点赞或者评论过后，更新点赞数和评论数组件显示得最新数据
     */
    private void refreshno(){
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("type", 2 + "");
        params.addBodyParameter("resourceid", resource.getResourceid() + "");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                List<Resource> list = JSONArray.parseArray(s, Resource.class);
                Resource resource2 = list.get(0);
                goodno.setText(resource2.getPointpraiseno() + "");
                commentno.setText(resource2.getCommentno() + "");
                Intent intent = new Intent();
                intent.setAction("action.refreshUserInfo");
                sendBroadcast(intent);
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
     * 加载评论数据
     */
//    private void loadComments(int belongid){
//        RequestParams params = new RequestParams(Constant.URI+"comment");
//        params.setMultipart(true);
//        params.addBodyParameter("method","receive");
//        params.addBodyParameter("belongid", belongid + "");
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String s) {
//                List<Comment> list = JSONArray.parseArray(s, Comment.class);
//                if (adapter == null) {
//                    if (list.size() > 0) {
//                        adapter = new CommentsListAdapter(CommentsActivity.this, list);
//                        comments.setAdapter(adapter);
//                        setListViewHeightBasedOnChildren(comments);
//                    }
//                } else {
//                    adapter.addAll(list);
//                    setListViewHeightBasedOnChildren(comments);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable, boolean b) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException e) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }

    private void loadComments(int belongid,int commentsid){
        RequestParams params = new RequestParams(Constant.URI+"comment");
        params.setMultipart(true);
        params.addBodyParameter("method","receive");
        params.addBodyParameter("belongid", belongid + "");
        params.addBodyParameter("startid", commentsid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                List<Comment> list = JSONArray.parseArray(s, Comment.class);
                if (adapter == null) {
                    if (list.size() > 0) {
                        adapter = new CommentsListAdapter(CommentsActivity.this, list);
                        comments.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(comments);
                    }
                } else {
                    adapter.add(list);
                    setListViewHeightBasedOnChildren(comments);
                    adapter.notifyDataSetChanged();
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
     * 发表评论
     */
    @Event(R.id.comments_send)
    private void sendComments(View view){
        RequestParams params = new RequestParams(Constant.URI+"comment");
        params.setMultipart(true);
        params.addBodyParameter("method", "publish");
        if (LoginUser.getInstance().getUser() == null){
            Toast.makeText(x.app(), getString(R.string.pleaseLogin), Toast.LENGTH_LONG).show();
        }else {
            params.addBodyParameter("commentatorid", LoginUser.getInstance().getUser().getUserid() + "");
            params.addBodyParameter("belongid", resource.getResourceid() + "");
            if (et_comment.getText().toString().equals("")){
                Toast.makeText(x.app(), getString(R.string.havaContent), Toast.LENGTH_SHORT).show();
                return;
            }else {
                params.addBodyParameter("comments", et_comment.getText().toString());
            }
            params.addBodyParameter("commenttime", TimeUtil.getTimeSeconds());
            params.addBodyParameter("commentatorname", LoginUser.getInstance().getUser().getName());
            params.addBodyParameter("commentatoricon", LoginUser.getInstance().getUser().getIcon().substring(18+Constant.IP.length()));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(send.getApplicationWindowToken(), 0);

                    et_comment.setText("");
                    Toast.makeText(x.app(), getString(R.string.pubSuccess), Toast.LENGTH_SHORT).show();
                    IntegralUtil.addintegral(5, LoginUser.getInstance().getUser().getUserid());
                    if (adapter == null){
                        loadComments(resource.getResourceid(),0);
                    }else{
                        loadComments(resource.getResourceid(),adapter.getItem(adapter.getCount()-1).getCommentid()+1);
                    }
                    refreshno();
                    Intent intent = new Intent();
                    intent.setAction("action.refreshUserInfo");
                    sendBroadcast(intent);
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
     * 分享功能
     * @param view
     */
    @Event(value = R.id.comments_iv_share,type = ImageView.OnClickListener.class)
    private void share(View view){
        if (resource.getResourcepic()== null){
            shareText();
        }else {
            shareTextAndPic();
        }

    }

    /**
     * 分享功能
     * 只有文字的投稿的分享
     */
    private void shareText(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, resource.getResourcetext() + getString(R.string.fromFunny));
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //设置分享列表的标题，并且每次都显示分享列表
        startActivityForResult(Intent.createChooser(intent, getString(R.string.shareTo)), RESULT_SHARE);
    }

    /**
     * 包含图片的投稿的分享
     */
    private void shareTextAndPic(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, resource.getResourcepic());
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(Intent.createChooser(shareIntent, getString(R.string.shareTo)), RESULT_SHARE);
    }

    /**
     * 当分享成功后返回会调用该方法，提示用户分享成功，并添加相应的积分，刷新数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SHARE && resultCode == RESULT_OK ) {
            Toast.makeText(CommentsActivity.this, getString(R.string.shareSuccess), Toast.LENGTH_SHORT).show();
            IntegralUtil.addintegral(2,LoginUser.getInstance().getUser().getUserid());
            Intent intent = new Intent();
            intent.setAction("action.refreshUserInfo");
            sendBroadcast(intent);
        }
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


    /**
     * 异步操作，初始化的时候对是否已经点赞得结果进行处理
     */
    Handler inithandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what>0){
                Drawable drawable = getResources().getDrawable(R.drawable.good);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                goodno.setCompoundDrawables(drawable, null, null, null);
            }
            return true;
        }
    });

    /**
     * 异步操作，点击点赞按钮的时候对是否已经点赞得结果进行处理
     */
    Handler clickhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what>0){
                Toast toast = Toast.makeText(x.app(), getString(R.string.alreadyGood), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }else {
                Drawable drawable = getResources().getDrawable(R.drawable.good);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                goodno.setCompoundDrawables(drawable, null, null, null);
                IntegralUtil.addintegral(1, LoginUser.getInstance().getUser().getUserid());
                addGood();
            }
            return true;
        }
    });




    /**
     * 检查当前登录用户是否对当前资源已经点赞
     * 并根据情况进行操作
     */
    private void checkgood(int resourceid, final Handler handler){
            RequestParams params = new RequestParams(Constant.URI + "like");
            params.setMultipart(true);
            params.addBodyParameter("method", "check");
            params.addBodyParameter("likeofresource", resourceid + "");
            params.addBodyParameter("likeofpeople", LoginUser.getInstance().getUser().getUserid() + "");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    int row = Integer.parseInt(s);
                    Message msg = new Message();
                    msg.what = row;
                    handler.sendMessage(msg);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
