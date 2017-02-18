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
import com.ruiping.BankApp.entiy.RelateObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemRelateAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<RelateObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemRelateAdapter(List<RelateObj> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_relate, null);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RelateObj cell = lists.get(position);
        if (cell != null) {
            holder.item_title.setText((cell.getTitle()==null?"":cell.getTitle()));
            if(cell.getBankemp() != null){
                holder.item_dateline.setText((cell.getBankemp().getEmpName())+"--"+cell.getRecordTime());
            }else {
                holder.item_dateline.setText(cell.getRecordTime());
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView item_title;
        TextView item_dateline;
    }
}
