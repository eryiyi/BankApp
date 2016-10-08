package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpBeanData;
import com.ruiping.BankApp.db.DBHelper;
import com.ruiping.BankApp.db.Emp;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.huanxin.ui.ChatActivity;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/9/27.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private TextView back;
    private TextView title;
    private TextView right_btn;
    private String emp_id;//该用户的id

    private ImageView cover;
    private TextView name;
    private TextView status;
    private TextView groupName;
    private TextView upEmpName;
    private TextView email;
    private TextView mobile;
    BankEmpBean emp;
    private RelativeLayout relate_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        emp_id = getIntent().getExtras().getString("emp_id");
        initView();
        //根据用户id 查询用户详情
        getMember();
    }

    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("个人资料");
        back.setOnClickListener(this);
        right_btn.setVisibility(View.GONE);
        relate_chat = (RelativeLayout) this.findViewById(R.id.relate_chat);
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        status = (TextView) this.findViewById(R.id.status);
        groupName = (TextView) this.findViewById(R.id.groupName);
        upEmpName = (TextView) this.findViewById(R.id.upEmpName);
        email = (TextView) this.findViewById(R.id.email);
        mobile = (TextView) this.findViewById(R.id.mobile);
        relate_chat.setOnClickListener(this);
        this.findViewById(R.id.relate_mobile).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.relate_chat:
            {
                if(emp.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                    //说明是自己
                }else{
                    //私聊
                    Intent chatV = new Intent(ProfileActivity.this, ChatActivity.class);
                    chatV.putExtra("userId", emp.getHx_name());
                    chatV.putExtra("userName", emp.getEmpName());
                    startActivity(chatV);
                }
            }
                break;
            case R.id.relate_mobile:
            {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile.getText().toString()));
                startActivity(intent);
            }
                break;
        }
    }

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public void getMember() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_EMP_DETAIL_BY_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    BankEmpBeanData data = getGson().fromJson(s, BankEmpBeanData.class);
                                    emp = data.getData();
                                    if(emp != null){
                                        initData();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", emp_id);
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

    void initData(){
        if(!StringUtil.isNullOrEmpty(emp.getEmpCover())){
            imageLoader.displayImage(InternetURL.INTERNAL+emp.getEmpCover(), cover, animateFirstListener);
        }

        if(!StringUtil.isNullOrEmpty(emp.getEmpName())){
            name.setText(emp.getEmpName());
        }
        if(emp.getBankGroup() != null && !StringUtil.isNullOrEmpty(emp.getBankGroup().getGroupName())){
            groupName.setText(emp.getBankGroup().getGroupName());
        }

        if(emp.getBankemp() != null && !StringUtil.isNullOrEmpty(emp.getBankemp().getEmpName())){
            upEmpName.setText(emp.getBankemp().getEmpName());
        }

        if(!StringUtil.isNullOrEmpty(emp.getEmail())){
            email.setText(emp.getEmail());
        }
        if(!StringUtil.isNullOrEmpty(emp.getEmpMobile())){
            mobile.setText(emp.getEmpMobile());
        }

        if(!StringUtil.isNullOrEmpty(emp.getIsUse())){
            switch (Integer.parseInt(emp.getIsUse())){
                case 0:
                    status.setText("在职");
                    break;
                case 1:
                    status.setText("禁用");
                    break;
                case 2:
                    status.setText("离职");
                    break;
            }
        }
        if(emp.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
            //说明是自己
            relate_chat.setVisibility(View.GONE);
        }else {
            relate_chat.setVisibility(View.VISIBLE);
        }
        Emp emp1 = new Emp(emp.getEmpId(), emp.getEmpMobile(), emp.getNewPhone(), emp.getEmpName(), emp.getEmpCover(), emp.getPushId(), emp.getDeviceId(), emp.getChannelId()) ;
        DBHelper.getInstance(ProfileActivity.this).saveEmp(emp1);
    }

}
