package com.ruiping.BankApp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.adapter.ItemCommentAdapter;
import com.ruiping.BankApp.adapter.ItemDoneAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportCommentBeanData;
import com.ruiping.BankApp.data.BankJobReportDoneBeanData;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.entiy.BankJobReportCommentBean;
import com.ruiping.BankApp.entiy.BankJobReportDoneBean;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.HttpUtils;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.ContentListView;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 季报详情
 */
public class QuarterDetailActivtiy extends BaseActivity implements View.OnClickListener , ContentListView.OnRefreshListener, ContentListView.OnLoadListener{
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private TextView add_comment;//添加评论

    private ContentListView lstv;//评论集合
    private LinearLayout headLiner;
    private ItemCommentAdapter adapter;
    private List<BankJobReportCommentBean> lists = new ArrayList<BankJobReportCommentBean>();//评论数据列表
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ListView lstvT;//查阅集合
    private ItemDoneAdapter adapterT;//查阅适配器
    private static boolean IS_REFRESH_T = true;
    private int pageIndex_T = 1;
    private List<BankJobReportDoneBean> listsDone = new ArrayList<BankJobReportDoneBean>();//查阅数据列表

    private View view_one;
    private View view_two;

    private RelativeLayout relate_one;
    private RelativeLayout relate_two;

    private int tmpSelect = 0;
    private TextView btn_attachment;//附件查看

    private BankJobReport bankJobReport;

    private ImageView head;
    private TextView nickname;
    private TextView dateline;
    private TextView week;
    private TextView content_one;
    private TextView content_two;
    private TextView content_three;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private String reportFile = "";

    private Boolean flag = false;//true 当前登录用户自己的日报或周报   false  看别人的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankJobReport = (BankJobReport) getIntent().getExtras().get("bankJobReport");
        if(bankJobReport != null){
            if(bankJobReport.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))) {
                flag = true;
            }else{
                flag = false;
            }
        }
        setContentView(R.layout.weekly_detail_activity);
        registerBoradcastReceiver();
        initView();
        if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
            progressDialog = new CustomProgressDialog(QuarterDetailActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            initData();
            getDone();
        }
        initHeader();
