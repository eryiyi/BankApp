package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.DoubleDatePickerDialog;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 更新任务开始日期和结束日期
 */
public class AddTaskDatelineActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private TextView starttime;//开始日期
    private TextView endtime;//到期日


    private String taskId;//任务id
    private String taskTitle;//任务标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_update_dateline_activity);
        taskId = getIntent().getExtras().getString("taskId");
        taskTitle = getIntent().getExtras().getString("taskTitle");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置任务开始和结束日期");

        starttime = (TextView) this.findViewById(R.id.starttime);
        endtime = (TextView) this.findViewById(R.id.endtime);

        this.findViewById(R.id.liner_starttime).setOnClickListener(this);
        this.findViewById(R.id.liner_endtime).setOnClickListener(this);

//        starttime.setText(DateUtil.getDateAndTimeTwo());
    }

    private String startTimeStr = "";
    private String endTimeStr = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break; case R.id.liner_starttime:
            {
                    //开始日期
                    Calendar c = Calendar.getInstance();
                    // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                    new DoubleDatePickerDialog(AddTaskDatelineActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                              int startDayOfMonth) {
                            startTimeStr = String.format("%d-%d-%d", startYear,
                                    startMonthOfYear + 1, startDayOfMonth);
                            starttime.setText(startTimeStr);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
            break;
            case R.id.liner_endtime:
            {
                    //到期日期
                    Calendar c = Calendar.getInstance();
                    // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                    new DoubleDatePickerDialog(AddTaskDatelineActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                              int startDayOfMonth) {
                            endTimeStr = String.format("%d-%d-%d", startYear,
                                    startMonthOfYear + 1, startDayOfMonth);
                            endtime.setText(endTimeStr);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
            break;

        }
    }

    //更新日期
    private void updateDateline(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskUpdateTaskDate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    Intent intent = new Intent(AddTaskDatelineActivity.this, AddTaskEmpSActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    intent.putExtra("taskTitle", taskTitle);
                                    intent.putExtra("dateLine", starttime.getText().toString());
                                    intent.putExtra("dateLineEnd", endtime.getText().toString());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddTaskDatelineActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddTaskDatelineActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskDatelineActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("dateLine", starttime.getText().toString());
                params.put("dateLineEnd", endtime.getText().toString());
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


    public void saveTaskDatelineAction(View view){
        if(StringUtil.isNullOrEmpty(starttime.getText().toString()) || StringUtil.isNullOrEmpty(endtime.getText().toString())){
            showMsg(AddTaskDatelineActivity.this, "请选择日期");
            return;
        }
        updateDateline();
    }
}
