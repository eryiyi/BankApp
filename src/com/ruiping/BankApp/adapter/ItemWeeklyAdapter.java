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
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemWeeklyAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankJobReport> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemWeeklyAdapter(List<BankJobReport> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_weekly, null);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankJobReport cell = lists.get(position);
        if (cell != null) {
            if(cell.getBankEmp() != null){
                holder.item_title.setText(cell.getReportNumber() +"--" +cell.getBankEmp().getEmpName());
            }else {
                holder.item_title.setText(cell.getReportNumber());
            }
            String strContent = cell.getReportTitle();
            if(!StringUtil.isNullOrEmpty(strContent) && strContent.length() > 50){
                strContent = strContent.substring(0,49);
            }
            holder.item_cont.setText(strContent==null?"":strContent);
            holder.item_dateline.setText(cell.getDateStartEnd());
        }

        return convertView;
    }

    class ViewHolder {
        TextView item_title;
        TextView item_cont;
        TextView item_dateline;
    }
}
