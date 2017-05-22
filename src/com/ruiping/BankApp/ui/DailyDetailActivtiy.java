package com.ruiping.BankApp.ui;

import android.app.Dialog;
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
import com.ruiping.BankApp.util.DateUtil;
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
 */
public class DailyDetailActivtiy extends BaseActivity implements View.OnClickListener, ContentListView.OnRefreshListener, ContentListView.OnLoadListener{
    private TextView back;
    private TextView title;
    private TextView right_btn;
    private ContentListView lstv;//评论集合
    private LinearLayout headLiner;
    private ItemCommentAdapter adapter;
    private List<BankJobReportCommentBean> lists = new ArrayList<BankJobReportCommentBean>();//评论数据列表
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ContentListView lstvT;//查阅集合
    private ItemDoneAdapter adapterT;//查阅适配器
    private static boolean IS_REFRESH_T = true;
    private int pageIndex_T = 1;
    private List<BankJobReportDoneBean> listsDone = new ArrayList<BankJobReportDoneBean>();//查阅数据列表

    private View view_one;
    private View view_two;

    private BankJobReport bankJobReport;
    private ImageView head;
    private TextView nickname;
    private TextView dateline;
    private TextView content;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private RelativeLayout relate_one;
    private RelativeLayout relate_two;

    private int tmpSelect = 0;
    private TextView btn_attachment;//附件

    private TextView btn_edit;
    private TextView btn_delete;
    private LinearLayout liner_bottom;

    boolean isMobileNet, isWifiNet;
    boolean flag = false;//true是自己 false是他人的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_detail_activity);
        registerBoradcastReceiver();
        bankJobReport = (BankJobReport) getIntent().getExtras().get("bankJobReport");
        if(bankJobReport != null){
            if(bankJobReport.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                flag = true;
            }
        }
        initView();

