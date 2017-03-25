package com.yb.funny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yb.funny.R;
import com.yb.funny.entity.Comment;
import com.yb.funny.util.BitmapUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 显示评论的自定义ListView
 * Created by Yangbin on 2017/1/17
 */

public class CommentsListAdapter extends BaseAdapter{

    private List<Comment> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentsListAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    public void addAll(List<Comment> list){
        data.clear();
        data.addAll(list);
    }

    public void add(List<Comment> list){
        data.addAll(list);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Comment getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Comment comment = data.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.comments_list_item,
                    parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.comments_list_name);
            holder.time = (TextView) convertView.findViewById(R.id.comments_list_time);
            holder.comment = (TextView) convertView.findViewById(R.id.comments_list_comment);
            holder.icon = (ImageView) convertView.findViewById(R.id.comments_list_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BitmapUtil.loadIcon(comment.getCommentatoricon(), holder.icon);
        holder.name.setText(comment.getCommentatorname());
        holder.time.setText(DateToString(comment.getCommenttime()));
        holder.comment.setText(comment.getComments());
        return convertView;
    }

    private static class ViewHolder
    {
        ImageView icon;
        TextView name;
        TextView time;
        TextView comment;

    }

    private String DateToString(Date date){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(date);
//        calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
//        calendar.add(Calendar.HOUR_OF_DAY,1);
        return df.format(calendar.getTime());
    }
}
