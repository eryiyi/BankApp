package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemWeeklyAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportData;
import com.ruiping.BankApp.entiy.BankJobReport;
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
 * 评论我的年度
 */
public class YearCommentActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView back;
    private TextView right_btn;
    private TextView no_data;
    private PullToRefreshListView lstv;
    private ItemWeeklyAdapter adapter;
    private List<BankJobReport> lists = new ArrayList<BankJobReport>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_list_activity);
        initView();
        progressDialog = new CustomProgressDialog(YearCommentActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getWeekDate();
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

        title.setText("评论我的");

        adapter = new ItemWeeklyAdapter(lists, YearCommentActivity.this);
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
                getWeekDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getWeekDate();
            }
        });
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankJobReport bankJobReport = lists.get(position-1);
                Intent intent = new Intent(YearCommentActivity.this, YearDetailActivtiy.class);
                intent.putExtra("bankJobReport", bankJobReport);
                startActivity(intent);
            }
        });
    }
    //查询我的年中
    private void getWeekDate() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_COMMENT_LIST_MINE_URL,
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
                                    Toast.makeText(YearCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(YearCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(YearCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportType", "6");
                params.put("pagecurrent", String.valueOf(pageIndex));
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
                //添加年中报告
            {
                Intent intent = new Intent(YearCommentActivity.this, AddYearActivtiy.class);
                startActivity(intent);
            }
            break;
        }
    }
}