//        getDetailById();
    }

    //填充季报数据
    void initHeader(){
        if(bankJobReport != null && bankJobReport.getBankEmp() != null){
            if(!StringUtil.isNullOrEmpty(bankJobReport.getBankEmp().getEmpCover())){
                imageLoader.displayImage(InternetURL.INTERNAL+bankJobReport.getBankEmp().getEmpCover(), head, animateFirstListener);
            }
            if(!StringUtil.isNullOrEmpty(bankJobReport.getBankEmp().getEmpName())){
                nickname.setText(bankJobReport.getBankEmp().getEmpName());
            }
            if(!StringUtil.isNullOrEmpty(bankJobReport.getDateLine())){
                dateline.setText(bankJobReport.getReportNumber());
            }
            if(!StringUtil.isNullOrEmpty(bankJobReport.getDateStartEnd())){
                week.setText(bankJobReport.getDateStartEnd());
            }
            content_one.setText(bankJobReport.getReportTitle()==null?"":bankJobReport.getReportTitle());
            content_two.setText(bankJobReport.getReportCont()==null?"":bankJobReport.getReportCont());
            content_three.setText(bankJobReport.getReportNext()==null?"":bankJobReport.getReportNext());
        }
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);

        title.setText("季报详情");
        if(flag){
            right_btn.setText("保存");
            right_btn.setOnClickListener(this);
        }else {
            right_btn.setVisibility(View.GONE);
        }

        relate_one = (RelativeLayout) this.findViewById(R.id.relate_one);
        relate_two = (RelativeLayout) this.findViewById(R.id.relate_two);
        relate_one.setVisibility(View.VISIBLE);
        relate_two.setVisibility(View.GONE);
        tmpSelect = 0;

        lstv = (ContentListView) this.findViewById(R.id.lstv);
        lstvT = (ListView) this.findViewById(R.id.lstv2);
        headLiner = (LinearLayout) LayoutInflater.from(QuarterDetailActivtiy.this).inflate(R.layout.moonth_detail_header, null);
        btn_attachment = (TextView) headLiner.findViewById(R.id.btn_attachment);//附件查看
        btn_attachment.setOnClickListener(this);

        head = (ImageView) headLiner.findViewById(R.id.head);//头像
        nickname = (TextView) headLiner.findViewById(R.id.nickname);//周报标题
        dateline = (TextView) headLiner.findViewById(R.id.dateline);//时间
        week = (TextView) headLiner.findViewById(R.id.week);//所属周
        content_one = (TextView) headLiner.findViewById(R.id.content_one);//工作成效
        content_two = (TextView) headLiner.findViewById(R.id.content_two);//工作总结
        content_three = (TextView) headLiner.findViewById(R.id.content_three);//工作计划
        add_comment = (TextView) headLiner.findViewById(R.id.add_comment);

        content_one.setOnClickListener(this);
        content_two.setOnClickListener(this);
        content_three.setOnClickListener(this);

        add_comment.setOnClickListener(this);

        view_one = headLiner.findViewById(R.id.view_one);
        view_two = headLiner.findViewById(R.id.view_two);
        headLiner.findViewById(R.id.btn_brower).setOnClickListener(this);//评论点击
        headLiner.findViewById(R.id.btn_comment).setOnClickListener(this);//查阅点击

        adapter = new ItemCommentAdapter(lists, QuarterDetailActivtiy.this);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.addHeaderView(headLiner);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position-2 < lists.size()){
                    BankJobReportCommentBean bankJobReportCommentBean = lists.get(position-2);
                    Intent intent = new Intent(QuarterDetailActivtiy.this, AddDailyCommentActivity.class);
                    intent.putExtra("reportId", bankJobReport.getReportId());
                    intent.putExtra("reportCommentId", bankJobReportCommentBean.getReportCommentId());//父评论ID
                    intent.putExtra("reportCommentName", bankJobReportCommentBean.getBankEmp().getEmpName());//父评论ID
                    startActivity(intent);
                }
            }
        });

        adapterT = new ItemDoneAdapter(listsDone, QuarterDetailActivtiy.this);
        lstvT.addHeaderView(headLiner);
        lstvT.setAdapter(adapterT);
        lstvT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if(position-2 < lists.size()){
//                    BankJobReportDoneBean bankJobReportCommentBean = listsDone.get(position-2);
//                }
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
            {
                //提交
                if(StringUtil.isNullOrEmpty(content_one.getText().toString())){
                    showMsg(QuarterDetailActivtiy.this, "请输入工作成效！");
                    return;
                }
//                if(StringUtil.isNullOrEmpty(content_two.getText().toString())){
//                    showMsg(QuarterDetailActivtiy.this, "请输入工作总结！");
//                    return;
//                }
//                if(StringUtil.isNullOrEmpty(content_three.getText().toString())){
//                    showMsg(QuarterDetailActivtiy.this, "请输入工作计划！");
//                    return;
//                }
                if(bankJobReport !=null){
                    progressDialog = new CustomProgressDialog(QuarterDetailActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    updateWeekly();
                }
            }
                break;
            case R.id.btn_comment:
                view_one.setBackgroundColor(getResources().getColor(R.color.blue_btn_normal));
                view_two.setBackgroundColor(getResources().getColor(R.color.white));
                relate_one.setVisibility(View.VISIBLE);
                relate_two.setVisibility(View.GONE);
                tmpSelect = 0;
                break;
            case R.id.btn_brower:
                view_one.setBackgroundColor(getResources().getColor(R.color.white));
                view_two.setBackgroundColor(getResources().getColor(R.color.blue_btn_normal));
                relate_one.setVisibility(View.GONE);
                relate_two.setVisibility(View.VISIBLE);
                tmpSelect = 1;
                break;
            case R.id.add_comment:
                //添加评论
            {
                if(bankJobReport != null){
                    Intent intent = new Intent(QuarterDetailActivtiy.this, AddDailyCommentActivity.class);
                    intent.putExtra("reportId", bankJobReport.getReportId());
                    intent.putExtra("reportCommentId", "");
                    intent.putExtra("reportCommentName", "");
                    startActivity(intent);
                }else{
                    showMsg(QuarterDetailActivtiy.this, "请先添加季报！");
                    return;
                }
            }
                break;
            case R.id.btn_attachment:
            {
                if(bankJobReport != null){
                    //附件查看
                    Intent intent  = new Intent(QuarterDetailActivtiy.this, AttachMentActivity.class);
                    intent.putExtra("attach_file", (bankJobReport.getReportFile()==null?"":bankJobReport.getReportFile()));//附件
                    intent.putExtra("reportId", bankJobReport.getReportId());//报表id
                    intent.putExtra("empId", bankJobReport.getEmpId());//用户ID
                    startActivity(intent);
                }else{
                    Intent intent  = new Intent(QuarterDetailActivtiy.this, AttachMentUpWeekActivity.class);
                    startActivityForResult(intent, 1003);
                }
            }
                break;
            case R.id.content_one:
            {
                if(flag){
                    Intent intent = new Intent(QuarterDetailActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "1");
                    intent.putExtra("typeWrite_content", content_one.getText().toString());
                    startActivityForResult(intent, 1000);
                }
            }
                break;
            case R.id.content_two:
            {
                if(flag){
                    Intent intent = new Intent(QuarterDetailActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "2");
                    intent.putExtra("typeWrite_content", content_two.getText().toString());
                    startActivityForResult(intent, 1001);
                }
            }
                break;
            case R.id.content_three:
            {
                if(flag){
                    Intent intent = new Intent(QuarterDetailActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "3");
                    intent.putExtra("typeWrite_content", content_three.getText().toString());
                    startActivityForResult(intent, 1002);
                }
            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                {
                    String xStr = data.getExtras().getString("xontent");
                    content_one.setText(xStr);
                }
                break;
                case 1001:
                {
                    String xStr = data.getExtras().getString("xontent");
                    content_two.setText(xStr);
                }
                break;
                case 1002:
                {
                    String xStr = data.getExtras().getString("xontent");
                    content_three.setText(xStr);
                }
                break;

                case 1003:
                {
                    reportFile = data.getExtras().getString("attach_file");
                }
                    break;
            }
        }

    }

    @Override
    public void onLoad() {
        if(tmpSelect == 0){
            //评论
            IS_REFRESH = false;
            pageIndex++;
            if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
                initData();
            }else{
                lstv.onRefreshComplete();
                lstv.onLoadComplete();
            }
            lstv.onRefreshComplete();
            lstv.onLoadComplete();
            lstv.setResultSize(0);
        }
        if(tmpSelect == 1){
            //查阅
            pageIndex_T++;
            IS_REFRESH_T = false;
            if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
                getDone();
            }
        }
    }

    @Override
    public void onRefresh() {
        if(tmpSelect == 0){
            IS_REFRESH = true;
            pageIndex = 1;
            if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())){
                initData();
            }else{
                lstv.onRefreshComplete();
                lstv.onLoadComplete();
            }
            lstv.onRefreshComplete();
            lstv.onLoadComplete();
            lstv.setResultSize(0);
        }
        if(tmpSelect == 1){
            //查阅
            pageIndex_T = 1;
            IS_REFRESH_T = true;
            if(bankJobReport != null && !StringUtil.isNullOrEmpty(bankJobReport.getReportId())) {
                getDone();
            }
        }
    }

    //评论列表
    private void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_COMMENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankJobReportCommentBeanData data = getGson().fromJson(s, BankJobReportCommentBeanData.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH) {
                                    lists.clear();
                                    lstv.onRefreshComplete();
                                    lists.addAll(data.getData());
                                    lstv.setResultSize(data.getData().size());
                                } else {
                                    lstv.onLoadComplete();
                                    lists.addAll(data.getData());
                                    lstv.setResultSize(data.getData().size());
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", bankJobReport.getReportId());
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

    //获得查阅列表
    private void getDone() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_DONE_LISTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankJobReportDoneBeanData data = getGson().fromJson(s, BankJobReportDoneBeanData.class);
                            if (data.getCode() == 200) {
                                listsDone.clear();
                                listsDone.addAll(data.getData());
                                adapterT.notifyDataSetChanged();
                            } else {
                                Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", bankJobReport.getReportId());
                params.put("pagecurrent", String.valueOf(pageIndex_T));
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

    private void updateWeekly() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_UPDATE_EMP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    bankJobReport = data.getData();
                                    showMsg(QuarterDetailActivtiy.this, "操作成功");
                                    Intent intent1 = new Intent("add_quarter_success");
                                    sendBroadcast(intent1);
                                }else {
                                    showMsg(QuarterDetailActivtiy.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(QuarterDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(QuarterDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportId", bankJobReport.getReportId());
                params.put("reportTitle", content_one.getText().toString());
                params.put("reportCont", content_two.getText().toString());
                params.put("reportNext", content_three.getText().toString());
                params.put("reportType", "4");
                params.put("reportNumber", bankJobReport.getReportNumber());
                params.put("dateLine", bankJobReport.getDateLine());
                params.put("isUse", "0");
                params.put("status", "1");
                params.put("dateStartEnd", bankJobReport.getDateStartEnd());
                if(!StringUtil.isNullOrEmpty(bankJobReport.getReportFile())){
                    params.put("reportFile", bankJobReport.getReportFile());
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


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_report_comment_success")) {
                BankJobReportCommentBean bankJobReportCommentBean = (BankJobReportCommentBean) intent.getExtras().get("bankJobReportCommentBean");
                lists.add(0,bankJobReportCommentBean);
                adapter.notifyDataSetChanged();
            }
            if(action.equals("update_report_file_success")){
                //更新报表附件成功
                bankJobReport = (BankJobReport) intent.getExtras().get("bankJobReport");
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_report_comment_success");
        myIntentFilter.addAction("update_report_file_success");//更新报表附件成功
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    boolean isMobileNet, isWifiNet;

    //查阅详情
    void getDetailById(){
        //判断是否有网
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(QuarterDetailActivtiy.this);
            isWifiNet = HttpUtils.isWifiDataEnable(QuarterDetailActivtiy.this);
            if (!isMobileNet && !isWifiNet) {
                showMsg(QuarterDetailActivtiy.this, "请检查您的网络链接");
            } else {
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        InternetURL.REPORT_DETAIL_BY_ID_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (StringUtil.isJson(s)) {
                                } else {
                                    Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(QuarterDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("reportId", bankJobReport.getReportId());
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

                request.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                getRequestQueue().add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
