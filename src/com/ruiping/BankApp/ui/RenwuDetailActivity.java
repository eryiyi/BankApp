package com.ruiping.BankApp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.adapter.ItemTaskCommentAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobTaskCommentData;
import com.ruiping.BankApp.data.TaskBeanObjData;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.entiy.BankJobTaskComment;
import com.ruiping.BankApp.entiy.TaskBeanObj;
import com.ruiping.BankApp.popview.TaskPopMenu;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.HttpUtils;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.ContentListView;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.DoubleDatePickerDialog;
import com.ruiping.BankApp.widget.SelectDeleteWindow;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/7/2.
 */
public class RenwuDetailActivity extends BaseActivity implements View.OnClickListener, ContentListView.OnRefreshListener, ContentListView.OnLoadListener ,TaskPopMenu.OnItemClickListener{
    private TextView back;
    private TextView title;
    private ImageView right_btn;

    private ContentListView lstv;
    private LinearLayout headLiner;
    private ItemTaskCommentAdapter adapter;
    private List<BankJobTaskComment> lists = new ArrayList<BankJobTaskComment>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private BankJobTask bankJobTask;//任务详情
    private TaskBeanObj taskBeanObj;//任务主 全部信息

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    boolean isMobileNet, isWifiNet;
    boolean flag = false;//true是负责人或者创建人 false是他人的

    private TextView task_title;//标题
    private TextView dateline;//时间
    private TextView nickname;//负责人
    private ImageView head;//负责人
    private TextView endtime;//到期日
    private TextView progr;//百分比
    private SeekBar seekBar;//进度条
    private TextView child;//子任务数量
    private TextView people;//参与者数量
    private TextView attach;//附件点此查看
    private TextView task_type;//紧急程度
    private TextView starttime;//开始日期
    private TextView share_count;//共享人数
    private TextView content;

    private String taskId;//任务id

    //下拉菜单
    private TaskPopMenu menu;
    List<String> arrayMenu = new ArrayList<>();

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;
    private TextView txt6;
    private TextView txt7;

