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
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemDailyAdapter extends BaseAdapter {
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


    public ItemDailyAdapter(List<BankJobReport> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_daily, null);
            holder.item_nickname = (TextView) convertView.findViewById(R.id.item_nickname);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_brose = (TextView) convertView.findViewById(R.id.item_brose);
            holder.item_comment = (TextView) convertView.findViewById(R.id.item_comment);
            holder.item_fujian = (ImageView) convertView.findViewById(R.id.item_fujian);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankJobReport cell = lists.get(position);
        if (cell != null) {
            if(cell.getBankEmp() != null){
                holder.item_nickname.setText(cell.getBankEmp().getEmpName()==null?"":cell.getBankEmp().getEmpName());
            }
            if(!StringUtil.isNullOrEmpty(cell.getDateLine())){
                holder.item_dateline.setText(DateUtil.getDate(cell.getDateLine(), "MM-dd HH:mm"));
            }
            String titleStr = cell.getReportTitle()==null?"":cell.getReportTitle();
            if(titleStr.length() > 50){
                titleStr = titleStr.substring(0,49)+"...";
            }
            holder.item_cont.setText(titleStr);
            holder.item_brose.setText(String.valueOf(cell.getDoneCount()));
            holder.item_comment.setText(String.valueOf(cell.getCommentCount()));
            if(!StringUtil.isNullOrEmpty(cell.getReportFile())){
                holder.item_fujian.setVisibility(View.VISIBLE);
            }else {
                holder.item_fujian.setVisibility(View.GONE);
            }

            if (!StringUtil.isNullOrEmpty(BankAppApplication.fontSize)) {
                holder.item_dateline.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_nickname.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_cont.setTextSize(Float.valueOf(BankAppApplication.fontSize));
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView item_nickname;
        TextView item_cont;
        TextView item_dateline;
        TextView item_brose;
        TextView item_comment;
        ImageView item_fujian;//附件
    }
}
