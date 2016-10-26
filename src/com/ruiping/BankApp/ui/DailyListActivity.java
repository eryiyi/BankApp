package com.ruiping.BankApp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemDailyAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportData;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.library.PullToRefreshBase;
import com.ruiping.BankApp.library.PullToRefreshListView;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.HttpUtils;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 */
public class DailyListActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView back;
    private TextView right_btn;
    private TextView no_data;
    private PullToRefreshListView lstv;
    private ItemDailyAdapter adapter;
    private List<BankJobReport> lists = new ArrayList<BankJobReport>();
    private String type;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    boolean isMobileNet, isWifiNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_list_activity);
        registerBoradcastReceiver();
        type = getIntent().getExtras().getString("type");
        initView();
        progressDialog = new CustomProgressDialog(DailyListActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        initData();

        //查询今天的日报
        getData();
    }

    private void initView() {
        no_data = (TextView) this.findViewById(R.id.no_data);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("添加");
        back.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        switch (Integer.parseInt(type)){
            case 1:
                title.setText("我的日报");
                break;
            case 2:
                title.setText("下属日报");
                right_btn.setVisibility(View.GONE);
                break;
            case 3:
                title.setText("评论的日报");
                right_btn.setVisibility(View.GONE);
                break;
        }


        adapter = new ItemDailyAdapter(lists, DailyListActivity.this);
        lstv.setAdapter(adapter);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DailyListActivity.this, DailyDetailActivtiy.class);
                BankJobReport bankJobReport = lists.get(position-1);
                intent.putExtra("bankJobReport", bankJobReport);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加日报
            {
                if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
                    //已经存在了
                    Intent intent = new Intent(DailyListActivity.this, DailyDetailActivtiy.class);
                    intent.putExtra("bankJobReport", bankJobReport);
                    startActivity(intent);
                }else{
                    //不存在日报信息
                    Intent intent = new Intent(DailyListActivity.this, AddDailyActivity.class);
                    startActivity(intent);
                }
            }
                break;
        }
    }

    private void initData() {
        //判断是否有网
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(DailyListActivity.this);
            isWifiNet = HttpUtils.isWifiDataEnable(DailyListActivity.this);
            if (!isMobileNet && !isWifiNet) {
                showMsg(DailyListActivity.this, "请检查您的网络链接");
            } else {
                String internal_url = "";
                switch (Integer.parseInt(type)){
                    case 1:
                        internal_url = InternetURL.REPORT_LISTS_URL;
                        break;
                    case 2:
                        internal_url = InternetURL.REPORT_LISTS_SUBORDINATE_URL;
                        break;
                    case 3:
                        internal_url = InternetURL.REPORT_COMMENTS_MINE_URL;
                        break;
                }
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        internal_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (StringUtil.isJson(s)) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String code1 = jo.getString("code");
                                        if (Integer.parseInt(code1) == 200) {
                                            BankJobReportData data = getGson().fromJson(s, BankJobReportData.class);
                                            if (IS_REFRESH) {
                                                lists.clear();
                                            }
                                            lists.addAll(data.getData());
                                            lstv.onRefreshComplete();
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(DailyListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(DailyListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                if (lists.size() == 0) {
                                    no_data.setVisibility(View.VISIBLE);
                                    lstv.setVisibility(View.GONE);
                                } else {
                                    no_data.setVisibility(View.GONE);
                                    lstv.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(DailyListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pagecurrent", String.valueOf(pageIndex));
                        params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                        params.put("reportType", "1");
                        if("1".equals(type)){
                            params.put("year", DateUtil.getYearAndMonth());
                            params.put("day", DateUtil.getDay());
                        }
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                getRequestQueue().add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_report_daily_success")) {
                BankJobReport bankJobReport = (BankJobReport) intent.getExtras().get("bankJobReportCommentBean");
                lists.add(0,bankJobReport);
                adapter.notifyDataSetChanged();
            }
            if(action.equals("update_report_daily_success")){
                initData();
            }

            if(action.equals("add_report_comment_success")){
                initData();
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_report_daily_success");
        myIntentFilter.addAction("update_report_daily_success");
        myIntentFilter.addAction("add_report_comment_success");//添加日报评论
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    BankJobReport bankJobReport;
    //查询日报 今天的
    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_DATE_BY_EMP_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    bankJobReport = data.getData();
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
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
                        Toast.makeText(DailyListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("yearmonth", DateUtil.getYear() +"-"+ DateUtil.getMonth() +"-"+ DateUtil.getDay());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

}
