package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.MainActivity;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpBeanData;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.huanxin.DemoHelper;
import com.ruiping.BankApp.huanxin.db.DemoDBManager;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener,Runnable {
    private BankEmpBean bankEmpBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }

    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(1000);
            SharedPreferences.Editor editor = getSp().edit();
            boolean isFirstRun = getSp().getBoolean("isFirstRun", true);
            if (isFirstRun) {
                editor.putBoolean("isFirstRun", false);
                editor.commit();
                Intent loadIntent = new Intent(WelcomeActivity.this, AboutActivity.class);
                startActivity(loadIntent);
                finish();
            } else {
                if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class)
                ) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_PWD, ""), String.class))) {
                    login();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

    }
    private void login() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    BankEmpBeanData data = getGson().fromJson(s, BankEmpBeanData.class);
                                    bankEmpBean= data.getData();
                                    saveEmp();
                                }else {
                                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
                params.put("pwd", getGson().fromJson(getSp().getString(Contance.EMP_PWD, ""), String.class));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //保存登录用户的信息
    void saveEmp(){
        //进行绑定
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(WelcomeActivity.this, "api_key"));
        save(Contance.EMP_ID, bankEmpBean.getEmpId());
        save(Contance.EMP_MOBILE, bankEmpBean.getEmpMobile());
        save(Contance.EMP_NAME, bankEmpBean.getEmpName());
        save(Contance.EMP_SEX, bankEmpBean.getEmpSex());
        save(Contance.EMP_UP, bankEmpBean.getEmpIdUp());
        save(Contance.EMP_COVER, InternetURL.INTERNAL + bankEmpBean.getEmpCover());
        save(Contance.EMP_IS_USE, bankEmpBean.getIsUse());
        save(Contance.EMP_IS_CHECK, bankEmpBean.getIsCheck());
        save(Contance.EMP_DATELINE, bankEmpBean.getDateLine());
        save(Contance.EMP_PUSH_ID, bankEmpBean.getPushId());
        save(Contance.EMP_DEVICE_ID, bankEmpBean.getDeviceId());
        save(Contance.EMP_CHANNEL_ID, bankEmpBean.getChannelId());
        save(Contance.EMP_HX_NAME, bankEmpBean.getHx_name());
        save(Contance.EMP_ROLE_ID, bankEmpBean.getRoleid());
        save(Contance.EMP_TASK_NUM, bankEmpBean.getTasknum());
        save(Contance.EMP_DAY_REPORT, bankEmpBean.getDayreport());
        save(Contance.EMP_WEEK_REPORT, bankEmpBean.getWeekreport());
        save(Contance.EMP_YEAR_REPORT, bankEmpBean.getYearreport());
        save(Contance.EMP_LOGIN_NUM, bankEmpBean.getLoginnum());
        save(Contance.EMP_EMAIL, bankEmpBean.getEmail());
        save(Contance.IS_MEETING, bankEmpBean.getIsMeeting());
        if(!StringUtil.isNullOrEmpty(bankEmpBean.getEmpIdUp())){
            if(bankEmpBean.getBankemp() != null){
                save(Contance.EMP_NAME_UP, bankEmpBean.getBankemp().getEmpName());
            }
        }
        if(bankEmpBean.getBankGroup() != null){
            save(Contance.GROUP_ID, bankEmpBean.getBankGroup().getGroupId());
            save(Contance.GROUP_NAME, bankEmpBean.getBankGroup().getGroupName());
        }


        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(bankEmpBean.getHx_name());
        final long start = System.currentTimeMillis();

        EMClient.getInstance().login(bankEmpBean.getHx_name(), "qs123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        BankAppApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(WelcomeActivity.this, getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(WelcomeActivity.this,
                                LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        });

    }

}
