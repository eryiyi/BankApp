package com.ruiping.BankApp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.entiy.AttachMentObj;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemAttachNoticeAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<AttachMentObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemAttachNoticeAdapter(List<AttachMentObj> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_attach_ment, null);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AttachMentObj cell = lists.get(position);
        if (cell != null) {
            holder.item_title.setText(cell.getTitle());
            holder.btn_delete.setVisibility(View.GONE);//隐藏

        }

        return convertView;
    }

    class ViewHolder {
        TextView item_title;
        ImageView btn_delete;
    }
}
