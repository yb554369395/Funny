package com.yb.funny.activity;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yb.funny.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

//@ContentView(R.layout.activity_resource)
public class ResourceActivity extends AppCompatActivity {

    @ViewInject(R.id.text)
    TextView text;

//    @ViewInject(R.id.button)
//    Button button;
//
//    @ViewInject(R.id.button2)
//    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

//    @Event(R.id.button)
//    private void click(View view){
//        Intent intent = new Intent(ResourceActivity.this,LoginActivity.class);
//        startActivity(intent);
//    }
//
//    @Event(R.id.button2)
//    private void click2(View view){
//        Intent intent = new Intent(ResourceActivity.this,RegisterActivity.class);
//        startActivity(intent);
//    }


    private void load(View view){
        //将url直接添加到参数里面
        RequestParams params = new RequestParams("http://192.168.0.104:8080/funny/resource");
//        params.addParameter("resourcetext","哈哈哈哈哈哈哈哈哈");
//        params.addParameter("publishedtime",getTime() );
//        params.addParameter("publisher", 1);
//        params.addParameter("hashid", MD5.md5("1"+ getTime()));
//        params.addParameter("method","upload");
        params.addParameter("method","get");

       x.http().get(params, new Callback.CommonCallback<String>() {
           @Override
           public void onSuccess(String s) {
               text.setText(s);
           }

           @Override
           public void onError(Throwable throwable, boolean b) {
                Toast.makeText(x.app(),"load error",Toast.LENGTH_LONG).show();
           }

           @Override
           public void onCancelled(CancelledException e) {

           }

           @Override
           public void onFinished() {

           }
       });
    }


    public String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }


}
