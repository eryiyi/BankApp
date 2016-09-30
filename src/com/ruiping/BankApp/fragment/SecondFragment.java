package com.ruiping.BankApp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseFragment;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.data.IndexCountObjData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.entiy.IndexCountObj;
import com.ruiping.BankApp.ui.AddDailyActivity;
import com.ruiping.BankApp.ui.DailyDetailActivtiy;
import com.ruiping.BankApp.ui.DailyListActivity;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private Resources res;

    private TextView title;
    private TextView right_btn;
    private TextView back;
    private TextView daily_count;
    private TextView xiashuCount;
    private TextView comment_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.two_fragment, null);
        res = getActivity().getResources();
        initView();
        progressDialog = new CustomProgressDialog(getActivity(), "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //查询统计数据
        getMineCount();
        //查询今天的日报
        getData();
        return view;
    }

    private void initView() {
        title = (TextView) view.findViewById(R.id.title);
        right_btn = (TextView) view.findViewById(R.id.right_btn);
        back = (TextView) view.findViewById(R.id.back);
        daily_count = (TextView) view.findViewById(R.id.daily_count);
        xiashuCount = (TextView) view.findViewById(R.id.xiashuCount);
        comment_count = (TextView) view.findViewById(R.id.comment_count);
        back.setVisibility(View.GONE);
        right_btn.setOnClickListener(this);
        right_btn.setText("添加");
        title.setText("日报目录");

        view.findViewById(R.id.liner_one).setOnClickListener(this);
        view.findViewById(R.id.liner_two).setOnClickListener(this);
        view.findViewById(R.id.liner_three).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                //添加
            {
                if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
                    //已经存在了
                    Intent intent = new Intent(getActivity(), DailyDetailActivtiy.class);
                    intent.putExtra("bankJobReport", bankJobReport);
                    startActivity(intent);
                }else{
                    //不存在日报信息
                    Intent intent = new Intent(getActivity(), AddDailyActivity.class);
                    startActivity(intent);
                }
            }
                break;
            case R.id.liner_one:
                //我的日报
            {
                Intent intent = new Intent(getActivity(), DailyListActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
            break;
            case R.id.liner_two:
                //下属日报
            {
                Intent intent = new Intent(getActivity(), DailyListActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
            break;
            case R.id.liner_three:
                //评论的日报
            {
                Intent intent = new Intent(getActivity(), DailyListActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
            }
            break;

        }
    }

    IndexCountObj indexCountObj;
    //查询评论我的日报数
    private void getMineCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_REPORT_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    IndexCountObjData data = getGson().fromJson(s, IndexCountObjData.class);
                                    indexCountObj = data.getData();
                                    initData();
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportType", "1");
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
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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