package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.ruiping.BankApp.base.ActivityTack;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpBeanData;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.huanxin.DemoHelper;
import com.ruiping.BankApp.huanxin.db.DemoDBManager;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.util.Utils;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText password;
    private BankEmpBean bankEmpBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {
            }
            @Override
            public void onError(int code, String message) {
            }
        });
        initView();

        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(LoginActivity.this, "api_key"));
    }

    private void initView() {
        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_PWD, ""), String.class))){
            password.setText(getGson().fromJson(getSp().getString(Contance.EMP_PWD, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_PWD, ""), String.class))){
            progressDialog = new CustomProgressDialog(LoginActivity.this, "正在加载中",R.anim.custom_dialog_frame);
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            login();
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void loginAction(View view){
        //登录
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(LoginActivity.this, "请输入手机号");
            return;
        }
        if(mobile.getText().toString().length() != 11){
            showMsg(LoginActivity.this, "请输入正确的手机号");
            return;
        }
        if(StringUtil.isNullOrEmpty(password.getText().toString())){
            showMsg(LoginActivity.this, "请输入密码");
            return;
        }
        progressDialog = new CustomProgressDialog(LoginActivity.this, "登陆中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        login();
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
                                    showMsg(LoginActivity.this, jo.getString("message"));
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mobile.getText().toString());
                params.put("pwd", password.getText().toString());

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
                Utils.getMetaValue(LoginActivity.this, "api_key"));

        save(Contance.EMP_ID, bankEmpBean.getEmpId());
        save(Contance.EMP_MOBILE, bankEmpBean.getEmpMobile());
        save(Contance.EMP_PWD, password.getText().toString());
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
            save(Contance.EMP_NAME_UP, bankEmpBean.getBankemp().getEmpName());
        }
        if(bankEmpBean.getBankGroup() != null){
            save(Contance.GROUP_ID, bankEmpBean.getBankGroup().getGroupId());
            save(Contance.GROUP_NAME, bankEmpBean.getBankGroup().getGroupName());
        }

        DemoDBManager.getInstance().closeDB();

        // 将自己服务器返回的环信账号、昵称和头像URL设置到帮助类中。
        DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(bankEmpBean.getEmpName());
        DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(bankEmpBean.getEmpCover());
        DemoHelper.getInstance().setCurrentUserName(bankEmpBean.getHx_name());// 环信Id

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

                Intent intent = new Intent(LoginActivity.this,
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
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
