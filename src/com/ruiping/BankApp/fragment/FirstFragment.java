package com.ruiping.BankApp.fragment;

import android.content.Intent;
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

    private IndexCountObj indexCountObj ;//任务统计对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        initView();
        //获取任务统计
        getMineCount();
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

        task_mine_number = (TextView) view.findViewById(R.id.task_mine_number);
        task_mine_number_do = (TextView) view.findViewById(R.id.task_mine_number_do);
        task_mine_number_finish = (TextView) view.findViewById(R.id.task_mine_number_finish);
        task_mine_number_all = (TextView) view.findViewById(R.id.task_mine_number_all);
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
        }
    }
}
