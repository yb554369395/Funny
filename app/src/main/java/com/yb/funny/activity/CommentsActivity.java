package com.yb.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.adapter.CommentsListAdapter;
import com.yb.funny.adapter.ResourceListAdapter;
import com.yb.funny.entity.Comment;
import com.yb.funny.entity.Resource;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */


@ContentView(R.layout.activity_comments)
public class CommentsActivity extends AppCompatActivity{

    private CommentsListAdapter adapter;

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
        Resource resource= (Resource) intent.getSerializableExtra("resource");
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
                adapter = new CommentsListAdapter(getParent(),list);
                comments.setAdapter(adapter);
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
