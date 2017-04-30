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
import com.ruiping.BankApp.entiy.BankJobTaskEmp;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemTaskPersonAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankJobTaskEmp> lists;
    private Context mContect;
    Resources res;
    private boolean flag;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemTaskPersonAdapter(List<BankJobTaskEmp> lists, Context mContect, boolean flag) {
        this.lists = lists;
        this.mContect = mContect;
        this.flag = flag;
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_task_person, null);
            holder.item_head = (ImageView) convertView.findViewById(R.id.item_head);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.btn_delete = (TextView) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankJobTaskEmp cell = lists.get(position);
        if (cell != null) {
            if( cell.getBankEmp() != null ){
                imageLoader.displayImage(InternetURL.INTERNAL + cell.getBankEmp().getEmpCover(), holder.item_head, BankAppApplication.txOptions, animateFirstListener);
                holder.item_title.setText(cell.getBankEmp().getEmpName());
            }
            if(flag){
                holder.btn_delete.setVisibility(View.VISIBLE);
            }else{
                holder.btn_delete.setVisibility(View.GONE);
            }
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, null);
                }
            });
        }
        return convertView;
    }

    public static   class ViewHolder {
        ImageView item_head;
        TextView item_title;
        TextView btn_delete;
    }
}
