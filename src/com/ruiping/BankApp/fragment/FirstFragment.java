package com.ruiping.BankApp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseFragment;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.IndexCountObjData;
import com.ruiping.BankApp.entiy.IndexCountObj;
import com.ruiping.BankApp.ui.AddTaskTitleActivity;
import com.ruiping.BankApp.ui.RenwuListActivity;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class FirstFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private Resources res;

    private TextView title;
    private TextView right_btn;
    private TextView back;

    private TextView task_mine_number;
    private TextView task_mine_number_do;
    private TextView task_mine_number_finish;
    private TextView task_mine_number_all;
    private TextView task_mine_number_share;

    private IndexCountObj indexCountObj ;//任务统计对象

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        registerBoradcastReceiver();
        res = getActivity().getResources();
        initView();
        //获取任务统计
        getMineCount();
        changeColorOrSize();
        return view;
    }

    private void initView() {
        title = (TextView) view.findViewById(R.id.title);
        right_btn = (TextView) view.findViewById(R.id.right_btn);
        back = (TextView) view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        right_btn.setOnClickListener(this);
        right_btn.setText("添加");
        title.setText("任务目录");

        view.findViewById(R.id.liner_one).setOnClickListener(this);
        view.findViewById(R.id.liner_two).setOnClickListener(this);
        view.findViewById(R.id.liner_three).setOnClickListener(this);
        view.findViewById(R.id.liner_four).setOnClickListener(this);
        view.findViewById(R.id.liner_share).setOnClickListener(this);

        task_mine_number = (TextView) view.findViewById(R.id.task_mine_number);
        task_mine_number_do = (TextView) view.findViewById(R.id.task_mine_number_do);
        task_mine_number_finish = (TextView) view.findViewById(R.id.task_mine_number_finish);
        task_mine_number_all = (TextView) view.findViewById(R.id.task_mine_number_all);
        task_mine_number_share = (TextView) view.findViewById(R.id.task_mine_number_share);

        txt1 = (TextView) view.findViewById(R.id.txt1);
        txt2 = (TextView) view.findViewById(R.id.txt2);
        txt3 = (TextView) view.findViewById(R.id.txt3);
        txt4 = (TextView) view.findViewById(R.id.txt4);
        txt5 = (TextView) view.findViewById(R.id.txt5);

    }

    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            txt1.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt2.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt3.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt4.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt5.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));

            task_mine_number.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            task_mine_number_do.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            task_mine_number_finish.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            task_mine_number_all.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            task_mine_number_share.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_btn:
                //添加
            {
                Intent renwuAdd = new Intent(getActivity(), AddTaskTitleActivity.class);
                startActivity(renwuAdd);
            }
                break;
            case R.id.liner_one:
                //我的任务
            {
                Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
                break;
            case R.id.liner_two:
                //创建的任务
            {
                Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
                break;
            case R.id.liner_three:
                //已完成的任务
            {
                Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
            }
                break;
            case R.id.liner_four:
                //全部任务
            {
                Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
            }
                break;
            case R.id.liner_share:
            {
                //共享
                Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
            }
                break;
        }
    }

    //获得任务统计
    private void getMineCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_RENWU_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    IndexCountObjData data = getGson().fromJson(s, IndexCountObjData.class);
                                    indexCountObj = data.getData();
                                    initData();
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
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

    void initData(){
        if(indexCountObj != null){
            task_mine_number.setText(indexCountObj.getKey1()==null?"0":indexCountObj.getKey1());
            task_mine_number_do.setText(indexCountObj.getKey4()==null?"0":indexCountObj.getKey4());
            task_mine_number_finish.setText(indexCountObj.getKey2()==null?"0":indexCountObj.getKey2());
            task_mine_number_all.setText(indexCountObj.getKey5()==null?"0":indexCountObj.getKey5());
            task_mine_number_share.setText(indexCountObj.getKey6()==null?"0":indexCountObj.getKey6());
        }
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_renwu_number")) {
                getMineCount();
            }
            if (action.equals("change_color_size")) {
                changeColorOrSize();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_renwu_number");
        myIntentFilter.addAction("change_color_size");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
