package com.ruiping.BankApp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.ruiping.BankApp.huanxin.DemoHelper;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView daily_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
