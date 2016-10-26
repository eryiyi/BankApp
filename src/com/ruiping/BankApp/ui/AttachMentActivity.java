package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.*;
import android.text.TextUtils;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.FileUtils;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemAttachMentAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.AttachMentObj;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.HttpDownloader;
import com.ruiping.BankApp.util.OpenFilesMine;
import com.ruiping.BankApp.util.StringUtil;
import easeui.ui.EaseShowNormalFileActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AttachMentActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener,Runnable{
    private ListView lstv;
    private TextView title;
    private TextView right_btn;
    private ItemAttachMentAdapter adapter;
    private List<AttachMentObj> lists = new ArrayList<AttachMentObj>();

    private String attach_file = "";//附件列表
    private String reportId;//报表iD
    private String empId;//用户iD

    public static Boolean flag = false;//true 当前登录用户自己的日报或周报   false  看别人的

    private String fileUrls = "";//上传文件返回的保存路径
    private String fileNames ="";//上传文件返回的文件名
    private Intent fileChooserIntent ;
    private static final int REQUEST_CODE = 1;   //请求码

    private String dataList = "";//选择文件  手机的路径
    private String dataListName = "";//选择文件的文件名  手机的路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attach_ment_activity);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        attach_file = getIntent().getExtras().getString("attach_file");
        reportId = getIntent().getExtras().getString("reportId");
        empId = getIntent().getExtras().getString("empId");
        if(!StringUtil.isNullOrEmpty(empId)){
            if(empId.equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                flag = true;
            }else{
                flag = false;
            }
        }

        if(!StringUtil.isNullOrEmpty(attach_file)){
            //如果存在附件
            String[] arrs = attach_file.split(",");
            if(arrs != null){
                for(String str:arrs){
                    String[] strA = str.split("\\|");
                    if(strA != null && strA.length == 2){
                        lists.add(new AttachMentObj(strA[0], strA[1]));
                    }
                }
            }
        }
        initView();
    }


    private void initView() {
        lstv = (ListView) this.findViewById(R.id.lstv);
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("附件列表");
        if(flag){
            //自己
            right_btn.setText("点此添加附件");
            right_btn.setVisibility(View.VISIBLE);
            right_btn.setOnClickListener(this);
        }else{
            right_btn.setVisibility(View.GONE);
        }

        adapter = new ItemAttachMentAdapter(lists, AttachMentActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //todo
                if(lists.size() > i){
                    AttachMentObj attachMentObj = lists.get(i);
                    if(attachMentObj != null) {
                        if (!StringUtil.isNullOrEmpty(attachMentObj.getUrlStr())) {
                            HttpDownloader httpDownLoader=new HttpDownloader();
                            int result=httpDownLoader.downfile(InternetURL.INTERNAL + attachMentObj.getUrlStr(), "test/", attachMentObj.getTitle());
                            if(result==0)
                            {
                                openFIle(attachMentObj.getTitle(), "test/");
                                Toast.makeText(AttachMentActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
                            }
                            else if(result==1) {
                                openFIle("notifications.txt", "test/notifications.txt");
                                Toast.makeText(AttachMentActivity.this, "已有文件！", Toast.LENGTH_SHORT).show();
                            }
                            else if(result==-1){
                                Toast.makeText(AttachMentActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showMsg(AttachMentActivity.this, "对不起，暂无文件");
                        }
                    }
                    }
                }
        });
    }

    void openFIle(String fileName, String filepath){
        File currentPath = null;
        currentPath = new File(filepath);
        Intent intent;
                            if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingImage))) {
                                intent = OpenFilesMine.getImageFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingWebText))) {
                                intent = OpenFilesMine.getHtmlFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingPackage))) {
                                intent = OpenFilesMine.getApkFileIntent(currentPath);
                                startActivity(intent);

                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingAudio))) {
                                intent = OpenFilesMine.getAudioFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingVideo))) {
                                intent = OpenFilesMine.getVideoFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingText))) {
                                intent = OpenFilesMine.getTextFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingPdf))) {
                                intent = OpenFilesMine.getPdfFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingWord))) {
                                intent = OpenFilesMine.getWordFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingExcel))) {
                                intent = OpenFilesMine.getExcelFileIntent(currentPath);
                                startActivity(intent);
                            } else if (StringUtil.checkEndsWithInStringArray(fileName, getResources().
                                    getStringArray(R.array.fileEndingPPT))) {
                                intent = OpenFilesMine.getPPTFileIntent(currentPath);
                                startActivity(intent);
                            } else {
                                showMsg(AttachMentActivity.this, "无法打开，请安装相应的软件！");
                            }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //添加附件
            {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    fileChooserIntent =  new Intent(this, FileChooserActivity.class);
                    startActivityForResult(fileChooserIntent , REQUEST_CODE);
                } else{
                    showMsg(AttachMentActivity.this, getResources().getString(R.string.sdcard_unmonted_hint));
                }
            }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
//            showMsg(WeeklyDetailActivtiy.this, getResources().getString(R.string.open_file_none));
            return ;
        }

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //获取路径名
            String pptPath = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER);
            String pptName = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER_NAME);
            if(pptPath != null){
                dataList = pptPath;
                dataListName = pptName;
                //上传
                new Thread(AttachMentActivity.this).start();
                lists.add(new AttachMentObj(pptName, pptPath));
                adapter.notifyDataSetChanged();
            }else {
                showMsg(AttachMentActivity.this, getResources().getString(R.string.open_file_failed));
            }
        }
    }

    public void sendFile() {
        fileUrls = "";
        fileNames = "";
            File f = new File(dataList);
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
                                        fileUrls = jo.getString("data");
                                        fileNames = jo.getString("fileName");
                                        attach_file += fileNames + "|" + fileUrls + ",";

                                        updateWeekly();
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

    private void updateWeekly() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.WEEK_UPDATE_FILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
//                                    showMsg(AttachMentActivity.this, getResources().getString(R.string.caozuo_success));
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    //调用广播，刷新
                                    Intent intent1 = new Intent("update_report_file_success");
                                    intent1.putExtra("bankJobReport", data.getData());
                                    sendBroadcast(intent1);
                                }else {
                                    showMsg(AttachMentActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AttachMentActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AttachMentActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reportId", reportId);
                if(!StringUtil.isNullOrEmpty(attach_file)){
                    params.put("reportFile", attach_file);
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
        switch (flag){
            case 1:
                AttachMentObj attachMentObj = lists.get(position);
                //调用删除方法
                String attach_file_url_str =  attachMentObj.getUrlStr();
                if(!StringUtil.isNullOrEmpty(attach_file)){
                    String[] arras = attach_file.split(",");
                    int tmpI = 0;
                    for(int i=0;i<arras.length;i++){
                        String str = arras[i];
                        if(str.indexOf(attach_file_url_str) != -1){
                            //说明这个项目要被删除
                            tmpI = i;
                            break;
                        }
                    }
                    String strTmp = "";
                    for(int i=0;i<arras.length;i++){
                        if(i != tmpI){
                            strTmp += arras[i]+",";
                        }
                    }
                    attach_file = strTmp;
                }
                updateWeekly();
                lists.remove(position);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void run() {
        sendFile();
    }
}
