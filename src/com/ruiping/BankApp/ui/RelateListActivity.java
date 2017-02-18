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
import com.ruiping.BankApp.adapter.ItemRelateAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.data.RelateObjData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.entiy.RelateObj;
import com.ruiping.BankApp.library.PullToRefreshBase;
import com.ruiping.BankApp.library.PullToRefreshListView;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 下属相关
 */
public class RelateListActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView back;
    private TextView right_btn;
    private TextView no_data;
    private PullToRefreshListView lstv;
    private ItemRelateAdapter adapter;
    private List<RelateObj> lists = new ArrayList<RelateObj>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relate_list_activity);
        registerBoradcastReceiver();
        initView();
        progressDialog = new CustomProgressDialog(RelateListActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        no_data = (TextView) this.findViewById(R.id.no_data);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setVisibility(View.GONE);
        back.setOnClickListener(this);
        right_btn.setOnClickListener(this);

        title.setText("下属相关信息");

        adapter = new ItemRelateAdapter(lists, RelateListActivity.this);
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
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size() > (position-1)){
                    RelateObj relateObj = lists.get(position-1);
                    if(relateObj != null){
                        switch (Integer.parseInt(relateObj.getRecordType())){
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            {
                                getDetail(relateObj.getRecordId(), relateObj.getRecordType());
                            }
                            break;
                            case 7:
                            {
                                //任务
                                Intent intent = new Intent(RelateListActivity.this, RenwuDetailActivity.class);
                                intent.putExtra("taskId", relateObj.getRecordId());
                                startActivity(intent);
                            }
                            break;

                        }
                    }
                }
            }
        });
    }


    private void getDetail(final String recordId, final String typeId) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_DETAIL_BY_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    if(data != null){
                                        BankJobReport bankJobReport = data.getData();
                                        if(bankJobReport != null){
                                            switch (Integer.parseInt(typeId)){
                                                case 1:
                                                {
                                                    //日报
                                                    Intent intent  = new Intent(RelateListActivity.this, DailyDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                                case 2:
                                                {
                                                    //周报
                                                    Intent intent  = new Intent(RelateListActivity.this, WeeklyDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                                case 3:
                                                {
                                                    //月报
                                                    Intent intent  = new Intent(RelateListActivity.this, MoonthDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                                case 4:
                                                {
                                                    //季报
                                                    Intent intent  = new Intent(RelateListActivity.this, QuarterDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                                case 5:
                                                {
                                                    //年zhong
                                                    Intent intent  = new Intent(RelateListActivity.this, YearMiddleDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                                case 6:
                                                {
                                                    //年度
                                                    Intent intent  = new Intent(RelateListActivity.this, YearDetailActivtiy.class);
                                                    intent.putExtra("bankJobReport", bankJobReport);
                                                    startActivity(intent);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(RelateListActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RelateListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RelateListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", recordId);
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

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.findBankRelation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    RelateObjData data = getGson().fromJson(s, RelateObjData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(RelateListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RelateListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RelateListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("pagecurrent", String.valueOf(pageIndex));
                params.put("type", "");
                params.put("pagesize", "10");
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




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加周报
            {
                Intent intent = new Intent(RelateListActivity.this, AddMineMonthActivtiy.class);
                startActivity(intent);
            }
                break;
        }
    }
    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_month_success")) {
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_month_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
