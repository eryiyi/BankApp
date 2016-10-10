package com.ruiping.BankApp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.ruiping.BankApp.adapter.ItemRenwuAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobTaskData;
import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.library.PullToRefreshBase;
import com.ruiping.BankApp.library.PullToRefreshListView;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 */
public class RenwuListActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView back;
    private TextView right_btn;
    private TextView no_data;
    private PullToRefreshListView lstv;
    private ItemRenwuAdapter adapter;
    private List<BankJobTask> lists = new ArrayList<BankJobTask>();
    private String type;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private String urlInternet = "";//接口路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.renwu_list_activity);
        type = getIntent().getExtras().getString("type");
        initView();
        progressDialog = new CustomProgressDialog(RenwuListActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        no_data = (TextView) this.findViewById(R.id.no_data);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("添加");
        back.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        switch (Integer.parseInt(type)){
            case 1:
                title.setText("我的任务");
                urlInternet = InternetURL.GET_TASK_BY_EMP_ID_URL;
                break;
            case 2:
                title.setText("创建的任务");
                urlInternet = InternetURL.GET_TASK_CREATE_BY_EMP_ID_URL;
                break;
            case 3:
                title.setText("未完成的任务");
                urlInternet = InternetURL.GET_TASK_UNFINISH_BY_EMP_ID_URL;
                break;
            case 4:
                title.setText("全部任务");
                urlInternet = InternetURL.GET_TASK_ALL_BY_EMP_ID_URL;
                break;
            case 5:
                title.setText("共享任务");
                urlInternet = InternetURL.GET_BANK_TASK_SHARE_URL;
                break;
        }

        adapter = new ItemRenwuAdapter(lists, RenwuListActivity.this);
        lstv.setAdapter(adapter);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        no_data.setVisibility(View.GONE);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size() > (position-1)){
                    BankJobTask bankJobTask = lists.get(position-1);
                    if(bankJobTask != null){
                        Intent intent = new Intent(RenwuListActivity.this, RenwuDetailActivity.class);
                        intent.putExtra("taskId", bankJobTask.getTaskId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加任务
            {
                Intent renwuAdd = new Intent(RenwuListActivity.this, AddTaskTitleActivity.class);
                startActivity(renwuAdd);
            }
                break;
        }
    }

    //查询任务列表
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                urlInternet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobTaskData data = getGson().fromJson(s, BankJobTaskData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(RenwuListActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuListActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("pagecurrent", String.valueOf(pageIndex));
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if (action.equals("update_task_fuzeren_success")) {
//                //负责人更新
//                BankJobTaskEmp bankJobTaskEmp = (BankJobTaskEmp) intent.getExtras().get("bankJobTaskEmp");
//                for(int i=0;i<lists.size();i++){
//                    BankJobTask bankJobTask  = lists.get(i);
//                    if(bankJobTask.getTaskId().equals(bankJobTaskEmp.getBankJobTask().getTaskId())){
//                        lists.get(i).setBankEmp(bankJobTaskEmp.getBankEmp());
//                        lists.get(i).setEmpName(bankJobTaskEmp.getBankEmp().getEmpName());
//                        break;
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
//        myIntentFilter.addAction("update_task_fuzeren_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
