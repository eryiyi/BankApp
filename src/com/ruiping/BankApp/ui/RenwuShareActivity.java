package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemSharePeopleAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankTaskShareBeanData;
import com.ruiping.BankApp.entiy.BankTaskShareBean;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.PictureGridview;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class RenwuShareActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private PictureGridview grid;
    private ItemSharePeopleAdapter adapterPerson;
    private List<BankTaskShareBean> listsPerson = new ArrayList<BankTaskShareBean>();


    private String taskId;//任务ID
    private String flag;//true是负责人或者创建人 false是他人的

    private RelativeLayout liner_one;
    private RelativeLayout liner_two;
    private RelativeLayout liner_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskId = getIntent().getExtras().getString("taskId");
        flag = getIntent().getExtras().getString("flag");
        setContentView(R.layout.renwu_share_activity);
        initView();
        //获得任务共享人员列表
        progressDialog = new CustomProgressDialog(RenwuShareActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getPersons();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("共享人");
        grid = (PictureGridview) this.findViewById(R.id.grid);
        adapterPerson = new ItemSharePeopleAdapter(listsPerson, RenwuShareActivity.this);
        grid.setAdapter(adapterPerson);
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listsPerson.size() > position) {
                    BankTaskShareBean bankTaskShareBean = listsPerson.get(position);

                    if (bankTaskShareBean != null) {
                        if ("true".equals(flag)) {
                            deleteShare(bankTaskShareBean);
                        } else {
                            if(bankTaskShareBean.getFlag() == 0){
                                Intent intent = new Intent(RenwuShareActivity.this, ProfileActivity.class);
                                intent.putExtra("emp_id", bankTaskShareBean.getEmpid());
                                startActivity(intent);
                            }
                        }

                    }
                }
            }
        });
        liner_one = (RelativeLayout) this.findViewById(R.id.liner_one);
        liner_two = (RelativeLayout) this.findViewById(R.id.liner_two);
        liner_three = (RelativeLayout) this.findViewById(R.id.liner_three);
        liner_one.setOnClickListener(this);
        liner_two.setOnClickListener(this);
        liner_three.setOnClickListener(this);
        if("true".equals(flag)){
            liner_one.setVisibility(View.VISIBLE);
            liner_two.setVisibility(View.VISIBLE);
            liner_three.setVisibility(View.VISIBLE);
        }else {
            liner_one.setVisibility(View.GONE);
            liner_two.setVisibility(View.GONE);
            liner_three.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_three:
            {
                if("true".equals(flag)){
                    //选择所有人
                    addPerson();
                }

            }
                break;
            case R.id.liner_one:
            {
                if("true".equals(flag)){
                    //选择人员
                    Intent intent= new Intent(RenwuShareActivity.this, TaskSharePersonSelectActivity.class);
                    intent.putExtra("taskId", taskId);
                    startActivityForResult(intent, 1000);
                }

            }
                break;
            case R.id.liner_two:
            {
                if("true".equals(flag)){
                    //选择部门
                    Intent intent= new Intent(RenwuShareActivity.this, TaskShareGroupsSelectActivity.class);
                    intent.putExtra("taskId", taskId);
                    startActivityForResult(intent, 1001);
                }

            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 10001){
//            BankEmpBean bankEmpBean = (BankEmpBean) data.getExtras().get("bankEmpBean");
            getPersons();
        }
        if(requestCode == 1001 && resultCode == 10001){
//            BankEmpBean bankEmpBean = (BankEmpBean) data.getExtras().get("bankEmpBean");
            getPersons();
        }
    }

    private void getPersons() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_PERSONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankTaskShareBeanData data = getGson().fromJson(s, BankTaskShareBeanData.class);
                                    listsPerson.clear();
                                    listsPerson.addAll(data.getData());
                                    adapterPerson.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    void deleteShare(final BankTaskShareBean bankTaskShareBean){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_DELETE_GROUP_SHARE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    Intent intent1 = new Intent("add_person_task_share_success");
                                    sendBroadcast(intent1);
                                    getPersons();
                                } else {
                                    Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskShareId", bankTaskShareBean.getTaskShareId());
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

    private void addPerson() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_SAVE_GROUP_SHARE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    getPersons();
                                    Intent intent1 = new Intent("add_person_task_share_success");
                                    sendBroadcast(intent1);
                                } else {
                                    Toast.makeText(RenwuShareActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuShareActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuShareActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskid", taskId);
                params.put("empid", "2");
                params.put("name", "所有人");
                params.put("flag", "2");
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
