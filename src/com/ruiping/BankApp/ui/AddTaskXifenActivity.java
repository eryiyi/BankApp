package com.ruiping.BankApp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemRenwuAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobTaskData;
import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AddTaskXifenActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private TextView no_data;

    private String taskId;//任务ID

    private List<BankJobTask> lists = new ArrayList<BankJobTask>();
    private ItemRenwuAdapter adapter;

    private TextView right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_5_activity);
        taskId = getIntent().getExtras().getString("taskId");
        initView();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("任务细分");
        right_btn.setText("下一步");
        right_btn.setOnClickListener(this);
        no_data = (TextView) this.findViewById(R.id.no_data);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemRenwuAdapter(lists, AddTaskXifenActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size() > position){
                    BankJobTask bankJobTask = lists.get(position);
                    if(bankJobTask != null){
                        taskTmp = position;
                        titleTmp = bankJobTask.getTaskTitle();
                        contentTmp = bankJobTask.getTaskCont();
                        showDialogContent(bankJobTask);
                    }
                }
            }
        });
    }

    private int taskTmp;

    private String titleTmp="";
    private String contentTmp="";

    private void showDialogContent(final BankJobTask bankJobTask) {

        final Dialog picAddDialog = new Dialog(AddTaskXifenActivity.this, R.style.MyAlertDialog);
        View picAddInflate = View.inflate(this, R.layout.dialog_task_child_content, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);

        TextView taskEmpName = (TextView) picAddInflate.findViewById(R.id.taskEmpName);
        String dataName = getResources().getString(R.string.task_emp_name_f);
        if(bankJobTask.getBankEmpf() != null){
            dataName = String.format(dataName, bankJobTask.getBankEmpf().getEmpName());
            taskEmpName.setText(dataName);
        }

       final EditText title = (EditText) picAddInflate.findViewById(R.id.title);
//       final EditText content = (EditText) picAddInflate.findViewById(R.id.content);

        title.setText(bankJobTask.getTaskTitle()==null?"":bankJobTask.getTaskTitle());
//        content.setText(bankJobTask.getTaskCont()==null?"":bankJobTask.getTaskCont());

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新
                if(StringUtil.isNullOrEmpty(title.getText().toString())){
                    showMsg(AddTaskXifenActivity.this, "请输入标题！");
                    return;
                }else {
                    updateEmpTask( bankJobTask.getTaskId(), title.getText().toString());
                    picAddDialog.dismiss();
                }
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

    private void updateEmpTask(final String taskId, final String title) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskUpdateDetailTask,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    //taskIdTmp
//                                    lists.get(taskTmp).setTaskTitle(titleTmp);
//                                    lists.get(taskTmp).setTaskCont(contentTmp);
                                    getData();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    showMsg(AddTaskXifenActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskXifenActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskXifenActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("taskTitle", title);
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

    private void getData() {
        progressDialog = new CustomProgressDialog(AddTaskXifenActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskFindTaskAndSubTask,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobTaskData data = getGson().fromJson(s, BankJobTaskData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(AddTaskXifenActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddTaskXifenActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if(lists.size() == 0){
                            no_data.setVisibility(View.VISIBLE);
                            lstv.setVisibility(View.GONE);
                        }else {
                            no_data.setVisibility(View.GONE);
                            lstv.setVisibility(View.VISIBLE);
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
                        Toast.makeText(AddTaskXifenActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("pagecurrent", "1");
                params.put("pagesize", "1000");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //下一步
                Intent intent = new Intent(AddTaskXifenActivity.this, AddTaskShareActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
                finish();
            }
                break;
        }
    }
}
