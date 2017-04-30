package com.ruiping.BankApp.ui;

import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMCallBack;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.ActivityTack;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankVersionData;
import com.ruiping.BankApp.entiy.BankVersion;
import com.ruiping.BankApp.entiy.SetFontSize;
import com.ruiping.BankApp.huanxin.DemoHelper;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.CustomerSpinner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView daily_count;

    private CustomerSpinner textSize;
    ArrayAdapter<String> adapterEmpType;
    private ArrayList<SetFontSize> empTypeList = new ArrayList<SetFontSize>();
    private ArrayList<String> empTypeListStr = new ArrayList<String>();
    private TextView fontsize_text;
    private SetFontSize tmpSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.mine_set_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        daily_count = (TextView) this.findViewById(R.id.daily_count);
        title.setText("设置");
        this.findViewById(R.id.liner_one).setOnClickListener(this);
        this.findViewById(R.id.liner_two).setOnClickListener(this);
        this.findViewById(R.id.liner_three).setOnClickListener(this);
        this.findViewById(R.id.liner_four).setOnClickListener(this);
        this.findViewById(R.id.liner_notice).setOnClickListener(this);
        daily_count.setText(getVersion());

        fontsize_text = (TextView) this.findViewById(R.id.fontsize_text);
        textSize = (CustomerSpinner) this.findViewById(R.id.textSize);

        empTypeList.add(new SetFontSize(getResources().getString(R.string.font_zhengchang), "16"));
        empTypeList.add(new SetFontSize(getResources().getString(R.string.font_small), "14"));
        empTypeList.add(new SetFontSize(getResources().getString(R.string.font_zhong), "18"));
        empTypeList.add(new SetFontSize(getResources().getString(R.string.font_big), "22"));
        empTypeList.add(new SetFontSize(getResources().getString(R.string.font_big_big), "26"));
        empTypeListStr.add(getResources().getString(R.string.font_zhengchang));
        empTypeListStr.add(getResources().getString(R.string.font_small));
        empTypeListStr.add(getResources().getString(R.string.font_zhong));
        empTypeListStr.add(getResources().getString(R.string.font_big));
        empTypeListStr.add(getResources().getString(R.string.font_big_big));
        adapterEmpType = new ArrayAdapter<String>(MineSetActivity.this, android.R.layout.simple_spinner_item, empTypeListStr);
        textSize.setList(empTypeListStr);
        textSize.setAdapter(adapterEmpType);
        textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmpSize = empTypeList.get(position);
                save("font_size", tmpSize.getSizeStr());
                //调用广播，刷新主页
                Intent intent1 = new Intent("change_color_size");
                sendBroadcast(intent1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    String getV(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_one:
            {
                //清楚缓存
            }
                break;
            case R.id.liner_two:
            {
                //新版本检查
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new CustomProgressDialog(MineSetActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.liner_three:
            {
                //二维码
                Intent intent  = new Intent(MineSetActivity.this, ErweimaActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_four:
            {
                //关于我们
                Intent intent  = new Intent(MineSetActivity.this, AboutOaActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_notice:
            {
                //通知
                Intent intent = new Intent(MineSetActivity.this, NoticesActivity.class);
                startActivity(intent);
            }
                break;

        }
    }

    public void quiteAction(View view){
        //退出
        AlertDialog dialog = new AlertDialog.Builder(MineSetActivity.this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getResources().getString(R.string.sure_quite))
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
    void logout() {
        save(Contance.EMP_PWD, "");
        //调用广播，刷新主页
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        save(Contance.EMP_ID, "");
                        save(Contance.EMP_PWD, "");
                        save(Contance.EMP_NAME, "");
                        save(Contance.EMP_SEX, "");
                        save(Contance.EMP_UP, "");
                        save(Contance.EMP_COVER, "");
                        save(Contance.EMP_IS_USE, "");
                        save(Contance.EMP_IS_CHECK, "");
                        save(Contance.EMP_DATELINE, "");
                        save(Contance.EMP_PUSH_ID, "");
                        save(Contance.EMP_DEVICE_ID, "");
                        save(Contance.EMP_CHANNEL_ID, "");
                        save(Contance.EMP_HX_NAME, "");
                        save(Contance.EMP_ROLE_ID, "");
                        save(Contance.EMP_TASK_NUM, "");
                        save(Contance.EMP_DAY_REPORT, "");
                        save(Contance.EMP_WEEK_REPORT, "");
                        save(Contance.EMP_YEAR_REPORT, "");
                        save(Contance.EMP_LOGIN_NUM, "");
                        save(Contance.EMP_EMAIL, "");
                        save(Contance.IS_MEETING, "");
                        save(Contance.EMP_NAME_UP, "");
                        save(Contance.GROUP_ID, "");
                        save(Contance.GROUP_NAME, "");
                        finish();
                        ActivityTack.getInstanse().exit(MineSetActivity.this);
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
    }

    BankVersion bankVersion ;
    public void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.CHECK_VERSION_CODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankVersionData data = getGson().fromJson(s, BankVersionData.class);
                                    bankVersion = data.getData();
                                    //更新
                                    final Uri uri = Uri.parse(bankVersion.getDownload_url());
                                    final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(it);
                                }else {
                                    showMsg(MineSetActivity.this, jo.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MineSetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MineSetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", getV());
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


    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            fontsize_text.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            if ("16".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
                textSize.setSelection(0, true);
            }
            if ("14".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
                textSize.setSelection(1, true);
            }
            if ("18".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
                textSize.setSelection(2, true);
            }
            if ("22".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
                textSize.setSelection(3, true);
            }
            if ("26".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
                textSize.setSelection(4, true);
            }
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("change_color_size")) {
                changeColorOrSize();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("change_color_size");//
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
