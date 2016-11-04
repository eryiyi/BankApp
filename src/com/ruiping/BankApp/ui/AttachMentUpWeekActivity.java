package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.util.FileUtils;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemAttachMentAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.entiy.AttachMentObj;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.HttpDownloader;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AttachMentUpWeekActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener,Runnable{
    private ListView lstv;
    private TextView title;
    private TextView right_btn;
    private ItemAttachMentAdapter adapter;
    private List<AttachMentObj> lists = new ArrayList<AttachMentObj>();

    private String fileUrls = "";//上传文件返回的保存路径
    private String fileNames ="";//上传文件返回的文件名
    private Intent fileChooserIntent ;
    private static final int REQUEST_CODE = 1;   //请求码

    private String dataList = "";//选择文件  手机的路径
    private String dataListName = "";//选择文件的文件名  手机的路径

    private String attach_file = "";//附件上传之后的拼接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attach_ment_up_activity);
        attach_file  = getGson().fromJson(getSp().getString("reportFile_up", ""), String.class);
        initView();
    }

    private void initView() {
        lstv = (ListView) this.findViewById(R.id.lstv);
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("附件列表");
          //自己
        right_btn.setText("点此添加附件");
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setOnClickListener(this);

        adapter = new ItemAttachMentAdapter(lists, AttachMentUpWeekActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(lists.size() > i){
                   final  AttachMentObj attachMentObj = lists.get(i);
                    if(attachMentObj != null) {
                        if (!StringUtil.isNullOrEmpty(attachMentObj.getUrlStr())) {
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    HttpDownloader httpDownLoader=new HttpDownloader();
                                    int result=httpDownLoader.downfile(InternetURL.INTERNAL + attachMentObj.getUrlStr(), InternetURL.DOWNLOAD_FILE_URL, attachMentObj.getTitle());
                                    if(result==0)
                                    {
                                        File file = new File(InternetURL.OPEN_FILE_URL + attachMentObj.getTitle());
                                        FileUtils.openFile(file, AttachMentUpWeekActivity.this);
                                    }
                                    else if(result==1) {
                                        File file = new File(InternetURL.OPEN_FILE_URL + attachMentObj.getTitle());
                                        FileUtils.openFile(file, AttachMentUpWeekActivity.this);
                                    }
                                    else if(result==-1){
                                    }
                                }
                            }).start();
                        } else {
                            showMsg(AttachMentUpWeekActivity.this, "对不起，暂无文件");
                        }
                    }
                }
            }
        });


    }

    public void saveAction(View view){
        //保存

        Intent intent = new Intent();
        intent.putExtra("attach_file", attach_file);
        setResult(RESULT_OK, intent);
        save("reportFile_up", attach_file);
        finish();
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
                    showMsg(AttachMentUpWeekActivity.this, getResources().getString(R.string.sdcard_unmonted_hint));
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
                new Thread(AttachMentUpWeekActivity.this).start();
                lists.add(new AttachMentObj(pptName, pptPath));
                adapter.notifyDataSetChanged();
            }else {
                showMsg(AttachMentUpWeekActivity.this, getResources().getString(R.string.open_file_failed));
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
