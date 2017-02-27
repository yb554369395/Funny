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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.adapter.CommentsListAdapter;
import com.yb.funny.entity.Comment;
import com.yb.funny.entity.Resource;
import com.yb.funny.entity.User;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.AppManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/17.
 */


@ContentView(R.layout.activity_comments)
public class CommentsActivity extends AppCompatActivity{

    private CommentsListAdapter adapter;
    private Resource resource;

    @ViewInject(R.id.comments_iv_icon)
    SimpleDraweeView icon;
    @ViewInject(R.id.comments_iv_image)
    SimpleDraweeView image;
    @ViewInject(R.id.comments_iv_share)
    ImageView share;
    @ViewInject(R.id.comments_tv_name)
    TextView name;
    @ViewInject(R.id.comments_tv_text)
    TextView text;
    @ViewInject(R.id.comments_tv_good)
    TextView goodno;
    @ViewInject(R.id.comments_tv_comments)
    TextView commentno;
    @ViewInject(R.id.comments_et_comment)
    EditText et_comment;
    @ViewInject(R.id.comments_send)
    Button send;
    @ViewInject(R.id.lv_comments)
    ListView comments;
    @ViewInject(R.id.tbHeadBar)
    Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    TextView toolbar_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText("详情");
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = this.getIntent();
        resource= (Resource) intent.getSerializableExtra("resource");
        initActivity(resource);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initActivity(Resource resource){
        BitmapUtil.loadIcon(resource.getPublishericon(),icon);
        if ( resource.getResourcepic() == null){
            image.setVisibility(View.GONE);
        }else {
//            BitmapUtil.loadBitmap(resource.getResourcepic(),image);
            BitmapUtil.loadPicture(image,resource.getResourcepic(),BitmapUtil.getScreenWidth(CommentsActivity.this));
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
        goodno.setText(resource.getPointpraiseno()+"");
        commentno.setText(resource.getCommentno()+"");

        //加载ListView空视图
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)comments.getParent()).addView(emptyView);
        comments.setEmptyView(emptyView);
        loadComments(resource.getResourceid());

    }

    @Event(value = R.id.comments_iv_icon,type = SimpleDraweeView.OnClickListener.class)
    private void loadUserInfo(View view){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("publisher", resource.getPublisher() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                JSONArray array = JSON.parseArray(s);
                List<User>list =  JSONArray.parseArray(s,User.class);
                User user = list.get(0);
                Intent intent = new Intent(CommentsActivity.this,UserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
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

    @Event(value = R.id.comments_tv_good,type = TextView.OnClickListener.class)
    private void good(View view){
        if (LoginUser.getInstance().getUser() != null) {
            checkgood(resource.getResourceid(), clickhandler);
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
        }else{
            Toast.makeText(x.app(), "请先登录！", Toast.LENGTH_LONG).show();
        }

    }

    /*当点赞或者评论过后，更新点赞数和评论数组件显示得最新数据*/
    private void refreshno(){
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method","get");
        params.addBodyParameter("type",2+"");
        params.addBodyParameter("resourceid",resource.getResourceid()+"");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                JSONArray array = JSON.parseArray(s);
                List<Resource>list =  JSONArray.parseArray(s,Resource.class);
               Resource resource2 =  list.get(0);
                goodno.setText(resource2.getPointpraiseno()+"");
                commentno.setText(resource2.getCommentno()+"");
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

    /*加载评论数据*/
    private void loadComments(int belongid){
        RequestParams params = new RequestParams(Constant.URI+"comment");
        params.setMultipart(true);
        params.addBodyParameter("method","receive");
        params.addBodyParameter("belongid",belongid+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                JSONArray array = JSON.parseArray(s);
                List<Comment> list =  JSONArray.parseArray(s,Comment.class);
                if (adapter == null){
                    if (list.size()>0) {
                        adapter = new CommentsListAdapter(CommentsActivity.this, list);
                        comments.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(comments);
                    }
                }else {
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

    /*发表评论*/
    @Event(R.id.comments_send)
    private void sendComments(View view){
        RequestParams params = new RequestParams(Constant.URI+"comment");
        params.setMultipart(true);
        params.addBodyParameter("method","publish");
        if (LoginUser.getInstance().getUser() == null){
            Toast.makeText(x.app(), "请先登录！", Toast.LENGTH_LONG).show();
        }else {
            params.addBodyParameter("commentatorid", LoginUser.getInstance().getUser().getUserid() + "");
            params.addBodyParameter("belongid", resource.getResourceid() + "");
            if (et_comment.getText().toString().equals("")){
                Toast.makeText(x.app(), "评论必须有内容才可以发表哦！", Toast.LENGTH_SHORT).show();
            }else {
                params.addBodyParameter("comments", et_comment.getText().toString());
            }
            params.addBodyParameter("commenttime", getTime());
            params.addBodyParameter("commentatorname", LoginUser.getInstance().getUser().getName());
            params.addBodyParameter("commentatoricon", LoginUser.getInstance().getUser().getIcon());
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    et_comment.setText("");
                    Toast.makeText(x.app(), "发表评论成功！", Toast.LENGTH_SHORT).show();
                    loadComments(resource.getResourceid());
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
    }

    @Event(value = R.id.comments_iv_share,type = ImageView.OnClickListener.class)
    private void share(View view){
        if (resource.getResourcepic()== null){
            shareText();
        }else {
            shareTextAndPic();
        }

    }

    private void shareText(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, resource.getResourcetext()+"-----内容来自Funny,笑口常开！");
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(intent, "分享到"));
    }
    private void shareTextAndPic(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, resource.getResourcepic());
        shareIntent.putExtra(Intent.EXTRA_TEXT, resource.getResourcetext()+"-----内容来自Funny,笑口常开！");
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }


    private String getTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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


    /*异步操作，初始化的时候对是否已经点赞得结果进行处理*/
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

    /*异步操作，点击点赞按钮的时候对是否已经点赞得结果进行处理*/
    Handler clickhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what>0){
                Toast toast = Toast.makeText(x.app(), "您已经赞过了", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }else {
                Drawable drawable = getResources().getDrawable(R.drawable.good);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                goodno.setCompoundDrawables(drawable, null, null, null);
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
