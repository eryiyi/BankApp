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
public class UpdateMobileActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile_one_edit;
    private EditText mobile_two_edit;

    private TextView title;
    private TextView right_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_mobile_activity);
        initView();
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class))){
            mobile_one_edit.setText(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
        }
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("修改手机号");
        right_btn.setText("确认");
        right_btn.setOnClickListener(this);
        mobile_two_edit = (EditText) this.findViewById(R.id.mobile_two_edit);
        mobile_one_edit = (EditText) this.findViewById(R.id.mobile_one_edit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //确认按钮
                if(StringUtil.isNullOrEmpty(mobile_one_edit.getText().toString())){
                    showMsg(UpdateMobileActivity.this, "请输入原手机号");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mobile_two_edit.getText().toString())){
                    showMsg(UpdateMobileActivity.this, "请输入新手机号");
                    return;
                }
                if(mobile_one_edit.getText().toString().length() != 11){
                    showMsg(UpdateMobileActivity.this, "请输入正确的手机号格式");
                    return;
                }
                if(mobile_two_edit.getText().toString().length() != 11){
                    showMsg(UpdateMobileActivity.this, "请输入正确的手机号格式");
                    return;
                }
                progressDialog = new CustomProgressDialog(UpdateMobileActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                sendMobile();
                break;
        }
    }

    //TODO
    //修改手机号
    private void sendMobile() {
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
                                    save(Contance.EMP_MOBILE , mobile_two_edit.getText().toString());
                                    //调用广播
                                    Intent intent1 = new Intent("update_mobile_success");
                                    sendBroadcast(intent1);
                                    showMsg(UpdateMobileActivity.this, "更新手机号成功！");
                                    finish();
                                }else {
                                    Toast.makeText(UpdateMobileActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdateMobileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateMobileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("empMobile", mobile_one_edit.getText().toString());
                params.put("newPhone", mobile_two_edit.getText().toString());
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
