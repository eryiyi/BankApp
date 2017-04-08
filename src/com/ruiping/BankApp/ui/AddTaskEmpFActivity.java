package com.ruiping.BankApp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ContactAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpData;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.pinyin.PinyinComparator;
import com.ruiping.BankApp.pinyin.SideBar;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/8/30.
 * 新建任务  选择负责人
 */
public class AddTaskEmpFActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private String taskId;//任务ID
    private String taskTitle;//任务标题

    private ListView lvContact;
    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView mDialogText;

    private List<BankEmpBean> listEmps = new ArrayList<BankEmpBean>();
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_2_activity);
        taskId = getIntent().getExtras().getString("taskId");
        taskTitle = getIntent().getExtras().getString("taskTitle");

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initView();
        progressDialog = new CustomProgressDialog(AddTaskEmpFActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    void initView(){
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择负责人");
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);

        lvContact = (ListView) this.findViewById(R.id.lvContact);
        adapter = new ContactAdapter(AddTaskEmpFActivity.this, listEmps);
        lvContact.setAdapter(adapter);
        indexBar = (SideBar) this.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        mDialogText = (TextView) LayoutInflater.from(this).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listEmps.size()>i){
                    BankEmpBean bankEmpBean = listEmps.get(i);
                    if(bankEmpBean != null){
                        showDialog(bankEmpBean);
                    }
                }
            }
        });
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_FRIENDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankEmpData data = getGson().fromJson(s, BankEmpData.class);
                                    listEmps.clear();
                                    listEmps.addAll(data.getData());
                                    Collections.sort(listEmps, new PinyinComparator());
                                    adapter.notifyDataSetChanged();
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
                        Toast.makeText(AddTaskEmpFActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void showDialog(final BankEmpBean bankEmpBean) {
        final Dialog picAddDialog = new Dialog(AddTaskEmpFActivity.this, R.style.MyAlertDialog);
        View picAddInflate = View.inflate(this, R.layout.dialog_task_empf, null);

        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        TextView taskEmpName = (TextView) picAddInflate.findViewById(R.id.taskEmpName);
        String dataName = getResources().getString(R.string.task_emp_name);
        dataName = String.format(dataName, bankEmpBean.getEmpName());
        taskEmpName.setText(dataName);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新
                updateEmpTask(bankEmpBean.getEmpId());
                picAddDialog.dismiss();
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void updateEmpTask(final String empIdF) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskUpdateManagerEmpId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    Intent intent = new Intent(AddTaskEmpFActivity.this, AddTaskDatelineActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    intent.putExtra("taskTitle", taskTitle);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    showMsg(AddTaskEmpFActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskEmpFActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskEmpFActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empIdF", empIdF);
                params.put("taskId", taskId);
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
