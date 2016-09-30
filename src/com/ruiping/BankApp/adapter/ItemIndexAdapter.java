package com.ruiping.BankApp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.entiy.IndexObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemIndexAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<IndexObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemIndexAdapter(List<IndexObj> lists, Context mContect) {
        this.lists = lists;
        this.mContect = mContect;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        res = mContect.getResources();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_index_layout, null);
            holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IndexObj cell = lists.get(position);
        if (cell != null) {
            holder.item_number.setText(cell.getNumber());
            holder.item_title.setText(cell.getTitle());
            holder.item_number.setBackground(res.getDrawable(cell.getPic()));
            if(position == 0){
                holder.item_number.setTextColor(res.getColor(R.color.index_one));
            }
            if(position == 1){
                holder.item_number.setTextColor(res.getColor(R.color.index_two));
            }
            if(position == 2){
                holder.item_number.setTextColor(res.getColor(R.color.index_three));
            }
            if(position == 3){
                holder.item_number.setTextColor(res.getColor(R.color.index_four));
            }
            if(position == 4){
                holder.item_number.setTextColor(res.getColor(R.color.index_four));
            }
            if(position == 5){
                holder.item_number.setTextColor(res.getColor(R.color.index_five));
            }
            if(position == 6){
                holder.item_number.setTextColor(res.getColor(R.color.index_six));
            }
            if(position == 7){
                holder.item_number.setTextColor(res.getColor(R.color.index_seven));
            }


        }

        return convertView;
    }

    class ViewHolder {
        TextView item_number;
        TextView item_title;
    }
}
