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
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemMemoAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankNoteBean> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemMemoAdapter(List<BankNoteBean> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_memo, null);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankNoteBean cell = lists.get(position);
        if (cell != null) {
            holder.item_cont.setText(cell.getNoteContent());
            if(!StringUtil.isNullOrEmpty(cell.getDateLine())){
                holder.item_dateline.setText(DateUtil.getDate(cell.getDateLine(),"yyyy-MM-dd HH:mm"));
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView item_cont;
        TextView item_dateline;
    }
}
