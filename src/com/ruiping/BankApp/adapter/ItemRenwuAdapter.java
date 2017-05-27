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
            holder.item_nickname1 = (TextView) convertView.findViewById(R.id.item_nickname1);
            holder.item_nickname2 = (TextView) convertView.findViewById(R.id.item_nickname2);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_dateline1 = (TextView) convertView.findViewById(R.id.item_dateline1);
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
                holder.item_nickname.setText("负责人："+cell.getBankEmpf().getEmpName());
                if(BankAppApplication.EMPID.equals(cell.getBankEmpf().getEmpId())){
                    //说明是当前登陆者自己负责这个项目
                    holder.item_nickname.setTextColor(res.getColor(R.color.btn_blue_normal));
                }else {
                    //说明不是当前人负责的项目
                    holder.item_nickname.setTextColor(res.getColor(R.color.text_color_two));
                }
            }
            if("1".equals(cell.getIsType())){
                //任务已完成
                convertView.setBackgroundColor(res.getColor(R.color.main_bg));
            }else {
                //未完成
                convertView.setBackground(res.getDrawable(R.drawable.btn_white_shape));
            }

            if(cell.getBankEmp() != null){
               holder.item_nickname1.setText("创建人："+cell.getBankEmp().getEmpName());
            } else{
                holder.item_nickname1.setText("");
            }
            if(cell.getBankEmpZf() != null){
               holder.item_nickname2.setText("主负责人："+cell.getBankEmpZf().getEmpName());
            }else{
                holder.item_nickname2.setText("");
            }

            holder.item_title.setText(cell.getTaskTitle());
            holder.item_jindu.setText("进度："+cell.getTaskProgress());
            if(!StringUtil.isNullOrEmpty(cell.getDateLineEnd())){
                holder.item_dateline.setText("到期日："+DateUtil.getDate(cell.getDateLineEnd(), "yyyy-MM-dd"));
            }
            if(!StringUtil.isNullOrEmpty(cell.getDateLineEnd())){
                holder.item_dateline1.setText("开始日："+DateUtil.getDate(cell.getDateLine(), "yyyy-MM-dd"));
            }

            if (!StringUtil.isNullOrEmpty(BankAppApplication.fontSize)) {
                holder.item_title.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_nickname.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_nickname1.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_nickname2.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_jindu.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_dateline.setTextSize(Float.valueOf(BankAppApplication.fontSize));
                holder.item_dateline1.setTextSize(Float.valueOf(BankAppApplication.fontSize));
            }
            if("0".equals(cell.getIsExceed())){
                //未超时
//                holder.item_title.setTextColor(res.getColor(R.color.text_color));
            }else if("1".equals(cell.getIsExceed())){
                //超时
//                holder.item_title.setTextColor(res.getColor(R.color.red_p));
            }

        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_head;
        TextView item_title;
        TextView item_jindu;
        TextView item_nickname;
        TextView item_nickname1;
        TextView item_nickname2;
        TextView item_dateline;
        TextView item_dateline1;
    }
}
