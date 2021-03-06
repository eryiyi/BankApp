package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 新建任务  标题录入
 */
public class AddTaskTitleChildActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText content;//内容
    private String taskId;//任务ID
    private String pid;//主任务ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_title_activity);
        pid = getIntent().getExtras().getString("pid");

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("新建子任务");
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        content = (EditText) this.findViewById(R.id.content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void saveTaskTitleAction(View view){
        if(StringUtil.isNullOrEmpty(content.getText().toString())){
            showMsg(AddTaskTitleChildActivity.this, "请输入标题！");
            return;
        }
        progressDialog = new CustomProgressDialog(AddTaskTitleChildActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        addTask();
    }

    //保存任务标题
    private void addTask() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_TASK_TITLE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    taskId = jo.getString("data");
                                    Intent detailIntent = new Intent(AddTaskTitleChildActivity.this, RenwuChildDetailActivity.class);
                                    detailIntent.putExtra("taskId", taskId);
                                    startActivity(detailIntent);
                                }else {
                                    showMsg(AddTaskTitleChildActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskTitleChildActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskTitleChildActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("taskTitle", content.getText().toString());
                params.put("pid", pid);
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
