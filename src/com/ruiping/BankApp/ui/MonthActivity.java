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
import com.ruiping.BankApp.data.IndexCountObjData;
import com.ruiping.BankApp.entiy.IndexCountObj;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 月报目录
 */
public class MonthActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private TextView daily_count;
    private TextView xiashuCount;
    private TextView comment_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_activity);
        initView();

        progressDialog = new CustomProgressDialog(MonthActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //查询统计数据
        getMineCount();
    }

    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title = (TextView) this.findViewById(R.id.title);
        back.setOnClickListener(this);
        title.setText("月报目录");
        right_btn.setText("添加");
        right_btn.setOnClickListener(this);
        this.findViewById(R.id.liner_one).setOnClickListener(this);
        this.findViewById(R.id.liner_two).setOnClickListener(this);
        this.findViewById(R.id.liner_three).setOnClickListener(this);

        daily_count = (TextView) this.findViewById(R.id.daily_count);
        xiashuCount = (TextView) this.findViewById(R.id.xiashuCount);
        comment_count = (TextView) this.findViewById(R.id.comment_count);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加
            {
                Intent intent = new Intent(MonthActivity.this, AddMineMonthActivtiy.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_one:
            {
                //我的月报
//                getWeekDate();
                Intent intent = new Intent(MonthActivity.this, MoonthlyListActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_two:
            {
                //下属月报
                Intent intent = new Intent(MonthActivity.this, MoonthXiashuActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_three:
            {
                //评论我的月报
                Intent intent = new Intent(MonthActivity.this, MoonthCommentActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
            }
                break;
        }
    }

    IndexCountObj indexCountObj;
    //查询评论我的日报数
    private void getMineCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_REPORT_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            IndexCountObjData data = getGson().fromJson(s, IndexCountObjData.class);
                            if (data.getCode() == 200) {
                                indexCountObj = data.getData();
                                initData();
                            } else {
                                Toast.makeText(MonthActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MonthActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MonthActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportType", "3");
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
        //填充数据
        daily_count.setText(String.valueOf(indexCountObj.getKey1()));
        xiashuCount.setText(String.valueOf(indexCountObj.getKey2()));
        comment_count.setText(String.valueOf(indexCountObj.getKey3()));
    }
}
