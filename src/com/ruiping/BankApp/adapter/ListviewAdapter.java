package com.ruiping.BankApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ruiping.BankApp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/12.
 */
public class ListviewAdapter extends BaseAdapter {
    private ViewHolder holder;
    private Context context;
    private ArrayList<String> list;

    public ListviewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_set_font_size, null);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(list.get(position));
        return convertView;
    }
    class ViewHolder {
        TextView item_title;
    }

}