    private SelectDeleteWindow selectDeleteWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.renwu_detail_activity);
        taskId =  getIntent().getExtras().getString("taskId");

        initView();
        progressDialog = new CustomProgressDialog(RenwuDetailActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //获取任务详情
        getDetailTask();
        //获取评论
        getCommentData();

        arrayMenu.add("标记完成");
        arrayMenu.add("删除");

        //查询共享人数量
        getShareCount();
        changeColorOrSize();
    }

    void changeColorOrSize() {
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))) {
            txt1.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt2.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt3.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt4.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt5.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt6.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt6.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            txt7.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));

            task_title.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            dateline.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            nickname.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            endtime.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            progr.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            child.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            people.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            attach.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            task_type.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            starttime.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            share_count.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));

        }
    }


    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (ImageView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);
        title.setText("任务详情");

        lstv = (ContentListView) this.findViewById(R.id.lstv);
        headLiner = (LinearLayout) LayoutInflater.from(RenwuDetailActivity.this).inflate(R.layout.renwu_detail_header, null);
        task_title = (TextView) headLiner.findViewById(R.id.task_title);
        dateline = (TextView) headLiner.findViewById(R.id.dateline);
        nickname = (TextView) headLiner.findViewById(R.id.nickname);
        head = (ImageView) headLiner.findViewById(R.id.head);
        endtime = (TextView) headLiner.findViewById(R.id.endtime);
        progr = (TextView) headLiner.findViewById(R.id.progr);
        seekBar = (SeekBar) headLiner.findViewById(R.id.seekBar);
        child = (TextView) headLiner.findViewById(R.id.child);
        people = (TextView) headLiner.findViewById(R.id.people);
        attach = (TextView) headLiner.findViewById(R.id.attach);
        task_type = (TextView) headLiner.findViewById(R.id.task_type);
        starttime = (TextView) headLiner.findViewById(R.id.starttime);
        share_count = (TextView) headLiner.findViewById(R.id.share_count);
        content = (TextView) headLiner.findViewById(R.id.content);


        headLiner.findViewById(R.id.liner_header).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_endtime).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_child).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_people).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_attach).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_title).setOnClickListener(this);
        headLiner.findViewById(R.id.add_comment).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_type).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_starttime).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_content).setOnClickListener(this);
        headLiner.findViewById(R.id.liner_share).setOnClickListener(this);

        adapter = new ItemTaskCommentAdapter(lists, RenwuDetailActivity.this);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.addHeaderView(headLiner);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position-2 < lists.size()){
                    BankJobTaskComment bankJobReportCommentBean = lists.get(position-2);
                    Intent intent = new Intent(RenwuDetailActivity.this, AddTaskCommentActivity.class);
                    intent.putExtra("taskId", taskId);
                    intent.putExtra("commentId", bankJobReportCommentBean.getTaskCommentId());//父评论ID
                    intent.putExtra("reportCommentName", bankJobReportCommentBean.getBankEmp().getEmpName());
                    startActivity(intent);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updatePro(String.valueOf(progress));
            }
        });

        txt1 = (TextView) headLiner.findViewById(R.id.txt1);
        txt2 = (TextView) headLiner.findViewById(R.id.txt2);
        txt3 = (TextView) headLiner.findViewById(R.id.txt3);
        txt4 = (TextView) headLiner.findViewById(R.id.txt4);
        txt5 = (TextView) headLiner.findViewById(R.id.txt5);
        txt6 = (TextView) headLiner.findViewById(R.id.txt6);
        txt7 = (TextView) headLiner.findViewById(R.id.txt7);
    }

    //初始化数据
    void initData(){
        if(bankJobTask != null){
            if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                flag = true;
            }
        }

        if(flag){
            right_btn.setVisibility(View.VISIBLE);
        }else {
            right_btn.setVisibility(View.GONE);
        }

        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskTitle())){
            task_title.setText(bankJobTask.getTaskTitle());
        }
        if(!StringUtil.isNullOrEmpty(bankJobTask.getDateLine())){
            dateline.setText("起始日期："+ bankJobTask.getDateLine());
        }
        if(bankJobTask.getBankEmpf() != null){
            nickname.setText(bankJobTask.getBankEmpf().getEmpName());
            imageLoader.displayImage(InternetURL.INTERNAL + bankJobTask.getBankEmpf().getEmpCover(), head, BankAppApplication.txOptions, animateFirstListener);
        }

        if(!StringUtil.isNullOrEmpty(bankJobTask.getDateLine())){
            starttime.setText(bankJobTask.getDateLine());
        }
        if(!StringUtil.isNullOrEmpty(bankJobTask.getDateLineEnd())){
            endtime.setText(bankJobTask.getDateLineEnd());
        }

        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskProgress())){
            progr.setText(bankJobTask.getTaskProgress());
        }
        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskProgress())){
            String str = bankJobTask.getTaskProgress().replaceAll("%", "");
            if(!StringUtil.isNullOrEmpty(str)){
                seekBar.setProgress(Integer.parseInt(str));
            }
        }
        if(flag){
            seekBar.setEnabled(true);
        }else {
            seekBar.setEnabled(false);
        }
        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskType())){
            switch (Integer.parseInt(bankJobTask.getTaskType()))
            {
                case 0:
                    task_type.setText("正常");
                    break;
                case 1:
                    task_type.setText("紧急");
                    break;
                case 2:
                    task_type.setText("非常紧急");
                    break;
            }
        }

        people.setText(taskBeanObj.getJoinEmpNum()==null?"0":taskBeanObj.getJoinEmpNum());
        child.setText(taskBeanObj.getSubTaskNum()==null?"0":taskBeanObj.getSubTaskNum());

        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskFile())){
            String taskFile = bankJobTask.getTaskFile();
            String[] arras = taskFile.split(",");
            attach.setText(String.valueOf(arras.length));
        }else{
            attach.setText("0");
        }

        if(!StringUtil.isNullOrEmpty(bankJobTask.getTaskCont())){
            content.setText(bankJobTask.getTaskCont());
        }

