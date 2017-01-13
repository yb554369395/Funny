package com.yb.funny.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.adapter.ListAdapter;
import com.yb.funny.entity.Resource;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.List;



/**
 * Created by 橘沐 on 2015/12/27.
 */
public class ResourceFragment extends Fragment {
    private static final String RESOURCE = "resource";

    private View view;
    private ListAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resource, container, false);
        listView = (ListView) view.findViewById(R.id.lv_resource);
        loadResource();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(RESOURCE, Context.MODE_PRIVATE);
//        String json = sharedPreferences.getString(RESOURCE, "");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void loadResource(){
        RequestParams params = new RequestParams("http://192.168.0.104:8080/funny/resource");
        params.setMultipart(true);
        params.addBodyParameter("method","get");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(RESOURCE, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                editor.putString(RESOURCE, s);
//                editor.commit();//提交修改
                List<Resource> list = json(s);
                adapter = new ListAdapter(getActivity(),list);
                listView.setAdapter(adapter);
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


    public List<Resource> json(String s){
        JSONArray array = JSON.parseArray(s);
        List<Resource>list =  JSONArray.parseArray(s,Resource.class);
        return  list;
    }



}
