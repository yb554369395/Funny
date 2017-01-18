package com.yb.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        Intent intent = this.getIntent();
        resource= (Resource) intent.getSerializableExtra("resource");
        initActivity(resource);
    }

    private void initActivity(Resource resource){
        BitmapUtil.loadBitmap(resource.getPublishericon(),icon);
        if ( resource.getResourcepic() == null){
            image.setVisibility(View.GONE);
        }else {
            BitmapUtil.loadBitmap(resource.getResourcepic(),image);
        }
        if (resource.getResourcetext() == null){
            text.setVisibility(View.GONE);
        }else{
            text.setText(resource.getResourcetext());
        }
        name.setText(resource.getPublishername());
        goodno.setText(resource.getPointpraiseno()+"");
        commentno.setText(resource.getCommentno()+"");

        //加载ListView空视图
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)comments.getParent()).addView(emptyView);
        comments.setEmptyView(emptyView);
        loadComments(resource.getResourceid());

    }

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

    @Event(R.id.comments_send)
    private void sendComments(View view){
        RequestParams params = new RequestParams(Constant.URI+"comment");
        params.setMultipart(true);
        params.addBodyParameter("method","publish");
        if (LoginUser.getInstance().getUser() == null){
            Toast.makeText(x.app(), "请先登录在进行评论！", Toast.LENGTH_LONG).show();
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
}
