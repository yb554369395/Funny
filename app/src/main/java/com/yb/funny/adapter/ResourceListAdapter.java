package com.yb.funny.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.entity.Resource;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 自定义ListView
 * Created by Marven on 2017/1/11.
 */

public class ResourceListAdapter extends BaseAdapter{

    private List<Resource> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public ResourceListAdapter(Context context, List<Resource> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    public void add(List<Resource> list){
        data.addAll(0,list);
    }


    public void changeData(int position,Resource resource){
        data.remove(position);
        data.add(position,resource);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Resource resource = data.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.resource_list_item,
                    parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.tv_text);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.good = (TextView) convertView.findViewById(R.id.tv_good);
            holder.comments = (TextView) convertView.findViewById(R.id.tv_comments);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.iv_icon);
            holder.image = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
            holder.share = (ImageView) convertView.findViewById(R.id.iv_share);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BitmapUtil.loadBitmap(resource.getPublishericon(),holder.icon);
        if ( resource.getResourcepic() == null){
            holder.image.setVisibility(View.GONE);
        }else {
            BitmapUtil.loadBitmap(resource.getResourcepic(),holder.image);
        }

        holder.name.setText(resource.getPublishername());
        if (resource.getResourcetext() == null){
            holder.text.setVisibility(View.GONE);
        }else{
            holder.text.setText(resource.getResourcetext());
        }

        checkgood(resource.getResourceid(),holder.good);
        holder.good.setText(resource.getPointpraiseno()+"");
        holder.comments.setText(resource.getCommentno()+"");
        return convertView;
    }

    private static class ViewHolder
    {
        SimpleDraweeView icon;
        SimpleDraweeView image;
        ImageView share;
        TextView name;
        TextView text;
        TextView good;
        TextView comments;

    }

    /**
     * 检查当前登录用户是否对当前资源已经点赞
     */
    private void checkgood(int resourceid, final TextView textView){
        if (LoginUser.getInstance().getUser() != null){
            RequestParams params = new RequestParams(Constant.URI + "like");
            params.setMultipart(true);
            params.addBodyParameter("method", "check");
            params.addBodyParameter("likeofresource", resourceid + "");
            params.addBodyParameter("likeofpeople", LoginUser.getInstance().getUser().getUserid() + "");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    if (Integer.parseInt(s) > 0) {
                        Drawable drawable = x.app().getResources().getDrawable(R.drawable.good);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(drawable, null, null, null);
                    }else {
                        Drawable drawable = x.app().getResources().getDrawable(R.drawable.ungood);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(drawable, null, null, null);
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
    }
}
