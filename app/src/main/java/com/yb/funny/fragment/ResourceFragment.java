package com.yb.funny.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.activity.CommentsActivity;
import com.yb.funny.adapter.ResourceListAdapter;
import com.yb.funny.entity.Resource;
import com.yb.funny.util.Constant;
import com.yb.funny.util.SharedpreferencesUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Collections;
import java.util.List;


/**
 * 资源信息显示Fragment
 *
 * Created by Yangbin on 2017/1/10.
 */
public class ResourceFragment extends Fragment {

    private int chooseitem;
    private int chooseitemid;
    private int count = 0;
    private int lastitemid;

    private Resource lastVisibleResource;

    private View view;
    private ResourceListAdapter adapter;
    private ListView listView;
    private FloatingActionButton refresh;


    public ResourceFragment(Resource lastVisibleResource) {
        this.lastVisibleResource = lastVisibleResource;
    }

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
                Resource resource = adapter.getItem(position);
                chooseitem = position;
                chooseitemid = resource.getResourceid();
                bundle.putSerializable("resource", resource);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(adapter != null) {
                    lastVisibleResource = adapter.getItem(i);
                    SharedpreferencesUtil.setSharedPreference(x.app(), Constant.RESOURCE_INFO, JSON.toJSONString(lastVisibleResource));
                }
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.alert), getString(R.string.Loading));
                RequestParams params = new RequestParams(Constant.URI + "resource");
                params.setMultipart(true);
                params.addBodyParameter("method", "get");
                params.addBodyParameter("type", 1 + "");
                params.addBodyParameter("resourceid",lastitemid+"");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        Toast toast = Toast.makeText(x.app(),
                                getString(R.string.loadSuccess), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 400);
                        toast.show();
                        List<Resource> addlist = json(s);
                        adapter.addToHead(addlist);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(5);
                        lastitemid = addlist.get(0).getResourceid();
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
        params.addBodyParameter("method", "get");
        params.addBodyParameter("type",1+"");
        params.addBodyParameter("resourceid",lastVisibleResource.getResourceid()-1+"");


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                List<Resource> list = json(s);
                lastitemid = list.get(0).getResourceid();
                adapter = new ResourceListAdapter(getActivity(), list);
                listView.setAdapter(adapter);
                listView.setSelection(4);
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
        List<Resource> list =  JSONArray.parseArray(s, Resource.class);
        Collections.reverse(list);
        return list;
    }



}
