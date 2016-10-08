package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.adapter.ItemSelectFileAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportData;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 * 添加周报
 */
public class AddMineMonthActivtiy extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener {
    //标准配置
    private TextView back;
    private TextView title;
    private TextView right_btn;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    //上传文件配置信息
    private Intent fileChooserIntent ;
    private static final int REQUEST_CODE = 1;   //请求码

    //顶部banner
    private TextView btn_left;//上月
    private TextView btn_right;//本月

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;


    //头部信息--one
    private ImageView head1;
    private TextView nickname1;
    private TextView dateline1;
    private TextView week1;
    private TextView content_one1;
    private TextView content_two1;
    private TextView content_three1;
    private TextView btn_attachment1;//附件
    private LinearLayout headLiner1;
    private ListView lstv1;
    private ItemSelectFileAdapter adapter1;
    private String reportFile1 = "";
    private ArrayList<String> dataList1 = new ArrayList<String>();//选择文件  手机的路径
    private ArrayList<String> dataListName1 = new ArrayList<String>();//选择文件的文件名  手机的路径

    private ArrayList<String> dataListOne = new ArrayList<String>();//上周的附件列表  附件路径 总
    private ArrayList<String> dataListOneName = new ArrayList<String>();//上周的附件列表 附件名字 总

    private ArrayList<String> dataListOneL = new ArrayList<String>();//上周的附件列表  附件路径
    private ArrayList<String> dataListOneNameL = new ArrayList<String>();//上周的附件列表 附件名字

    //附件上传
    private List<String> fileUrls1 = new ArrayList<String>();//上传文件返回的保存路径
    private List<String> fileNames1 = new ArrayList<String>();//上传文件返回的文件名

    //头部信息--two
    private ImageView head2;
    private TextView nickname2;
    private TextView dateline2;
    private TextView week2;
    private TextView content_one2;
    private TextView content_two2;
    private TextView content_three2;
    private TextView btn_attachment2;//附件
    private LinearLayout headLiner2;
    private ListView lstv2;
    private ItemSelectFileAdapter adapter2;
    private String reportFile2 = "";
    private ArrayList<String> dataList2 = new ArrayList<String>();//选择文件  手机的路径
    private ArrayList<String> dataListName2 = new ArrayList<String>();//选择文件的文件名  手机的路径
    //附件上传
    private List<String> fileUrls2 = new ArrayList<String>();//上传文件返回的保存路径
    private List<String> fileNames2 = new ArrayList<String>();//上传文件返回的文件名

    private ArrayList<String> dataListTwo = new ArrayList<String>();//本周的附件列表 附件路径 总
    private ArrayList<String> dataListTwoName = new ArrayList<String>();//本周的附件列表  附件名字 总

    private ArrayList<String> dataListTwoL = new ArrayList<String>();//本周的附件列表 附件路径
    private ArrayList<String> dataListTwoNameL = new ArrayList<String>();//本周的附件列表  附件名字

    private int currentSelect = 0;//当前选中的viewpage

    private BankJobReport bankJobReportOne;//上周
    private BankJobReport bankJobReportTwo;//本周

