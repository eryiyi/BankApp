package com.ruiping.BankApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.pinyin.PingYinUtil;

import java.util.List;

/**
 * Created by zhl on 2016/9/7.
 * 通讯录
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {
    private Context mContext;
    private List<BankEmpBean> mNicks;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

            @SuppressWarnings("unchecked")
        public ContactAdapter(Context mContext, List<BankEmpBean> mNicks) {
            this.mContext = mContext;
            this.mNicks = mNicks;
            // 排序(实现了中英文混排)
//            Arrays.sort(mNicks, new PinyinComparator());
        }

        @Override
        public int getCount() {
            return mNicks.size();
        }

        @Override
        public Object getItem(int position) {
            return mNicks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BankEmpBean bankEmpBean = mNicks.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.contact_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tvCatalog = (TextView) convertView
                        .findViewById(R.id.contactitem_catalog);
                viewHolder.ivAvatar = (ImageView) convertView
                        .findViewById(R.id.contactitem_avatar_iv);
                viewHolder.tvNick = (TextView) convertView
                        .findViewById(R.id.contactitem_nick);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String catalog = PingYinUtil.converterToFirstSpell(bankEmpBean.getEmpName()==null?"":bankEmpBean.getEmpName())
                    .substring(0, 1);
            if (position == 0) {
                viewHolder.tvCatalog.setVisibility(View.VISIBLE);
                viewHolder.tvCatalog.setText(catalog);
            } else {
                String lastCatalog = PingYinUtil.converterToFirstSpell(
                        mNicks.get(position - 1).getEmpName()).substring(0, 1);
                if (catalog.equals(lastCatalog)) {
                    viewHolder.tvCatalog.setVisibility(View.GONE);
                } else {
                    viewHolder.tvCatalog.setVisibility(View.VISIBLE);
                    viewHolder.tvCatalog.setText(catalog);
                }
            }

//            viewHolder.ivAvatar.setImageResource(R.drawable.ic_launcher);
            imageLoader.displayImage(InternetURL.INTERNAL + bankEmpBean.getEmpCover(), viewHolder.ivAvatar, BankAppApplication.txOptions, animateFirstListener);
            viewHolder.tvNick.setText(bankEmpBean.getEmpName()==null?"":bankEmpBean.getEmpName());
            return convertView;
        }

        static class ViewHolder {
            TextView tvCatalog;// 目录
            ImageView ivAvatar;// 头像
            TextView tvNick;// 昵称
        }

        @Override
        public int getPositionForSection(int section) {
            for (int i = 0; i < mNicks.size(); i++) {
                String l = PingYinUtil.converterToFirstSpell(mNicks.get(i).getEmpName())
                        .substring(0, 1);
                char firstChar = l.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

}