//        endtime.setText(bankJobTask.getDateLineEnd());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_comment:
                //添加评论
            {
                Intent intent = new Intent(RenwuDetailActivity.this, AddTaskCommentActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("commentId", "");
                intent.putExtra("reportCommentName", "");

                startActivity(intent);
            }
                break;
            case R.id.liner_header:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                if(flag){
                    if(!StringUtil.isNullOrEmpty(bankJobTask.getPid()) && !"0".equals(bankJobTask.getPid())){
                        //说明有pid  是子任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskPersonFuzerenSelectActivity.class);
                            intent.putExtra("taskId", bankJobTask.getTaskId());//任务ID
                            startActivityForResult(intent, 1000);
                        }
                    }else{
                        //说明是主任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskPersonFuzerenSelectActivity.class);
                            intent.putExtra("taskId", bankJobTask.getTaskId());//任务ID
                            startActivityForResult(intent, 1000);
                        }
                    }
                }

            }
                break;
            case R.id.liner_child:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //子任务
                Intent intent = new Intent(RenwuDetailActivity.this, RenwuChildListActivity.class);
                intent.putExtra("taskId", bankJobTask.getTaskId());//任务ID
                intent.putExtra("empIdF", bankJobTask.getEmpIdF());//任务负责人ID
                intent.putExtra("taskTitle", bankJobTask.getTaskTitle());
                if(!StringUtil.isNullOrEmpty(bankJobTask.getDateLine())){
                    intent.putExtra("dateLine", bankJobTask.getDateLine());
                }else {
                    intent.putExtra("dateLine", "");
                }
                if(!StringUtil.isNullOrEmpty(bankJobTask.getDateLineEnd())){
                    intent.putExtra("dateLineEnd", bankJobTask.getDateLineEnd());
                }else{
                    intent.putExtra("dateLineEnd", "");
                }
                startActivity(intent);
            }
            break;
            case R.id.liner_people:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //参与人
                Intent intent = new Intent(RenwuDetailActivity.this, TaskPersonActivity.class);
                intent.putExtra("taskId", bankJobTask.getTaskId());//任务ID
                intent.putExtra("empIdF", bankJobTask.getEmpIdF());//任务负责人ID
                startActivity(intent);
            }
            break;
            case R.id.liner_attach:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //附件
                Intent intent = new Intent(RenwuDetailActivity.this, AttachMentTaskActivity.class);
                intent.putExtra("attach_file",(bankJobTask.getTaskFile()==null?"":bankJobTask.getTaskFile()));
                intent.putExtra("taskId", bankJobTask.getTaskId());
                intent.putExtra("empIdF", bankJobTask.getEmpIdF());
                startActivity(intent);
            }
            break;
            case R.id.liner_title:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //标题
                if(flag){
                    if(!StringUtil.isNullOrEmpty(bankJobTask.getPid()) && !"0".equals(bankJobTask.getPid())){
                        //说明有pid  是子任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskWriteContentActivity.class);
                            intent.putExtra("content", task_title.getText().toString());
                            startActivityForResult(intent, 1001);
                        }
                    }else{
                        //说明是主任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskWriteContentActivity.class);
                            intent.putExtra("content", task_title.getText().toString());
                            startActivityForResult(intent, 1001);
                        }
                    }
                }
            }
                break;
            case R.id.liner_type:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //紧急程度
                if(flag) {
                    showTaskTypeSelect();
                }
            }
                break;
            case R.id.liner_starttime:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                if(flag){
                    if(!StringUtil.isNullOrEmpty(bankJobTask.getPid()) && !"0".equals(bankJobTask.getPid())){
                        //说明有pid  是子任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            //开始日期
                            Calendar c = Calendar.getInstance();
                            // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                            new DoubleDatePickerDialog(RenwuDetailActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                      int startDayOfMonth) {
                                    String textString = String.format("%d-%d-%d", startYear,
                                            startMonthOfYear + 1, startDayOfMonth);
                                    //调用接口
                                    updateStart(textString);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
                        }
                    }else{
                        //说明是主任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            //开始日期
                            Calendar c = Calendar.getInstance();
                            // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                            new DoubleDatePickerDialog(RenwuDetailActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                      int startDayOfMonth) {
                                    String textString = String.format("%d-%d-%d", startYear,
                                            startMonthOfYear + 1, startDayOfMonth);
                                    //调用接口
                                    updateStart(textString);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
                        }
                    }
                }
            }
                break;
            case R.id.liner_endtime:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                if(flag){
                    if(!StringUtil.isNullOrEmpty(bankJobTask.getPid()) && !"0".equals(bankJobTask.getPid())){
                        //说明有pid  是子任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            //到期日期
                            Calendar c = Calendar.getInstance();
                            // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                            new DoubleDatePickerDialog(RenwuDetailActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                      int startDayOfMonth) {
                                    String textString = String.format("%d-%d-%d", startYear,
                                            startMonthOfYear + 1, startDayOfMonth);
//                        endtime.setText(textString);
                                    //调用接口
                                    updateEnd(textString);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
                        }
                    }else{
                        //说明是主任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            //到期日期
                            Calendar c = Calendar.getInstance();
                            // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                            new DoubleDatePickerDialog(RenwuDetailActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                      int startDayOfMonth) {
                                    String textString = String.format("%d-%d-%d", startYear,
                                            startMonthOfYear + 1, startDayOfMonth);
//                        endtime.setText(textString);
                                    //调用接口
                                    updateEnd(textString);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
                        }
                    }


                }
            }
            break;
            case R.id.liner_pro:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                if(flag){
                    updatePro("");
                }
            }
                break;
            case R.id.liner_share:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                //共享人
                Intent intent = new Intent(RenwuDetailActivity.this, RenwuShareActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("flag", String.valueOf(flag));
                startActivity(intent);
            }
                break;
            case R.id.liner_content:
            {
                if("1".equals(bankJobTask.getIsType())){
                    showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                    return;
                }
                if(flag){
                    if(!StringUtil.isNullOrEmpty(bankJobTask.getPid()) && !"0".equals(bankJobTask.getPid())){
                        //说明有pid  是子任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskWriteBeizhuActivity.class);
                            intent.putExtra("content", content.getText().toString());
                            startActivityForResult(intent, 1002);
                        }
                    }else{
                        //说明是主任务
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) ||bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpIdF().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            Intent intent = new Intent(RenwuDetailActivity.this, TaskWriteBeizhuActivity.class);
                            intent.putExtra("content", content.getText().toString());
                            startActivityForResult(intent, 1002);
                        }
                    }
                }
            }
                break;
        }
    }

    private void showTaskTypeSelect() {
        final Dialog picAddDialog = new Dialog(RenwuDetailActivity.this, R.style.MyAlertDialog);
        View picAddInflate = View.inflate(this, R.layout.task_type_dialog, null);
        Button btn_one = (Button) picAddInflate.findViewById(R.id.btn_one);
        Button btn_two = (Button) picAddInflate.findViewById(R.id.btn_two);
        Button btn_three = (Button) picAddInflate.findViewById(R.id.btn_three);
        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateType("0");
                picAddDialog.dismiss();
            }
        });

        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateType("1");
                picAddDialog.dismiss();
            }
        });

        btn_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateType("2");
                picAddDialog.dismiss();
            }
        });

        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    //弹出顶部主菜单
    public void onTopMenuPopupButtonClick(View view) {
        //判断是否有网
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(RenwuDetailActivity.this);
            isWifiNet = HttpUtils.isWifiDataEnable(RenwuDetailActivity.this);
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(RenwuDetailActivity.this, "请检查网络链接", Toast.LENGTH_SHORT).show();
            }else{
                //顶部右侧按钮
                menu = new TaskPopMenu(RenwuDetailActivity.this, arrayMenu);
                menu.setOnItemClickListener(this);
                menu.showAsDropDown(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 10002){
            BankEmpBean bankJobTaskEmp = (BankEmpBean) data.getExtras().get("bankJobTaskEmp");
            nickname.setText(bankJobTaskEmp.getEmpName());
            imageLoader.displayImage(InternetURL.INTERNAL + bankJobTaskEmp.getEmpCover(), head, BankAppApplication.txOptions, animateFirstListener);
        }
        if(requestCode == 1001 && resultCode == 10001){
            String xontent = data.getExtras().getString("xontent");
            if(!StringUtil.isNullOrEmpty(xontent)){
                updateTitle(xontent);
            }

        }
        if(requestCode == 1002 && resultCode == 10001){
            String xontent = data.getExtras().getString("xontent");
            if(!StringUtil.isNullOrEmpty(xontent)){
                updateContent(xontent);
            }

        }
    }

    @Override
    public void onLoad() {
            //评论
            IS_REFRESH = false;
            pageIndex++;
        getCommentData();
    }

    @Override
    public void onRefresh() {
            IS_REFRESH = true;
            pageIndex = 1;
        getCommentData();
    }


    //评论列表
    private void getCommentData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_COMMENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobTaskCommentData data = getGson().fromJson(s, BankJobTaskCommentData.class);
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
//                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        lstv.setResultSize(lists.size());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();

                        Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
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

    //获取任务详情
    private void getDetailTask(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_DETAIL_TASK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    TaskBeanObjData data = getGson().fromJson(s, TaskBeanObjData.class);
                                    if(data != null){
                                        taskBeanObj = data.getData();
                                        if(taskBeanObj != null){
                                            List<BankJobTask> bankJobTasks = taskBeanObj.getBankJobTask();
                                            if(bankJobTasks != null){
                                                bankJobTask = bankJobTasks.get(0);//获得任务详情了
                                                if(bankJobTask != null){
                                                    if(StringUtil.isNullOrEmpty(bankJobTask.getEmpIdZf())){
                                                        bankJobTask.setEmpIdZf("");
                                                    }
                                                    initData();//初始化数据
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        lstv.setResultSize(lists.size());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();

                        Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
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

    private void getShareCount(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_RENWU_SHARE_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    String data = jo.getString("data");
                                    share_count.setText(data);
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        lstv.setResultSize(lists.size());
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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





    @Override
    public void onItemClick(int index, String str) {
        if("000".equals(str)){
            switch (index) {
                case 0:
                {
                    if("1".equals(bankJobTask.getIsType())){
                        showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                        return;
                    }else{
                        //标记完成
                        progressDialog = new CustomProgressDialog(RenwuDetailActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        doneFFinishTask();
                    }
                }

                    break;
                case 1:
                    //删除
                    if("1".equals(bankJobTask.getIsType())){
                        showMsg(RenwuDetailActivity.this, "任务已标记完成，不能继续操作！");
                        return;
                    }else{
                        if(bankJobTask.getEmpIdZf().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class)) || bankJobTask.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                            showDialog();
                        }else{
                            showMsg(RenwuDetailActivity.this, "你没有权限删除该任务！");
                        }
                    }
                    break;
                case  2:
                    break;
            }
        }
    }

    //标记完成任务
    private void doneFFinishTask(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_FINISHED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(RenwuDetailActivity.this, "操作成功！");
                                    //完成进度100%
                                    progr.setText("100%");
                                    seekBar.setProgress(100);
                                    bankJobTask.setIsType("1");
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId",getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("taskId", taskId);
                params.put("dateLineEnd", DateUtil.getDateAndTimeTwo());
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

    //删除任务
    private void deleteTask(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(RenwuDetailActivity.this, "操作成功！");
                                    Intent intent1 = new Intent("update_renwu_number");
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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


    //更新日期
    private void updateStart(final String strtime){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_START_TIME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    starttime.setText(strtime);
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("dateLine", strtime);
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

    //更新日期
    private void updateEnd(final String strtime){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_END_TIME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    endtime.setText(strtime);
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("dateLineEnd", strtime);
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

    //更新标题
    private void updateTitle(final String title){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_TITLE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    task_title.setText(title);
                                    bankJobTask.setTaskTitle(title);
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("taskTitle", title);
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


    private void updateContent(final String title){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.updateMemo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    content.setText(title);
                                    bankJobTask.setTaskCont(title);
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("taskCont", title);
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


    //更新任务类型
    private void updateType(final String taskType){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_URGENCY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                   switch (Integer.parseInt(taskType)){
                                       case 0:
                                           task_type.setText("正常");
                                           break;
                                       case 1:
                                           task_type.setText("紧急");
                                           break;
                                       case 2:
                                           task_type.setText("非常紧急");
                                           break;
                                   }
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("taskType", taskType);
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

    //更新季度
    private void updatePro(final String taskProgress){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.TASK_UPDATE_PRO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    progr.setText(taskProgress+"%");
                                } else {
                                    Toast.makeText(RenwuDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenwuDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("taskId", taskId);
                params.put("taskProgress", taskProgress+"%");
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
            if (action.equals("add_task_comment_success")) {
                BankJobTaskComment bankJobTaskComment = (BankJobTaskComment) intent.getExtras().get("bankJobTaskComment");
                lists.add(0,bankJobTaskComment);
                adapter.notifyDataSetChanged();
            }
            if(action.equals("update_task_file_success")){
                String attach_file = intent.getExtras().getString("attach_file");
                bankJobTask.setTaskFile(attach_file);
                if(!StringUtil.isNullOrEmpty(attach.getText().toString())){
                    attach.setText(String.valueOf((Integer.parseInt(attach.getText().toString())+1)));
                }
                else {
                    attach.setText("1");
                }
            }
            if(action.equals("update_task_person_count_success")){
                if(!StringUtil.isNullOrEmpty(people.getText().toString())){
                    people.setText(String.valueOf(Integer.parseInt(people.getText().toString()) + 1));
                    taskBeanObj.setJoinEmpNum(String.valueOf(Integer.parseInt(people.getText().toString()) + 1));
                }else {
                    people.setText("1");
                    taskBeanObj.setJoinEmpNum("1");
                }
            }
            if(action.equals("add_person_task_share_success")){
                getShareCount();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_task_comment_success");
        myIntentFilter.addAction("update_task_file_success");//更新报表附件成功
        myIntentFilter.addAction("update_task_person_count_success");//更新参与人数
        myIntentFilter.addAction("add_person_task_share_success");//更新共享人数
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void showDialog() {
        selectDeleteWindow = new SelectDeleteWindow(RenwuDetailActivity.this, itemsOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        selectDeleteWindow.setBackgroundDrawable(new BitmapDrawable());
        selectDeleteWindow.setFocusable(true);
        selectDeleteWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectDeleteWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            selectDeleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_sure: {
                    progressDialog = new CustomProgressDialog(RenwuDetailActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    deleteTask();
                }
                break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) RenwuDetailActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) RenwuDetailActivity.this).getWindow().setAttributes(lp);
    }


}