    private int currentMoonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mine_moonth_activity);
        initView();

        progressDialog = new CustomProgressDialog(AddMineMonthActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        currentMoonth = Integer.parseInt(DateUtil.getMonth());
        initViewOne();
        initViewTwo();

        //先查询上月和本月有没有月报
        if(currentMoonth >1){
            getWeekDateOne();
            getWeekDateTwo();
        }else {

            bankJobReportOne = null;
            initDataViewOneNull();

            getWeekDateTwo();
        }
    }

    void initDataViewOneNull(){
        imageLoader.displayImage(InternetURL.INTERNAL + getGson().fromJson(getSp().getString(Contance.EMP_COVER, ""), String.class), head1, animateFirstListener);
        nickname1.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class) + "暂无上月工作报告");
        dateline1.setText("暂无上月工作报告");
        week1.setText("暂无上月工作报告");
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void initViewOne() {
        lstv1 = (ListView) view1.findViewById(R.id.lstv);
        headLiner1 = (LinearLayout) LayoutInflater.from(AddMineMonthActivtiy.this).inflate(R.layout.add_mine_moonth_header, null);
        btn_attachment1 = (TextView) headLiner1.findViewById(R.id.btn_attachment);//附件
        btn_attachment1.setOnClickListener(this);

        head1 = (ImageView) headLiner1.findViewById(R.id.head);//头像
        nickname1 = (TextView) headLiner1.findViewById(R.id.nickname);//周报标题
        dateline1 = (TextView) headLiner1.findViewById(R.id.dateline);//时间
        week1 = (TextView) headLiner1.findViewById(R.id.week);//所属周
        content_one1 = (TextView) headLiner1.findViewById(R.id.content_one);//工作成效
        content_two1 = (TextView) headLiner1.findViewById(R.id.content_two);//工作总结
        content_three1 = (TextView) headLiner1.findViewById(R.id.content_three);//工作计划

        content_one1.setOnClickListener(this);
        content_two1.setOnClickListener(this);
        content_three1.setOnClickListener(this);

        lstv1.addHeaderView(headLiner1);
        adapter1 = new ItemSelectFileAdapter(dataListOneName, AddMineMonthActivtiy.this);
        lstv1.setAdapter(adapter1);
        adapter1.setOnClickContentItemListener(this);
    }

    //上月数据填充
    void initDataViewOne(){
        if(bankJobReportOne == null){
            imageLoader.displayImage(InternetURL.INTERNAL + getGson().fromJson(getSp().getString(Contance.EMP_COVER, ""), String.class), head1, animateFirstListener);
            nickname1.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class) + "的" + DateUtil.getYear() + "年" + (currentMoonth - 1) + "月份工作报告");

            if(Integer.parseInt(DateUtil.getMonth()) > 1){
                dateline1.setText(DateUtil.getYear() + "年"+ String.valueOf(currentMoonth - 1)+"月份");
                week1.setText((Integer.parseInt(DateUtil.getMonth())-1)+".01"+ "-" + (Integer.parseInt(DateUtil.getMonth()) - 1)+"."+DateUtil.getPreLastMonthDay());
            }else {
                //说明是1月份
                dateline1.setText("暂无上月数据");
                week1.setText("暂无上月数据");
            }

        }else {
            imageLoader.displayImage(InternetURL.INTERNAL + bankJobReportOne.getBankEmp().getEmpCover(), head1, animateFirstListener);
            nickname1.setText(bankJobReportOne.getBankEmp().getEmpName() + "的"+ bankJobReportOne.getReportNumber()+"工作报告");
            dateline1.setText(bankJobReportOne.getReportNumber());
            week1.setText(bankJobReportOne.getDateStartEnd());
            content_one1.setText(bankJobReportOne.getReportTitle()==null?"":bankJobReportOne.getReportTitle());
            content_two1.setText(bankJobReportOne.getReportCont()==null?"":bankJobReportOne.getReportCont());
            content_three1.setText(bankJobReportOne.getReportNext()==null?"":bankJobReportOne.getReportNext());
            reportFile1 = bankJobReportOne.getReportFile();

            if(!StringUtil.isNullOrEmpty(reportFile1)){
                //如果存在附件
                String[] arrs = reportFile1.split(",");
                if(arrs != null){
                    for(String str:arrs){
                        String[] strA = str.split("\\|");
                        if(strA != null && strA.length == 2){
                            dataListOne.add(strA[1]);
                            dataListOneL.add(strA[1]);
                            dataListOneName.add(strA[0]);
                            dataListOneNameL.add(strA[0]);
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }
        }
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    //本周数据填充
    void initDataViewTwo(){
        if(bankJobReportTwo == null){
            imageLoader.displayImage(InternetURL.INTERNAL + getGson().fromJson(getSp().getString(Contance.EMP_COVER, ""), String.class), head2, animateFirstListener);
            nickname2.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class) + "的"+DateUtil.getYear()+ "年"+ currentMoonth +"月份工作报告");

            dateline2.setText(DateUtil.getYear() + "年"+ String.valueOf(currentMoonth)+"月份");
            week2.setText(DateUtil.getMonth()+".01"+ "-" + DateUtil.getMonth()+"."+DateUtil.getLastMonthDay());
        }else {
            imageLoader.displayImage(InternetURL.INTERNAL + bankJobReportTwo.getBankEmp().getEmpCover(), head2, animateFirstListener);
            nickname2.setText(bankJobReportTwo.getBankEmp().getEmpName() + "的"+ bankJobReportTwo.getReportNumber()+"工作报告");
            dateline2.setText(bankJobReportTwo.getReportNumber());
            week2.setText(bankJobReportTwo.getDateStartEnd());
            content_one2.setText(bankJobReportTwo.getReportTitle()==null?"":bankJobReportTwo.getReportTitle());
            content_two2.setText(bankJobReportTwo.getReportCont()==null?"":bankJobReportTwo.getReportCont());
            content_three2.setText(bankJobReportTwo.getReportNext()==null?"":bankJobReportTwo.getReportNext());
            reportFile2 = bankJobReportTwo.getReportFile();

            if(!StringUtil.isNullOrEmpty(reportFile2)){
                //如果存在附件
                String[] arrs = reportFile2.split(",");
                if(arrs != null){
                    for(String str:arrs){
                        String[] strA = str.split("\\|");
                        if(strA != null && strA.length == 2){
                            dataListTwo.add(strA[1]);
                            dataListTwoL.add(strA[1]);
                            dataListTwoName.add(strA[0]);
                            dataListTwoNameL.add(strA[0]);
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
            }
        }
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void initViewTwo() {
        lstv2 = (ListView) view2.findViewById(R.id.lstv);
        headLiner2 = (LinearLayout) LayoutInflater.from(AddMineMonthActivtiy.this).inflate(R.layout.add_mine_moonth_header, null);
        btn_attachment2 = (TextView) headLiner2.findViewById(R.id.btn_attachment);//附件
        btn_attachment2.setOnClickListener(this);

        head2 = (ImageView) headLiner2.findViewById(R.id.head);//头像
        nickname2 = (TextView) headLiner2.findViewById(R.id.nickname);//周报标题
        dateline2 = (TextView) headLiner2.findViewById(R.id.dateline);//时间
        week2 = (TextView) headLiner2.findViewById(R.id.week);//所属周
        content_one2 = (TextView) headLiner2.findViewById(R.id.content_one);//工作成效
        content_two2 = (TextView) headLiner2.findViewById(R.id.content_two);//工作总结
        content_three2 = (TextView) headLiner2.findViewById(R.id.content_three);//工作计划

        content_one2.setOnClickListener(this);
        content_two2.setOnClickListener(this);
        content_three2.setOnClickListener(this);

        lstv2.addHeaderView(headLiner2);
        adapter2 = new ItemSelectFileAdapter(dataListTwoName, AddMineMonthActivtiy.this);
        lstv2.setAdapter(adapter2);
        adapter2.setOnClickContentItemListener(this);
    }

    private void initView() {
        //标准配置
        back = (TextView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("添加月报");
        right_btn.setText("保存");
        right_btn.setOnClickListener(this);

        //顶部banner
        btn_left = (TextView) this.findViewById(R.id.btn_left);
        btn_right = (TextView) this.findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.add_mine_weekly_lay1, null);
        view2 = inflater.inflate(R.layout.add_mine_weekly_lay1, null);
        views.add(view1);
        views.add(view2);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 1;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //保存周报
                //先判断是上周还是本周的
                if(currentSelect == 0){
                    //上周
                    if(StringUtil.isNullOrEmpty(content_one1.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作成效！");
                        return;
                    }
                    if(content_one1.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(content_two1.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作总结！");
                        return;
                    }
                    if(content_two1.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(content_three1.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作计划！");
                        return;
                    }
                    if(content_three1.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }

                    //没有 添加
                    progressDialog = new CustomProgressDialog(AddMineMonthActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    if(dataList1 != null && dataList1.size() > 0){
                        //有图
                        sendFile1();
                    }else {
                        //没有图片
                        if(bankJobReportOne == null){
                            //没有有上周的周报 并且没有选择图片
                            addWeekly("");
                        }else {
                            //更新上周信息  并且没有选择图片
                            //先添加原先的附件列表
                            String reportFile1 = "";
                            for (int i = 0; i < dataListOne.size(); i++) {
                                reportFile1 += dataListOneName.get(i) + "|" + dataListOne.get(i) + ",";
                            }
                            updateWeekly(reportFile1);
                        }
                    }

                }else if(currentSelect == 1){
                    //本周
                    if(StringUtil.isNullOrEmpty(content_one2.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作成效！");
                        return;
                    }
                    if(content_one2.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(content_two2.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作总结！");
                        return;
                    }
                    if(content_two2.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(content_three2.getText().toString())){
                        showMsg(AddMineMonthActivtiy.this, "请输入工作计划！");
                        return;
                    }
                    if(content_three2.getText().toString().length() > 2000){
                        showMsg(AddMineMonthActivtiy.this, "最多输入2000字！");
                        return;
                    }

                    //没有 添加
                    progressDialog = new CustomProgressDialog(AddMineMonthActivtiy.this, "正在加载中",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    if(dataList2 != null && dataList2.size() > 0){
                        sendFile2();
                    }else {
                        if(bankJobReportTwo == null){
                            //没有附件 且是新增 并且没有附件
                            addWeekly("");
                        }else {
                            //更新本周信息 并且没有选择附件
                            String reportFile2 = "";
                            for (int i = 0; i < dataListTwoL.size(); i++) {
                                reportFile2 += dataListTwoNameL.get(i) + "|" + dataListTwoL.get(i) + ",";
                            }
                            updateWeekly(reportFile2);
                        }
                    }

                }
            }
                break;
            case R.id.btn_left:
            {
                //上周点击
                btn_left.setTextColor(getResources().getColor(R.color.white));
                btn_right.setTextColor(getResources().getColor(R.color.text_color));

                btn_left.setBackground(getResources().getDrawable(R.drawable.tab_left_p));
                btn_right.setBackground(getResources().getDrawable(R.drawable.tap_right));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_right:
            {
                //本周点击
                btn_left.setTextColor(getResources().getColor(R.color.text_color));
                btn_right.setTextColor(getResources().getColor(R.color.white));

                btn_left.setBackground(getResources().getDrawable(R.drawable.tab_left));
                btn_right.setBackground(getResources().getDrawable(R.drawable.tap_right_p));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_attachment:
            {
                //附件点击
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    fileChooserIntent =  new Intent(this, FileChooserActivity.class);
                    startActivityForResult(fileChooserIntent , REQUEST_CODE);
                } else{
                    showMsg(AddMineMonthActivtiy.this, getResources().getString(R.string.sdcard_unmonted_hint));
                }
            }
            break;
            case R.id.content_one:
            {
                if(currentSelect == 0){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "1");
                    intent.putExtra("typeWrite_content", content_one1.getText().toString());
                    startActivityForResult(intent, 1000);
                }else if(currentSelect == 1){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "1");
                    intent.putExtra("typeWrite_content", content_one2.getText().toString());
                    startActivityForResult(intent, 1000);
                }

            }
            break;
            case R.id.content_two:
            {
                if(currentSelect == 0){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "2");
                    intent.putExtra("typeWrite_content", content_two1.getText().toString());
                    startActivityForResult(intent, 1001);
                }else if(currentSelect == 1){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "2");
                    intent.putExtra("typeWrite_content", content_two2.getText().toString());
                    startActivityForResult(intent, 1001);
                }
            }
            break;
            case R.id.content_three:
            {
                if(currentSelect == 0){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "3");
                    intent.putExtra("typeWrite_content", content_three1.getText().toString());
                    startActivityForResult(intent, 1002);
                }else if(currentSelect == 1){
                    Intent intent = new Intent(AddMineMonthActivtiy.this, WriteContentActivity.class);
                    intent.putExtra("typeWrite", "3");
                    intent.putExtra("typeWrite_content", content_three2.getText().toString());
                    startActivityForResult(intent, 1002);
                }
            }
            break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                if(currentSelect == 0){
                    String str = dataListOne.get(position);//要删除的附件的路径
                    int tmpSelect = 0;
                    boolean flagSelect = false;//默认不选中

                    //查询在选择的新文件中 要删除的附件的位置
                    for(int i=0;i<dataList1.size();i++){
                        if(dataList1.get(i).equals(str)){
                            flagSelect = true;
                            tmpSelect = i;
                            break;
                        }
                    }

                    if(flagSelect){
                        //如果是新选中的被删除 先删除选中的文件列表
                        dataList1.remove(tmpSelect);
                        dataListName1.remove(tmpSelect);
                    }

                    int tmpSelect1 = 0;
                    boolean flagSelect1 = false;//默认不选中

                    //查询在选择的新文件中 要删除的附件的位置
                    for(int i=0;i<dataListOneL.size();i++){
                        if(dataListOneL.get(i).equals(str)){
                            flagSelect1 = true;
                            tmpSelect1 = i;
                            break;
                        }
                    }

                    if(flagSelect1){
                        //如果是新选中的被删除 先删除选中的文件列表
                        dataListOneL.remove(tmpSelect1);
                        dataListOneNameL.remove(tmpSelect1);
                    }

                    //删除列表
                    dataListOneName.remove(position);
                    dataListOne.remove(position);
                    adapter1.notifyDataSetChanged();
                }else if(currentSelect == 1){
                    String str = dataListTwo.get(position);//要删除的附件的路径
                    int tmpSelect = 0;
                    boolean flagSelect = false;//默认不选中
                    for(int i=0;i<dataList2.size();i++){
                        if(dataList2.get(i).equals(str)){
                            //新选择的文件被删除了
                            flagSelect = true;
                            tmpSelect = i;
                            break;
                        }
                    }
                    if(flagSelect){
                        //如果是新选中的被删除 先删除选中的文件列表
                        dataList2.remove(tmpSelect);
                        dataListName2.remove(tmpSelect);
                    }

                    int tmpSelect1 = 0;
                    boolean flagSelect1 = false;//默认不选中

                    //查询在选择的新文件中 要删除的附件的位置
                    for(int i=0;i<dataListTwoL.size();i++){
                        if(dataListTwoL.get(i).equals(str)){
                            flagSelect1 = true;
                            tmpSelect1 = i;
                            break;
                        }
                    }

                    if(flagSelect1){
                        //如果是新选中的被删除 先删除选中的文件列表
                        dataListTwoL.remove(tmpSelect1);
                        dataListTwoNameL.remove(tmpSelect1);
                    }

                    //删除列表
                    dataListTwoName.remove(position);
                    dataListTwo.remove(position);
                    adapter2.notifyDataSetChanged();
                }
                break;
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        int one = offset * 1 + bmpW;
//        int two = one * 1;

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
//            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
//            currIndex = arg0;
//            animation.setFillAfter(true);
//            animation.setDuration(300);
//            imageView.startAnimation(animation);
            if (arg0 == 0) {
                btn_left.setTextColor(getResources().getColor(R.color.white));
                btn_right.setTextColor(getResources().getColor(R.color.text_color));

                btn_left.setBackground(getResources().getDrawable(R.drawable.tab_left_p));
                btn_right.setBackground(getResources().getDrawable(R.drawable.tap_right));
                currentSelect = 0;
            }
            if (arg0 == 1) {
                btn_left.setTextColor(getResources().getColor(R.color.text_color));
                btn_right.setTextColor(getResources().getColor(R.color.white));

                btn_left.setBackground(getResources().getDrawable(R.drawable.tab_left));
                btn_right.setBackground(getResources().getDrawable(R.drawable.tap_right_p));
                currentSelect = 1;
            }
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
                    if(currentSelect == 0){
                        content_one1.setText(xStr);
                    }else  if(currentSelect == 1){
                        content_one2.setText(xStr);
                    }
                }
                break;
                case 1001:
                {
                    String xStr = data.getExtras().getString("xontent");
                    if(currentSelect == 0){
                        content_two1.setText(xStr);
                    }else if(currentSelect == 1){
                        content_two2.setText(xStr);
                    }
                }
                break;
                case 1002:
                {
                    String xStr = data.getExtras().getString("xontent");
                    if (currentSelect == 0){
                        content_three1.setText(xStr);
                    }else if(currentSelect == 1){
                        content_three2.setText(xStr);
                    }
                }
                break;
            }
        }

        if(resultCode == RESULT_CANCELED){
//            showMsg(WeeklyDetailActivtiy.this, getResources().getString(R.string.open_file_none));
            return ;
        }

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //获取路径名
            String pptPath = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER);
            String pptName = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER_NAME);
            if(pptPath != null){
                if(currentSelect == 0){
                    dataList1.add(pptPath);
                    dataListName1.add(pptName);

                    dataListOneName.add(pptName);
                    dataListOne.add(pptPath);

                    adapter1.notifyDataSetChanged();
                }else if(currentSelect == 1){
                    dataList2.add(pptPath);
                    dataListName2.add(pptName);

                    dataListTwoName.add(pptName);
                    dataListTwo.add(pptPath);

                    adapter2.notifyDataSetChanged();
                }
            }else {
                showMsg(AddMineMonthActivtiy.this, getResources().getString(R.string.open_file_failed));
            }
        }

    }

    //查询我的月报--上月的
    private void getWeekDateOne() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_DATE_MINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobReportData data = getGson().fromJson(s, BankJobReportData.class);
                                    List<BankJobReport> lists = data.getData();
                                    if(lists != null && lists.size()>0){
                                        //说明有这月的月报  查看月报详细 进行修改
                                        bankJobReportOne = lists.get(0);
                                        initDataViewOne();
                                    }else {
                                        bankJobReportOne = null;
                                        initDataViewOne();
                                    }
                                } else {
                                    Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("weekCount", DateUtil.getYear() +"年"+String.valueOf(currentMoonth-1)+"月份");
                params.put("year", DateUtil.getYear());
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

    //本周的
    private void getWeekDateTwo() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_DATE_MINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankJobReportData data = getGson().fromJson(s, BankJobReportData.class);
                                    List<BankJobReport> lists = data.getData();
                                    if(lists != null && lists.size()>0){
                                        //说明有这周的周报  查看周报详细 进行修改
                                        bankJobReportTwo = lists.get(0);
                                        initDataViewTwo();
                                    }else {
                                        bankJobReportTwo = null;
                                        initDataViewTwo();
                                    }
                                } else {
                                    Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddMineMonthActivtiy.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("weekCount", DateUtil.getYear() +"年"+String.valueOf(currentMoonth)+"月份");
                params.put("year", DateUtil.getYear());
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

    public void sendFile1() {
        fileUrls1.clear();
        fileNames1.clear();
        for (int i = 0; i < dataList1.size(); i++) {
            File f = new File(dataList1.get(i));
            final Map<String, File> files = new HashMap<String, File>();
            files.put("file", f);
            Map<String, String> params = new HashMap<String, String>();
            CommonUtil.addPutUploadFileRequest(
                    this,
                    InternetURL.UPLOAD_FILE,
                    files,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (StringUtil.isJson(s)) {
                                try {
                                    JSONObject jo = new JSONObject(s);
                                    String code = jo.getString("code");
                                    if (Integer.parseInt(code) == 200) {
                                        fileUrls1.add(jo.getString("data"));
                                        fileNames1.add(jo.getString("fileName"));
                                        if (fileUrls1.size() == dataList1.size()) {
                                            dataList1.clear();
                                            dataListName1.clear();
                                            if (fileUrls1 != null && fileUrls1.size() > 0) {
                                                //先添加原先的附件列表
                                                String reportFile1 = "";
                                                for (int i = 0; i < dataListOneL.size(); i++) {
                                                    reportFile1 += dataListOneNameL.get(i) + "|" + dataListOneL.get(i) + ",";
                                                }
                                                //再添加新的附件列表
                                                for (int i = 0; i < fileUrls1.size(); i++) {
                                                    reportFile1 += fileNames1.get(i) + "|" + fileUrls1.get(i) + ",";
                                                }
                                                if(currentSelect == 0){
                                                    if(bankJobReportOne == null){
                                                        addWeekly(reportFile1);
                                                    }else {
                                                        //更新 上周
                                                        updateWeekly(reportFile1);
                                                    }
                                                }else if(currentSelect == 1){
                                                    if(bankJobReportTwo == null){
                                                        addWeekly(reportFile1);
                                                    }else {
                                                        //更新 本周周
                                                        updateWeekly(reportFile1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    null);
        }
    }
    public void sendFile2() {
        fileUrls2.clear();
        fileNames2.clear();
        for (int i = 0; i < dataList2.size(); i++) {
            File f = new File(dataList2.get(i));
            final Map<String, File> files = new HashMap<String, File>();
            files.put("file", f);
            Map<String, String> params = new HashMap<String, String>();
            CommonUtil.addPutUploadFileRequest(
                    this,
                    InternetURL.UPLOAD_FILE,
                    files,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (StringUtil.isJson(s)) {
                                try {
                                    JSONObject jo = new JSONObject(s);
                                    String code = jo.getString("code");
                                    if (Integer.parseInt(code) == 200) {
                                        fileUrls2.add(jo.getString("data"));
                                        fileNames2.add(jo.getString("fileName"));
                                        if (fileUrls2.size() == dataList2.size()) {
                                            dataList2.clear();
                                            dataListName2.clear();
                                            if (fileUrls2 != null && fileUrls2.size() > 0) {
                                                //先添加原先的附件列表
                                                String reportFile2 = "";
                                                for (int i = 0; i < dataListTwoL.size(); i++) {
                                                    reportFile2 += dataListTwoNameL.get(i) + "|" + dataListTwoL.get(i) + ",";
                                                }
                                                for (int i = 0; i < fileUrls2.size(); i++) {
                                                    reportFile2 += fileNames2.get(i) + "|" + fileUrls2.get(i) + ",";
                                                }
                                                if(currentSelect == 0){
                                                    if(bankJobReportOne == null){
                                                        addWeekly(reportFile2);
                                                    }else {
                                                        //更新 上周
                                                        updateWeekly(reportFile2);
                                                    }
                                                }else if(currentSelect == 1){
                                                    if(bankJobReportTwo == null){
                                                        addWeekly(reportFile2);
                                                    }else {
                                                        //更新 本周周
                                                        updateWeekly(reportFile2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    null);
        }
    }

    private void addWeekly(final String filenames) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_SAVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    BankJobReport bankJobReportCommentBean = data.getData();
                                    Intent intent1 = new Intent("add_month_success");
                                    sendBroadcast(intent1);
//                                    跳转到周报详情页面
                                    Intent intent = new Intent(AddMineMonthActivtiy.this, MoonthDetailActivtiy.class);
                                    intent.putExtra("bankJobReport", bankJobReportCommentBean);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    showMsg(AddMineMonthActivtiy.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddMineMonthActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddMineMonthActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                if(currentSelect == 0){
                    //上月
                    params.put("reportTitle", content_one1.getText().toString());
                    params.put("reportCont", content_two1.getText().toString());
                    params.put("reportNext", content_three1.getText().toString());
                    params.put("reportNumber", dateline1.getText().toString());
                    params.put("dateLine", DateUtil.getYear()+"-"+(currentMoonth - 1)+"-"+DateUtil.getPreLastMonthDay() + " 00:00:00.000");
                    params.put("dateStartEnd", week1.getText().toString());
                }else {
                    //本月
                    params.put("reportTitle", content_one2.getText().toString());
                    params.put("reportCont", content_two2.getText().toString());
                    params.put("reportNext", content_three2.getText().toString());
                    params.put("reportNumber", dateline2.getText().toString());
                    params.put("dateLine", DateUtil.getYear()+"-"+(currentMoonth )+"-"+DateUtil.getLastMonthDay() + " 00:00:00.000");
                    params.put("dateStartEnd", week2.getText().toString());
                }

                params.put("reportType", "3");
                params.put("isUse", "0");
                params.put("status", "1");

                if(!StringUtil.isNullOrEmpty(filenames)){
                    params.put("reportFile", filenames);
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

    private void updateWeekly(final String filenames) {
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
                                    if(currentSelect == 0){
                                        bankJobReportOne = data.getData();
                                    }else if(currentSelect == 1){
                                        bankJobReportTwo = data.getData();
                                    }
                                    showMsg(AddMineMonthActivtiy.this, "修改操作成功");
                                    Intent intent1 = new Intent("add_month_success");
                                    sendBroadcast(intent1);
//                                    跳转到周报详情页面
                                    Intent intent = new Intent(AddMineMonthActivtiy.this, MoonthDetailActivtiy.class);
                                    intent.putExtra("bankJobReport", data.getData());
                                    startActivity(intent);
                                    finish();
                                }else {
                                    showMsg(AddMineMonthActivtiy.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddMineMonthActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddMineMonthActivtiy.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));

                if(currentSelect == 0){
                    //上周
                    params.put("reportId", bankJobReportOne.getReportId());
                    params.put("reportTitle", content_one1.getText().toString());
                    params.put("reportCont", content_two1.getText().toString());
                    params.put("reportNext", content_three1.getText().toString());
                    params.put("reportNumber", dateline1.getText().toString());
                    params.put("dateLine", bankJobReportOne.getDateLine());
                    params.put("dateStartEnd", week1.getText().toString());
                }else {
                    //本周
                    params.put("reportId", bankJobReportTwo.getReportId());
                    params.put("reportTitle", content_one2.getText().toString());
                    params.put("reportCont", content_two2.getText().toString());
                    params.put("reportNext", content_three2.getText().toString());
                    params.put("reportNumber", dateline2.getText().toString());
                    params.put("dateLine", bankJobReportTwo.getDateLine());
                    params.put("dateStartEnd", week2.getText().toString());
                }

                params.put("reportType", "3");
                params.put("isUse", "0");
                params.put("status", "1");

                if(!StringUtil.isNullOrEmpty(filenames)){
                    params.put("reportFile", filenames);
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


}
