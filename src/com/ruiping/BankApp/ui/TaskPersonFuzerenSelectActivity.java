package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.ruiping.BankApp.adapter.ItemTaskPersonSelectAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.TaskManagerData;
import com.ruiping.BankApp.entiy.BankEmpBean;
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
 * 任务负责人选择
 */
public class TaskPersonFuzerenSelectActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView right_btn;
    private String taskId;//主任务id
    private TextView no_data;//暂无数据
    private ListView lstv;//信息列表
    private ItemTaskPersonSelectAdapter adapter;
    private List<BankEmpBean> lists = new ArrayList<BankEmpBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_person_activity);
        taskId = getIntent().getExtras().getString("taskId");
        initView();
        progressDialog = new CustomProgressDialog(TaskPersonFuzerenSelectActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }


    private void initView() {
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        no_data = (TextView) this.findViewById(R.id.no_data);
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择负责人");
        right_btn.setVisibility(View.GONE);
        adapter = new ItemTaskPersonSelectAdapter(lists, TaskPersonFuzerenSelectActivity.this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankEmpBean  bankJobTaskEmp= lists.get(position);
                if(bankJobTaskEmp != null){
                    addPerson(bankJobTaskEmp);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 10001){
            getData();
        }
    }

    //查询参与人列表
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TASK_PERSON_ALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    TaskManagerData data = getGson().fromJson(s, TaskManagerData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(TaskPersonFuzerenSelectActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(TaskPersonFuzerenSelectActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TaskPersonFuzerenSelectActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
//                params.put("taskId", taskId);
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


    private void addPerson(final BankEmpBean bankJobTaskEmp) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_MANAGER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    //调用广播，刷新
                                    Intent intent1 = new Intent("update_task_fuzeren_success");
                                    intent1.putExtra("bankJobTaskEmp", bankJobTaskEmp);
                                    sendBroadcast(intent1);

                                    Intent intent = new Intent();
                                    intent.putExtra("bankJobTaskEmp", bankJobTaskEmp);
                                    setResult(10002, intent);
                                    finish();
                                } else {
                                    Toast.makeText(TaskPersonFuzerenSelectActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(TaskPersonFuzerenSelectActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TaskPersonFuzerenSelectActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empIdF", bankJobTaskEmp.getEmpId());
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
