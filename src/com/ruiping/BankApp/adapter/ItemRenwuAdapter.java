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
import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemRenwuAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankJobTask> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemRenwuAdapter(List<BankJobTask> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_renwu, null);
            holder.item_head = (ImageView) convertView.findViewById(R.id.item_head);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_jindu = (TextView) convertView.findViewById(R.id.item_jindu);
            holder.item_nickname = (TextView) convertView.findViewById(R.id.item_nickname);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankJobTask cell = lists.get(position);
        if (cell != null) {
            //判断主次
            if(!StringUtil.isNullOrEmpty(cell.getPid()) && !"0".equals(cell.getPid())){
                //说明有pid  是子任务
                holder.item_head.setImageDrawable(res.getDrawable(R.drawable.subtask));
            }else{
                //说明是主任务
                holder.item_head.setImageDrawable(res.getDrawable(R.drawable.maintask));
            }
            if(cell.getBankEmpf() != null){
//                imageLoader.displayImage(InternetURL.INTERNAL+cell.getBankEmpf().getEmpCover(), holder.item_head, BankAppApplication.txOptions, animateFirstListener);
                holder.item_nickname.setText(cell.getBankEmpf().getEmpName());
            }

            holder.item_title.setText(cell.getTaskTitle());
            holder.item_jindu.setText(cell.getTaskProgress());
            if(!StringUtil.isNullOrEmpty(cell.getDateLine())){
                holder.item_dateline.setText(DateUtil.getDate(cell.getDateLine(), "yy-MM-dd HH:mm"));
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_head;
        TextView item_title;
        TextView item_jindu;
        TextView item_nickname;
        TextView item_dateline;
    }
}
