package com.ruiping.BankApp.ui;

import android.content.Intent;
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
import com.ruiping.BankApp.adapter.ItemBangonghuiAdapter;
import com.ruiping.BankApp.adapter.ItemDailyAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportData;
import com.ruiping.BankApp.data.BankOfficeMeetingData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.entiy.BankOfficeMeeting;
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
 * Created by zhl on 2016/8/30.
 * 办公会
 */
public class BangonghuiActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView no_data;
    private PullToRefreshListView lstv;
    private ItemBangonghuiAdapter adapter;
    private List<BankOfficeMeeting> lists = new ArrayList<BankOfficeMeeting>();
    private String type;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    boolean isMobileNet, isWifiNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bangonghui_activity);
       initView();
        progressDialog = new CustomProgressDialog(BangonghuiActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        initData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("办公会");
        no_data = (TextView) this.findViewById(R.id.no_data);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemBangonghuiAdapter(lists, BangonghuiActivity.this);
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
//                Intent intent = new Intent(BangonghuiActivity.this, DailyDetailActivtiy.class);
//                BankOfficeMeeting bankJobReport = lists.get(position-1);
//                intent.putExtra("bankJobReport", bankJobReport);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void initData() {
        //判断是否有网
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(BangonghuiActivity.this);
            isWifiNet = HttpUtils.isWifiDataEnable(BangonghuiActivity.this);
            if (!isMobileNet && !isWifiNet) {
                showMsg(BangonghuiActivity.this, "请检查您的网络链接");
            } else {
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        InternetURL.GET_MEETING_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (StringUtil.isJson(s)) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String code1 = jo.getString("code");
                                        if (Integer.parseInt(code1) == 200) {
                                            BankOfficeMeetingData data = getGson().fromJson(s, BankOfficeMeetingData.class);
                                            if (IS_REFRESH) {
                                                lists.clear();
                                            }
                                            lists.addAll(data.getData());
                                            lstv.onRefreshComplete();
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(BangonghuiActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(BangonghuiActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BangonghuiActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("currpage", String.valueOf(pageIndex));
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

}
