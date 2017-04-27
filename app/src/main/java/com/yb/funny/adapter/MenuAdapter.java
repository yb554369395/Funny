package com.yb.funny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yb.funny.R;

/**
 * 菜单列表Adapter
 * Created by Yangbin on 17-4-13.
 */
public class MenuAdapter extends BaseAdapter{

    private Context context;
    private String[] text ;
    private int[] image ;


    public MenuAdapter(Context context,int[] image,  String[] text) {
        this.image = image;
        this.context = context;
        this.text = text;
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_list_item,parent,false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.menu_image);
            holder.textView = (TextView) convertView.findViewById(R.id.menu_text);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(image[position]);
        holder.textView.setText(text[position]);

        return convertView;
    }


    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
