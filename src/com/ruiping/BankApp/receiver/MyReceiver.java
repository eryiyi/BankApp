package com.ruiping.BankApp.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.huawei.android.pushagent.api.PushEventReceiver;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONObject;

/*
 * 接收Push所有消息的广播接收器
 */
public class MyReceiver extends PushEventReceiver  {

    /*
     * 显示Push消息
     */
    public void showPushMessage(int type, String msg) {
        String str = msg;
//        PustDemoActivity mPustTestActivity = MyApplication.instance().getMainActivity();
//        if (mPustTestActivity != null) {
//            Handler handler = mPustTestActivity.getHandler();
//            if (handler != null) {
//                Message message = handler.obtainMessage();
//                message.what = type;
//                message.obj = msg;
//                handler.sendMessageDelayed(message, 1L);
//            }
//        }
    }

    @Override
    public void onToken(Context context, String token, Bundle extras){
        String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        updateContent(context, token);
//        Log.d(PustDemoActivity.TAG, content);
//        showPushMessage(PustDemoActivity.RECEIVE_TOKEN_MSG, content);
    }


    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            int type = 1;
            String content = new String(msg, "UTF-8");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intentClick = new Intent(context, NotificationBroadcastReceiver.class);
            intentClick.setAction("notification_clicked");
            intentClick.putExtra(NotificationBroadcastReceiver.TYPE, type);
            PendingIntent pendingIntentClick = PendingIntent.getBroadcast(context, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);

            Intent intentCancel = new Intent(context, NotificationBroadcastReceiver.class);
            intentCancel.setAction("notification_cancelled");
            intentCancel.putExtra(NotificationBroadcastReceiver.TYPE, type);
            PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(context, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

            Notification notification = builder
                    .setContentTitle(content)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setColor(Color.parseColor("#EAA935"))
                    .setSound(ringUri)
                    .setContentIntent(pendingIntentClick)
                    .setDeleteIntent(pendingIntentCancel)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher)).build();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(type, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
//            Log.d(PustDemoActivity.TAG, content);
//            showPushMessage(PustDemoActivity.RECEIVE_NOTIFY_CLICK_MSG, content);
        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if(TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
//            Log.d(PustDemoActivity.TAG, message + isSuccess);
//            showPushMessage(PustDemoActivity.RECEIVE_TAG_LBS_MSG, message + isSuccess);
        }
        super.onEvent(context, event, extras);
    }


    private void updateContent(Context context, final String token) {
        final SharedPreferences sp = context.getSharedPreferences("bank_oa_manage", Context.MODE_PRIVATE);
        String empId = new Gson().fromJson(sp.getString(Contance.EMP_ID, ""), String.class);
        if(!StringUtil.isNullOrEmpty(empId)){
            RequestQueue queue = Volley.newRequestQueue(context);
            String uri = String.format(InternetURL.UPDATE_PUSH_ID_URL_HW + "&empId=%s&huaweiTK=%s", empId, token);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
//                                    SharedPreferences.Editor editor = sp.edit();
//                                    editor.putString("push_user_id", userId).commit();
                                }
                            } catch (Exception e) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
            );
            queue.add(request);
        }
    }

}
