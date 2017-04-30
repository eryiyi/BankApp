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
import com.ruiping.BankApp.data.BankNoteBeanSingleData;
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/3.
 * 备忘录目录
 */
public class MemoActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private TextView daily_count;
    private TextView xiashuCount;
    private TextView txt1;
    private TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.memo_activity);
        initView();
        progressDialog = new CustomProgressDialog(MemoActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //查询统计数据
        getMineCount();
        getMineCount2();
        changeColorOrSize();
    }
    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title = (TextView) this.findViewById(R.id.title);
        back.setOnClickListener(this);
        title.setText("备忘录目录");
        right_btn.setText("添加");
        right_btn.setOnClickListener(this);
        this.findViewById(R.id.liner_one).setOnClickListener(this);
        this.findViewById(R.id.liner_two).setOnClickListener(this);

        daily_count = (TextView) this.findViewById(R.id.daily_count);
        xiashuCount = (TextView) this.findViewById(R.id.xiashuCount);
        txt1 = (TextView) this.findViewById(R.id.txt1);
        txt2 = (TextView) this.findViewById(R.id.txt2);

    }

    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            txt1.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt2.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));

            daily_count.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            xiashuCount.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
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
                Intent intent = new Intent(MemoActivity.this, AddMemoActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_one:
            {
                //我的备忘录
                Intent intent = new Intent(MemoActivity.this, MemoListActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_two:
            {
                //已完成的备忘
                Intent intent = new Intent(MemoActivity.this, MemoListDoneActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
    BankNoteBean bankNoteBean;

    private void getMineCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MOME_NOTE_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankNoteBeanSingleData data = getGson().fromJson(s, BankNoteBeanSingleData.class);
                                    bankNoteBean = data.getData();
                                    if(bankNoteBean != null ){
                                        daily_count.setText(String.valueOf(bankNoteBean.getCount()));
                                    }
                                } else {
                                    Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
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

    private void getMineCount2() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MOME_NOTE_FINISHED_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankNoteBeanSingleData data = getGson().fromJson(s, BankNoteBeanSingleData.class);
                                    bankNoteBean = data.getData();
                                    if(bankNoteBean != null ){
                                        xiashuCount.setText(String.valueOf(bankNoteBean.getCount()));
                                    }
                                } else {
                                    Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_memo_success")) {
              //添加备忘录成功
                daily_count.setText(String.valueOf(Integer.parseInt(daily_count.getText().toString())+1));
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_memo_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


}
