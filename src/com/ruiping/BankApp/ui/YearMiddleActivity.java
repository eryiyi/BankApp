package com.ruiping.BankApp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.IndexCountObjData;
import com.ruiping.BankApp.entiy.IndexCountObj;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 年中目录
 */
public class YearMiddleActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private TextView daily_count;
    private TextView xiashuCount;
    private TextView comment_count;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year_middle_activity);
        initView();
        registerBoradcastReceiver();
        progressDialog = new CustomProgressDialog(YearMiddleActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //查询统计数据
        getMineCount();
        changeColorOrSize();
    }

    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title = (TextView) this.findViewById(R.id.title);
        back.setOnClickListener(this);
        title.setText("年中报告目录");
        right_btn.setText("添加");
        right_btn.setOnClickListener(this);
        this.findViewById(R.id.liner_one).setOnClickListener(this);
        this.findViewById(R.id.liner_two).setOnClickListener(this);
        this.findViewById(R.id.liner_three).setOnClickListener(this);

        daily_count = (TextView) this.findViewById(R.id.daily_count);
        xiashuCount = (TextView) this.findViewById(R.id.xiashuCount);
        comment_count = (TextView) this.findViewById(R.id.comment_count);

        txt1 = (TextView) this.findViewById(R.id.txt1);
        txt2 = (TextView) this.findViewById(R.id.txt2);
        txt3 = (TextView) this.findViewById(R.id.txt3);
    }

    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            txt1.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt2.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt3.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));

            daily_count.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            xiashuCount.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            comment_count.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加
            {
                Intent intent = new Intent(YearMiddleActivity.this, AddYearMiddleActivtiy.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_one:
            {
                //我的年中
                Intent intent = new Intent(YearMiddleActivity.this, AddYearMiddleActivtiy.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_two:
            {
                //下属年中
                Intent intent = new Intent(YearMiddleActivity.this, YearMiddleXiashuActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_three:
            {
                //评论我的年中
                Intent intent = new Intent(YearMiddleActivity.this, YearMiddleCommentActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    IndexCountObj indexCountObj;
    //查询评论我的年中数
    private void getMineCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_REPORT_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            IndexCountObjData data = getGson().fromJson(s, IndexCountObjData.class);
                            if (data.getCode() == 200) {
                                indexCountObj = data.getData();
                                initData();
                            } else {
                                Toast.makeText(YearMiddleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(YearMiddleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(YearMiddleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportType", "5");
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

    void initData(){
        //填充数据
        daily_count.setText(String.valueOf(indexCountObj.getKey1()));
        xiashuCount.setText(String.valueOf(indexCountObj.getKey2()));
        comment_count.setText(String.valueOf(indexCountObj.getKey3()));
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_year_success")) {
                getMineCount();
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_year_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
