package com.yb.funny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yb.funny.R;
import com.yb.funny.entity.Resource;
import org.xutils.x;

import java.util.List;

/**
 * 自定义ListView
 * Created by Marven on 2017/1/11.
 */

public class ListAdapter extends BaseAdapter{

    private List<Resource> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListAdapter(Context context,List<Resource> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(this.context);
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
            convertView = layoutInflater.inflate(R.layout.list_item,
                    parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.tv_text);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.good = (TextView) convertView.findViewById(R.id.tv_good);
            holder.comments = (TextView) convertView.findViewById(R.id.tv_comments);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.share = (ImageView) convertView.findViewById(R.id.iv_share);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        x.image().bind(holder.icon,resource.getPublishericon());
        if ( resource.getResourcepic() == null){
            holder.image.setVisibility(View.GONE);
        }else {
            x.image().bind(holder.image,resource.getResourcepic());
        }

        holder.name.setText(resource.getPublishername());
        if (resource.getResourcetext() == null){
            holder.text.setVisibility(View.GONE);
        }else{
            holder.text.setText(resource.getResourcetext());
        }
        holder.good.setText(resource.getPointpraiseno()+"");
        holder.comments.setText(resource.getCommentno()+"");
        return convertView;
    }

    private static class ViewHolder
    {
        ImageView icon;
        ImageView image;
        ImageView share;
        TextView name;
        TextView text;
        TextView good;
        TextView comments;

    }
}
