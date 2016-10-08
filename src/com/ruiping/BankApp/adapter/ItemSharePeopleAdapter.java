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
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.entiy.BankTaskShareBean;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemSharePeopleAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankTaskShareBean> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemSharePeopleAdapter(List<BankTaskShareBean> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_task_share_person, null);
            holder.item_cover = (ImageView) convertView.findViewById(R.id.item_cover);
            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankTaskShareBean cell = lists.get(position);
        if (cell != null) {
            if(cell.getFlag() == 0){
                imageLoader.displayImage(InternetURL.INTERNAL+cell.getBankEmp().getEmpCover(), holder.item_cover, BankAppApplication.txOptions, animateFirstListener);
            }
            holder.item_name.setText(cell.getName());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView item_cover;
        TextView item_name;
    }
}
