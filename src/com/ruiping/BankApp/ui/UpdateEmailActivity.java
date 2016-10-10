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
 * Created by zhl on 2016/7/1.
 */
public class UpdateEmailActivity extends BaseActivity implements View.OnClickListener {
    private EditText email;

    private TextView title;
    private TextView right_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_activity);
        initView();
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class))){
            email.setText(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class));
        }
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("修改邮箱");
        right_btn.setText("确认");
        right_btn.setOnClickListener(this);
        email = (EditText) this.findViewById(R.id.email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //确认按钮
                if(StringUtil.isNullOrEmpty(email.getText().toString())){
                    showMsg(UpdateEmailActivity.this, "请输入邮箱");
                    return;
                }
                if(!StringUtil.emailFormat(email.getText().toString())){
                    showMsg(UpdateEmailActivity.this, "请输入正确的邮箱格式");
                    return;
                }
                progressDialog = new CustomProgressDialog(UpdateEmailActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                sendEmail();
                break;
        }
    }

    //修改邮箱
    private void sendEmail() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save(Contance.EMP_EMAIL , email.getText().toString());
                                    //调用广播
                                    Intent intent1 = new Intent("update_email_success");
                                    sendBroadcast(intent1);
                                    showMsg(UpdateEmailActivity.this, "更新邮箱成功！");
                                    finish();
                                }else {
                                    Toast.makeText(UpdateEmailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdateEmailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateEmailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("email", email.getText().toString());
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
