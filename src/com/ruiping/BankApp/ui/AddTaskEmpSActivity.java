package com.ruiping.BankApp.ui;

import android.app.Dialog;
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
import com.ruiping.BankApp.adapter.ItemTaskEmpsAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.TaskManagerData;
import com.ruiping.BankApp.entiy.BankEmpBean;
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
 * 新建任务 细分
 */
public class AddTaskEmpSActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,OnClickContentItemListener {
    private TextView title;
    private TextView right_btn;

    private String taskId;//任务ID
    private String taskTitle;//任务标题
    private String dateLine;
    private String dateLineEnd;

    private TextView no_data;
    private ListView lstv;
    private ItemTaskEmpsAdapter adapter;
    private List<BankEmpBean> lists = new ArrayList<BankEmpBean>();

    private TextView btn_select_number;

    private List<String> listEmpSelect = new ArrayList<String>();//存放选中的会员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_3_activity);
        taskId = getIntent().getExtras().getString("taskId");
        taskTitle = getIntent().getExtras().getString("taskTitle");
        dateLine = getIntent().getExtras().getString("dateLine");
        dateLineEnd = getIntent().getExtras().getString("dateLineEnd");

        initView();
        progressDialog = new CustomProgressDialog(AddTaskEmpSActivity.this, "正在加载中",R.anim.custom_dialog_frame);
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
        title.setText("选择参与人");
        right_btn.setText("下一步");
        right_btn.setOnClickListener(this);

        adapter = new ItemTaskEmpsAdapter(AddTaskEmpSActivity.this, lists);
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
                listEmpSelect.add(lists.get(key).getEmpId());
            }
        }

        String dataName = getResources().getString(R.string.add_task_emp_s_txt);
        dataName = String.format(dataName, String.valueOf(listEmpSelect.size()));
        btn_select_number.setText(dataName);
    }

    /**
     * 当ListView 子项点击的时候
     */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout,
                            int position, long id) {
        if (itemLayout.getTag() instanceof ItemTaskEmpsAdapter.ViewHolder) {
            ItemTaskEmpsAdapter.ViewHolder holder = (ItemTaskEmpsAdapter.ViewHolder) itemLayout.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
            mapDone();
        }
    }

    //查询
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
                                    Toast.makeText(AddTaskEmpSActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddTaskEmpSActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskEmpSActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                if(listEmpSelect.size() == 0){
                    showMsg(AddTaskEmpSActivity.this, "请先选择参与人！");
                    return;
                }
                String strEmps = "";
                for(String str:listEmpSelect){
                    strEmps = strEmps + str +",";
                }
                //保存参与人
                updateEmpTask(strEmps);
            }
                break;
        }
    }

    //更新参与人
    private void updateEmpTask(final String strEmps) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appBankJobTaskSaveSub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
//                                    showDialog();
                                    //需要细分任务
                                    Intent intent = new Intent(AddTaskEmpSActivity.this, AddTaskXifenActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    startActivity(intent);
                                }else {
                                    showMsg(AddTaskEmpSActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddTaskEmpSActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddTaskEmpSActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", taskId);
                if(!StringUtil.isNullOrEmpty(dateLine)){
                    params.put("dateLine", dateLine);
                }
                if(!StringUtil.isNullOrEmpty(dateLineEnd)){
                    params.put("dateLineEnd", dateLineEnd);
                }
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("joinEmpId", strEmps);
                params.put("taskTitle", taskTitle);
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

    private void showDialog() {
        final Dialog picAddDialog = new Dialog(AddTaskEmpSActivity.this, R.style.MyAlertDialog);
        View picAddInflate = View.inflate(this, R.layout.dialog_task_xifen, null);

        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要细分任务
                Intent intent = new Intent(AddTaskEmpSActivity.this, AddTaskXifenActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);

                picAddDialog.dismiss();
                finish();
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不需要细分任务，直接选择分享人
                Intent intent = new Intent(AddTaskEmpSActivity.this, AddTaskShareActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);

                picAddDialog.dismiss();
                finish();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }
}