        progressDialog = new CustomProgressDialog(DailyDetailActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        initData();//获得评论列表
        getDone();//获得日报查阅列表
//        getDetail();//获得日报详情
        changeColorOrSize();
    }

    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            nickname.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            dateline.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            content.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
        }
    }




    private void getDetail() {
        //判断是否有网
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(DailyDetailActivtiy.this);
            isWifiNet = HttpUtils.isWifiDataEnable(DailyDetailActivtiy.this);
            if (!isMobileNet && !isWifiNet) {
                showMsg(DailyDetailActivtiy.this, "请检查您的网络链接");
            } else {
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        InternetURL.REPORT_DETAIL_BY_ID_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (StringUtil.isJson(s)) {
                                } else {
                                    Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    private void initView() {
        btn_edit = (TextView) this.findViewById(R.id.btn_edit);
        btn_delete = (TextView) this.findViewById(R.id.btn_delete);
        liner_bottom = (LinearLayout) this.findViewById(R.id.liner_bottom);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        relate_one = (RelativeLayout) this.findViewById(R.id.relate_one);
        relate_two = (RelativeLayout) this.findViewById(R.id.relate_two);
        relate_one.setVisibility(View.VISIBLE);
        relate_two.setVisibility(View.GONE);
        tmpSelect = 0;
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setVisibility(View.GONE);
        if(flag){
            //是自己
            right_btn.setVisibility(View.VISIBLE);
            right_btn.setText("保存");
            right_btn.setOnClickListener(this);
            liner_bottom.setVisibility(View.VISIBLE);
        }else{
            //他人
            right_btn.setVisibility(View.GONE);
            liner_bottom.setVisibility(View.GONE);
        }
        back.setOnClickListener(this);
        title.setText("日报详情");

        lstv = (ContentListView) this.findViewById(R.id.lstv);
        lstvT = (ContentListView) this.findViewById(R.id.lstv2);
        headLiner = (LinearLayout) LayoutInflater.from(DailyDetailActivtiy.this).inflate(R.layout.daily_detail_header, null);
        view_one = headLiner.findViewById(R.id.view_one);
        view_two = headLiner.findViewById(R.id.view_two);
        headLiner.findViewById(R.id.btn_brower).setOnClickListener(this);
        headLiner.findViewById(R.id.btn_comment).setOnClickListener(this);
        headLiner.findViewById(R.id.add_comment).setOnClickListener(this);
        head = (ImageView) headLiner.findViewById(R.id.head);
        nickname = (TextView) headLiner.findViewById(R.id.nickname);
        dateline = (TextView) headLiner.findViewById(R.id.dateline);
        content = (TextView) headLiner.findViewById(R.id.content);
        imageLoader.displayImage(InternetURL.INTERNAL + bankJobReport.getBankEmp().getEmpCover(), head, animateFirstListener);
        nickname.setText(bankJobReport.getBankEmp().getEmpName());
        dateline.setText(DateUtil.getDate(bankJobReport.getDateLine(),"MM-dd HH:mm"));
        content.setText(bankJobReport.getReportTitle());
        content.setOnClickListener(this);
        btn_attachment = (TextView) headLiner.findViewById(R.id.btn_attachment);
        btn_attachment.setOnClickListener(this);
        adapter = new ItemCommentAdapter(lists, DailyDetailActivtiy.this);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.addHeaderView(headLiner);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position-2 < lists.size()){
                    BankJobReportCommentBean bankJobReportCommentBean = lists.get(position-2);
                    Intent intent = new Intent(DailyDetailActivtiy.this, AddDailyCommentActivity.class);
                    intent.putExtra("reportId", bankJobReport.getReportId());
                    intent.putExtra("reportCommentId", bankJobReportCommentBean.getReportCommentId());//父评论ID
                    intent.putExtra("reportCommentName", bankJobReportCommentBean.getBankEmp().getEmpName());//父评论ID
                    startActivity(intent);
                }
            }
        });

        adapterT = new ItemDoneAdapter(listsDone, DailyDetailActivtiy.this);
        lstvT.setOnRefreshListener(this);
        lstvT.setOnLoadListener(this);
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
                Intent intent = new Intent(DailyDetailActivtiy.this, AddDailyCommentActivity.class);
                intent.putExtra("reportId", bankJobReport.getReportId());
                intent.putExtra("reportCommentId", "");
                intent.putExtra("reportCommentName", "");
                startActivity(intent);
            }
                break;
            case R.id.btn_attachment:
            {
                //附件
                Intent intent  = new Intent(DailyDetailActivtiy.this, AttachMentActivity.class);
                intent.putExtra("attach_file", (bankJobReport.getReportFile()==null?"":bankJobReport.getReportFile()));//附件列表
                intent.putExtra("reportId", bankJobReport.getReportId());//报表id
                intent.putExtra("empId", bankJobReport.getEmpId());//用户ID
                startActivity(intent);
            }
                break;
            case R.id.right_btn:
            {
                //更新
                updateDaily();
            }
            break;
            case R.id.content:
            case R.id.btn_edit:
            {
                //内容点击
                if(flag){
                    Intent intent = new Intent(DailyDetailActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "1");
                    intent.putExtra("typeWrite_content", content.getText().toString());
                    startActivityForResult(intent, 1000);
                }
            }
                break;
            case R.id.btn_delete:
            {
                if(flag){
                    showVersionDialog();
                }
            }
                break;
        }
    }

    private void showVersionDialog() {
        final Dialog picAddDialog = new Dialog(DailyDetailActivtiy.this, R.style.MyAlertDialog);
        View picAddInflate = View.inflate(this, R.layout.dialog_del_version, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new CustomProgressDialog(DailyDetailActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                deleteById();
                picAddDialog.dismiss();
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                {
                    String xStr = data.getExtras().getString("xontent");
                    content.setText(xStr);
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
            initData();
        }
        if(tmpSelect == 1){
            //查阅
            pageIndex_T++;
            IS_REFRESH_T = false;
            getDone();
        }
    }

    @Override
    public void onRefresh() {
        if(tmpSelect == 0){
            IS_REFRESH = true;
            pageIndex = 1;
            initData();
        }
        if(tmpSelect == 1){
            //查阅
            pageIndex_T = 1;
            IS_REFRESH_T = true;
            getDone();
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
                                Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                                if (IS_REFRESH_T) {
                                    listsDone.clear();
                                    lstvT.onRefreshComplete();
                                    listsDone.addAll(data.getData());
                                    lstvT.setResultSize(data.getData().size());
                                } else {
                                    lstvT.onLoadComplete();
                                    listsDone.addAll(data.getData());
                                    lstvT.setResultSize(data.getData().size());
                                }
                                adapterT.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DailyDetailActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        myIntentFilter.addAction("add_report_comment_success");//添加日报评论
        myIntentFilter.addAction("update_report_file_success");//更新报表附件成功
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    //更新日报信息
    void updateDaily(){
        //更新
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_UPDATE_BY_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    showMsg(DailyDetailActivtiy.this, "操作成功！");
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    BankJobReport bankJobReportCommentBean = data.getData();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent("update_report_daily_success");
                                    sendBroadcast(intent1);
                                    finish();
                                }else {
                                    showMsg(DailyDetailActivtiy.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(DailyDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DailyDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", bankJobReport.getReportId());
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportTitle", content.getText().toString());
                params.put("reportCont", content.getText().toString());
                params.put("reportType", "1");
                params.put("dateLine", bankJobReport.getDateLine());
                params.put("isUse", "0");
                params.put("status", "1");
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

    private void deleteById() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankJobReportCommentBeanData data = getGson().fromJson(s, BankJobReportCommentBeanData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(DailyDetailActivtiy.this, "删除成功", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent("del_report_daily_success");
                                sendBroadcast(intent1);
                                finish();
                            } else {
                                Toast.makeText(DailyDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DailyDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DailyDetailActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", bankJobReport.getReportId());
                params.put("empId", bankJobReport.getEmpId());
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
