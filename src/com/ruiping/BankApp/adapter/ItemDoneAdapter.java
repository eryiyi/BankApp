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
import com.ruiping.BankApp.entiy.BankJobReportDoneBean;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemDoneAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankJobReportDoneBean> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemDoneAdapter(List<BankJobReportDoneBean> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_done, null);
            holder.item_head = (ImageView) convertView.findViewById(R.id.item_head);
            holder.item_nickname = (TextView) convertView.findViewById(R.id.item_nickname);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankJobReportDoneBean cell = lists.get(position);
        if (cell != null) {
            if(cell.getBankEmp() != null){
                holder.item_nickname.setText(cell.getBankEmp().getEmpName());
                imageLoader.displayImage(InternetURL.INTERNAL + cell.getBankEmp().getEmpCover(), holder.item_head, animateFirstListener);
            }
            if(!StringUtil.isNullOrEmpty(cell.getDateLine())){
                holder.item_dateline.setText(DateUtil.getDate(cell.getDateLine(),"MM-dd HH:mm"));
            }
            holder.item_cont.setText("已查阅");

            if (!StringUtil.isNullOrEmpty(BankAppApplication.fontSize)) {
                holder.item_dateline.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_nickname.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_cont.setTextSize(Float.valueOf(BankAppApplication.fontSize));
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView item_head;
        TextView item_nickname;
        TextView item_dateline;
        TextView item_cont;
    }
}
