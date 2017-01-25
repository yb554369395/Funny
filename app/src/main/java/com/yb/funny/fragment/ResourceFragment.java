package com.yb.funny.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.activity.CommentsActivity;
import com.yb.funny.adapter.ResourceListAdapter;
import com.yb.funny.entity.Resource;
import com.yb.funny.util.Constant;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.List;



/**
 * Created by 橘沐 on 2015/12/27.
 */
public class ResourceFragment extends Fragment {

    private int chooseitem;
    private int chooseitemid;
    private int count = 0;

    private View view;
    private ResourceListAdapter adapter;
    private ListView listView;
    private FloatingActionButton refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resource, container, false);
        listView = (ListView) view.findViewById(R.id.lv_resource);
        refresh  = (FloatingActionButton) view.findViewById(R.id.refresh);
        loadResource();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CommentsActivity.class);
                Bundle bundle = new Bundle();
                Resource resource = (Resource) adapter.getItem(position);
                chooseitem = position;
                chooseitemid = resource.getResourceid();
                bundle.putSerializable("resource",resource);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
                final TextView load = new TextView(getActivity());
                load.setText("正在加载。。。");
                load.setGravity(Gravity.CENTER);
                listView.addHeaderView(load);
                RequestParams params = new RequestParams(Constant.URI+"resource");
                params.setMultipart(true);
                params.addBodyParameter("method","get");
                params.addBodyParameter("type",1+"");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        load.setText("加载完成。");
                        List<Resource> addlist = json(s);
                        adapter.add(addlist);
                        adapter.notifyDataSetChanged();
                        listView.removeHeaderView(load);
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
        });

        return view;
    }


    @Override
    public void onResume() {
        count++;
        super.onResume();
        if (count>1){
            RequestParams params = new RequestParams(Constant.URI+"resource");
            params.setMultipart(true);
            params.addBodyParameter("method","get");
            params.addBodyParameter("type",2+"");
            params.addBodyParameter("resourceid",chooseitemid+"");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    List<Resource> list = json(s);
                    Resource resource = list.get(0);
                    adapter.changeData(chooseitem,resource);
                    adapter.notifyDataSetChanged();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void loadResource(){
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method","get");
        params.addBodyParameter("type",1+"");


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                List<Resource> list = json(s);
                adapter = new ResourceListAdapter(getActivity(),list);
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
