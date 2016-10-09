package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.ruiping.BankApp.data.BankNoticesSingleData;
import com.ruiping.BankApp.entiy.BankNoticesBean;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 通知详情
 */
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView notice_title;
    private TextView content;
    private TextView dateline;
    private TextView attach;
    private BankNoticesBean bankNoticesBean;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_activity);
//        bankNoticesBean = (BankNoticesBean) getIntent().getExtras().get("bankNoticesBean");
        id = getIntent().getExtras().getString("id");
        initView();
        //获取公告详情
        getNoticeDetail();
    }

    private void getNoticeDetail() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NOTICE_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankNoticesSingleData data = getGson().fromJson(s, BankNoticesSingleData.class);
                                    bankNoticesBean = data.getData();
                                    initData();
                                } else {
                                    Toast.makeText(NoticeDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(NoticeDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NoticeDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("noticeId", id);
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
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

   //实例化
    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        notice_title = (TextView) this.findViewById(R.id.notice_title);
        content = (TextView) this.findViewById(R.id.content);
        dateline = (TextView) this.findViewById(R.id.dateline);
        attach = (TextView) this.findViewById(R.id.attach);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("公告详情");
        attach.setOnClickListener(this);
    }

    //赋值
    private void initData() {
        if(bankNoticesBean  != null){
            notice_title.setText(bankNoticesBean.getTitle());
            content.setText(bankNoticesBean.getContent());
            dateline.setText(DateUtil.getDate(bankNoticesBean.getReleaseTime(), "yyyy-MM-dd HH:mm"));
            if(!StringUtil.isNullOrEmpty(bankNoticesBean.getCommentFile())){
                attach.setVisibility(View.VISIBLE);
            }else {
                attach.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.attach:
            {
                //附件点击
                Intent intent  = new Intent(NoticeDetailActivity.this, AttachMentNoticeActivity.class);
                intent.putExtra("attach_file", (bankNoticesBean.getCommentFile()==null?"":bankNoticesBean.getCommentFile()));//附件列表
                startActivity(intent);
            }
                break;
        }
    }
}
