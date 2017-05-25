package com.ruiping.BankApp.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ruiping.BankApp.ui.RenwuListActivity;

/**
 * Created by zhl on 2017/5/25.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);

        if (type != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }

        if (action.equals("notification_clicked")) {
            //处理点击事件
            Intent intent1 = new Intent(context, RenwuListActivity.class);
            intent1.putExtra("type", "1");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

        if (action.equals("notification_cancelled")) {
            //处理滑动清除和点击删除事件
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }

    }


}
