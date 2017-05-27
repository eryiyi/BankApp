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
import com.ruiping.BankApp.adapter.ItemTaskShareAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.RoleObjData;
import com.ruiping.BankApp.entiy.RoleObj;
import com.ruiping.BankApp.util.Contance;
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
 * 新建任务  分享
 */
public class AddTaskShareActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,OnClickContentItemListener {
    private TextView title;
    private TextView right_btn;

    private String taskId;//任务ID

    private TextView no_data;
    private ListView lstv;
    private ItemTaskShareAdapter adapter;
    private List<RoleObj> lists = new ArrayList<RoleObj>();

    private TextView btn_select_number;

    private List<String> listEmpSelect = new ArrayList<String>();//存放选中的角色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_4_activity);
        taskId = getIntent().getExtras().getString("taskId");

        initView();
        progressDialog = new CustomProgressDialog(AddTaskShareActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();

    }

    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

    void initView(){
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        no_data = (TextView) this.findViewById(R.id.no_data);
        title.setText("选择通知有关人员");
        right_btn.setText("完成");
        right_btn.setOnClickListener(this);

        adapter = new ItemTaskShareAdapter(AddTaskShareActivity.this, lists);
        adapter.setOnClickContentItemListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(this);

        btn_select_number = (TextView) this.findViewById(R.id.btn_select_number);

        mapDone();
    }

    void mapDone(){
        listEmpSelect.clear();//先清空

        for (Map.Entry<Integer, Boolean> entry : adapter.isCheckMap.entrySet()) {
            Integer key = entry.getKey();
            Boolean value = entry.getValue();
            System.out.println("key=" + key + " value=" + value);
            if(value){
                //如果选中了
                listEmpSelect.add(lists.get(key).getId());
            }
        }

        String dataName = getResources().getString(R.string.add_task_share_txt);
        dataName = String.format(dataName, String.valueOf(listEmpSelect.size()));
        btn_select_number.setText(dataName);
    }

    /**
     * 当ListView 子项点击的时候
     */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout,
                            int position, long id) {
        if (itemLayout.getTag() instanceof ItemTaskShareAdapter.ViewHolder) {
            ItemTaskShareAdapter.ViewHolder holder = (ItemTaskShareAdapter.ViewHolder) itemLayout.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
            mapDone();
        }
    }

    //查询
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskFindAllRole,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    RoleObjData data = getGson().fromJson(s, RoleObjData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(AddTaskShareActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddTaskShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskShareActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.GROUP_ID, ""), String.class))){
                    params.put("groupId", getGson().fromJson(getSp().getString(Contance.GROUP_ID, ""), String.class));
                }else{
                    params.put("groupId", "");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                String strEmps = "";
                if(listEmpSelect.size()>0){
                    for(String str:listEmpSelect){
                        strEmps = strEmps + str +",";
                    }
                }
                if(!StringUtil.isNullOrEmpty(strEmps)){
                    updateEmpTask(strEmps);
                }else {
                    Intent intent = new Intent(AddTaskShareActivity.this, RenwuDetailActivity.class);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                    finish();
                }

            }
                break;
        }
    }


    //更新分享角色
    private void updateEmpTask(final String strEmps) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskSaveRoleShare,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    showMsg(AddTaskShareActivity.this, "新建任务成功！");
                                    Intent intent1 = new Intent("update_renwu_number");
                                    sendBroadcast(intent1);

                                    Intent intent = new Intent(AddTaskShareActivity.this, RenwuDetailActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    showMsg(AddTaskShareActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskShareActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskShareActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskid", taskId);
                if(!StringUtil.isNullOrEmpty(strEmps)){
                    params.put("shareRole", strEmps);
                }else{
                    params.put("shareRole", "");
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

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        mapDone();
    }

}
