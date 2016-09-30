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
import com.ruiping.BankApp.data.BankJobTaskCommentSingleData;
import com.ruiping.BankApp.entiy.BankJobTaskComment;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 任务评论回复
 */
public class AddTaskCommentActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private TextView title;
    private TextView right_btn;
    private String taskId;//被评论的日报ID
    private EditText content;//评论框
    private String commentId;//父评论ID，可能为null
    private String reportCommentName;//父评论会员姓名，可能为null
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_daily_comment_activity);
        taskId = getIntent().getExtras().getString("taskId");
        commentId = getIntent().getExtras().getString("commentId");
        reportCommentName = getIntent().getExtras().getString("reportCommentName");
        initView();
        if(!StringUtil.isNullOrEmpty(reportCommentName)){
            //如果是回复的父评论
            content.setHint("回复@"+reportCommentName);
        }
    }

    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);
        title.setText("添加评论");
        right_btn.setText("保存");
        right_btn.setOnClickListener(this);
        content = (EditText) this.findViewById(R.id.content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //保存
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(AddTaskCommentActivity.this, "请输入评论内容！");
                    return;
                }
                if(content.getText().toString().length() > 100){
                    showMsg(AddTaskCommentActivity.this, "评论内容200字以内！");
                    return;
                }
                progressDialog = new CustomProgressDialog(AddTaskCommentActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                addComment();
                break;
        }
    }

    private void addComment() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_REPLY_COMMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobTaskCommentSingleData data = getGson().fromJson(s, BankJobTaskCommentSingleData.class);
                                    BankJobTaskComment bankJobTaskComment = data.getData();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent("add_task_comment_success");
                                    intent1.putExtra("bankJobTaskComment", bankJobTaskComment);
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(AddTaskCommentActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskCommentActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AddTaskCommentActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("commentCont", content.getText().toString());
                if(!StringUtil.isNullOrEmpty(commentId)){
                    params.put("commentId", commentId);
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

}
