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
import com.ruiping.BankApp.entiy.BankOfficeMeeting;
import com.ruiping.BankApp.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemBangonghuiAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<BankOfficeMeeting> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemBangonghuiAdapter(List<BankOfficeMeeting> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_bgh, null);
            holder.item_daline_1 = (TextView) convertView.findViewById(R.id.item_daline_1);
            holder.item_daline_2 = (TextView) convertView.findViewById(R.id.item_daline_2);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankOfficeMeeting cell = lists.get(position);
        if (cell != null) {
            holder.item_daline_1.setText(cell.getCreateDate());
            holder.item_daline_2.setText(cell.getMeetingDate());
            String filePath = cell.getFilePath();
            String[] arras = filePath.split(",");
            StringBuffer strb = new StringBuffer();
            for(String str:arras){
                if(!StringUtil.isNullOrEmpty(str)){
                    String[] strA = str.split("\\|");
                    if(strA != null && strA.length == 2){
                        strb = strb.append(strA[0]+",");
                    }
                }
            }
            String str = String.valueOf(strb).substring(0,strb.length()-1).replaceAll("\n","").replaceAll(" ", "").replaceAll("\t", "");
            holder.item_cont.setText(str);
        }

        return convertView;
    }

    class ViewHolder {
        TextView item_daline_1;
        TextView item_cont;
        TextView item_daline_2;
    }
}
