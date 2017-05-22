package com.ruiping.BankApp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportData;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.datapicker.MonthDateView;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class DailyWeekActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;

    List<Integer> list = new ArrayList<Integer>();
    List<BankJobReport> listReports = new ArrayList<BankJobReport>();
    String currentDateAndDay = "";//当期的年月日
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_week_activity);
        registerBoradcastReceiver();
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的日报");

        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week  =(TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date,tv_week);

        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
                String dateAndDay = (monthDateView.getmSelYear()+"")  + (monthDateView.getmSelMonth()+"") +((monthDateView.getmSelDay()) +"");
//                if(Integer.parseInt(dateAndDay) > Integer.parseInt(currentDateAndDay)){
//                    showMsg(DailyWeekActivity.this, "该日报暂时不能添加");
//                }else {
                    int year = monthDateView.getmSelYear();
                    int month = monthDateView.getmSelMonth()+1;
                    int day = monthDateView.getmSelDay();
                    String yearmonth = "";
                    if(String.valueOf(month).length() == 2){
                        if(String.valueOf(day).length() == 2){
                            yearmonth = String.valueOf(year) +"-"+ String.valueOf(month) +"-"+ String.valueOf(day);
                        }else {
                            yearmonth = String.valueOf(year) +"-"+ String.valueOf(month) +"-0"+ String.valueOf(day);
                        }
                    }else {
                        if(String.valueOf(day).length() == 2){
                            yearmonth = String.valueOf(year) +"-0"+ String.valueOf(month) +"-"+ String.valueOf(day);
                        }else {
                            yearmonth = String.valueOf(year) +"-0"+ String.valueOf(month) +"-0"+ String.valueOf(day);
                        }
                    }
                    progressDialog = new CustomProgressDialog(DailyWeekActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    getDailyByDay(yearmonth);
//                }
            }
        });
        setOnlistener();

        progressDialog = new CustomProgressDialog(DailyWeekActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();

        currentDateAndDay = (monthDateView.getmSelYear()+"")  + (monthDateView.getmSelMonth()+"") + (monthDateView.getmSelDay() +"");
    }

    private void setOnlistener(){
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list.clear();
                monthDateView.onLeftClick();
                getData();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list.clear();
                monthDateView.onRightClick();
                getData();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list.clear();
                monthDateView.setTodayToView();
                getData();
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

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.getReportonMoths,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobReportData data = getGson().fromJson(s, BankJobReportData.class);
                                    if(data != null){
                                        listReports.clear();
                                        listReports.addAll(data.getData());
                                        if(listReports != null){
                                            list.clear();
                                            for(BankJobReport bankJobReport:listReports){
                                                if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getDate())){
                                                    list.add(Integer.parseInt(bankJobReport.getDate()));
                                                }
                                            }
                                        }
                                        monthDateView.setDaysHasThingList(list);
                                        monthDateView.refreshView();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("year",  String.valueOf(MonthDateView.mSelYear));
                if(!StringUtil.isNullOrEmpty(String.valueOf(MonthDateView.mSelMonth+1))){
                    String month = String.valueOf(MonthDateView.mSelMonth+1);
                    if(month.length() == 2){
                        params.put("month", month);
                    }else{
                        params.put("month", "0"+month);
                    }
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
        getRequestQueue().add(request);
    }


    void getDailyByDay(final String yearmonth){
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
                                    if(data != null){
                                        BankJobReport bankJobReport = data.getData();
                                        if(bankJobReport != null){
                                            //已经存在了
                                            Intent intent = new Intent(DailyWeekActivity.this, DailyDetailActivtiy.class);
                                            intent.putExtra("bankJobReport", bankJobReport);
                                            startActivity(intent);
                                        } else{
                                            //不存在日报信息
                                            Intent intent = new Intent(DailyWeekActivity.this, AddDailyActivity.class);
                                            intent.putExtra("tmpDate", yearmonth);
                                            startActivity(intent);
                                        }
                                    }
                                }else if(Integer.parseInt(code1) == 3){
                                    //不存在日报信息
                                    Intent intent = new Intent(DailyWeekActivity.this, AddDailyActivity.class);
                                    intent.putExtra("tmpDate", yearmonth);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
                        Toast.makeText(DailyWeekActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("yearmonth", yearmonth);
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

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_report_daily_success")) {
                getData();
            }
            if (action.equals("del_report_daily_success")) {
                getData();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_report_daily_success");
        myIntentFilter.addAction("del_report_daily_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
