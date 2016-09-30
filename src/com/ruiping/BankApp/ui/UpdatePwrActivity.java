package com.ruiping.BankApp.ui;

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
public class UpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    private EditText password_one;
    private EditText password_two;
    private EditText password_three;

    private TextView title;
    private TextView right_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwr_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("修改密码");
        right_btn.setText("确认");
        right_btn.setOnClickListener(this);
        password_one = (EditText) this.findViewById(R.id.password_one);
        password_two = (EditText) this.findViewById(R.id.password_two);
        password_three = (EditText) this.findViewById(R.id.password_three);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //确认按钮
                if(StringUtil.isNullOrEmpty(password_one.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入原始密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password_two.getText().toString().trim())){
                    showMsg(UpdatePwrActivity.this, "请输入新密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password_three.getText().toString().trim())){
                    showMsg(UpdatePwrActivity.this, "请输入确认密码");
                    return;
                }

                if(!password_two.getText().toString().trim().equals(password_three.getText().toString().trim())){
                    showMsg(UpdatePwrActivity.this, "两次输入密码不一致");
                    return;
                }  progressDialog = new CustomProgressDialog(UpdatePwrActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                sendPwr();
                break;
        }
    }

    private void sendPwr() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_PWD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save(Contance.EMP_PWD, password_two.getText().toString());
                                    showMsg(UpdatePwrActivity.this, "修改密码成功！");
                                    finish();
                                }else {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdatePwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdatePwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("empPassword", password_one.getText().toString());
                params.put("newempPassword", password_two.getText().toString());
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
