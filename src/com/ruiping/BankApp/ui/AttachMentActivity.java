package com.ruiping.BankApp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.util.FileUtils;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemAttachMentAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.AttachMentObj;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.CommonDefine;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.ImageUtils;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.SelectPhotoPopFileWindow;
import com.ruiping.BankApp.widget.SelectPhotoPopWindow;
import easeui.ui.EaseShowNormalFileActivity2;
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
public class AttachMentActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
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

//    private String dataList = "";//选择文件  手机的路径
//    private String dataListName = "";//选择文件的文件名  手机的路径
    private ArrayList<String> dataList = new ArrayList<String>();//选择文件  手机的路径
    private ArrayList<String> dataListName = new ArrayList<String>();//选择文件的文件名  手机的路径
    
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
                if(lists.size() > i){
                    final AttachMentObj attachMentObj = lists.get(i);
                        if(attachMentObj != null) {
                            if (!StringUtil.isNullOrEmpty(attachMentObj.getUrlStr())) {
                                if (!StringUtil.isNullOrEmpty(attachMentObj.getUrlStr())) {
                                    String local_url = InternetURL.OPEN_FILE_URL + attachMentObj.getTitle();
                                    File fileLocal = new File(local_url);
                                    if (fileLocal.exists()) {
                                        FileUtils.openFile(fileLocal,AttachMentActivity.this);
                                    } else {
                                        Intent intent = new Intent(AttachMentActivity.this, EaseShowNormalFileActivity2.class);
                                        intent.putExtra("filePath", InternetURL.INTERNAL + attachMentObj.getUrlStr());
                                        intent.putExtra("filePath_local", local_url);
                                        startActivity(intent);
                                    }
                                } else {
                                    showMsg(AttachMentActivity.this, "对不起，暂无文件");
                                }
                            } else {
                                showMsg(AttachMentActivity.this, "对不起，暂无文件");
                            }
                        }
                    }
                }
        });
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
//                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//                {
//                    fileChooserIntent =  new Intent(this, FileChooserActivity.class);
//                    startActivityForResult(fileChooserIntent , REQUEST_CODE);
//                } else{
//                    showMsg(AttachMentActivity.this, getResources().getString(R.string.sdcard_unmonted_hint));
//                }
                //添加附件
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    showSelectImageDialogFile();
                } else{
                    showSelectImageDialog();
                }
            }
                break;
        }
    }



    // 选择相册，相机
    SelectPhotoPopWindow selectPhotoPopWindow;
    SelectPhotoPopFileWindow selectPhotoPopFileWindow;

    public void showSelectImageDialog(){
        selectPhotoPopWindow = new SelectPhotoPopWindow(AttachMentActivity.this, itemsOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        selectPhotoPopWindow.setBackgroundDrawable(new BitmapDrawable());
        selectPhotoPopWindow.setFocusable(true);
        selectPhotoPopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectPhotoPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void showSelectImageDialogFile(){
        selectPhotoPopFileWindow = new SelectPhotoPopFileWindow(AttachMentActivity.this, itemsOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        selectPhotoPopFileWindow.setBackgroundDrawable(new BitmapDrawable());
        selectPhotoPopFileWindow.setFocusable(true);
        selectPhotoPopFileWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectPhotoPopFileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }



    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) AttachMentActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) AttachMentActivity.this).getWindow().setAttributes(lp);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if(selectPhotoPopWindow != null){
                selectPhotoPopWindow.dismiss();
            }
            if(selectPhotoPopFileWindow != null){
                selectPhotoPopFileWindow.dismiss();
            }
            switch (v.getId()) {
                case R.id.btn_file:
                {
                    fileChooserIntent =  new Intent(AttachMentActivity.this, FileChooserActivity.class);
                    startActivityForResult(fileChooserIntent , REQUEST_CODE);
                }
                break;
                case R.id.btn_camera: {
                    Intent cameraIntent = new Intent();
                    cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    // 根据文件地址创建文件
                    File file = new File(CommonDefine.FILE_PATH);
                    if (file.exists()) {
                        file.delete();
                    }
                    uri = Uri.fromFile(file);
                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    // 开启系统拍照的Activity
                    startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                }
                break;
                case R.id.btn_photo: {
                    dataList.clear();
                    Intent intent = new Intent(AttachMentActivity.this, AlbumActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                }
                break;
                default:
                    break;
            }
        }

    };


    private ArrayList<String> tDataList = new ArrayList<String>();

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
            if (!s.contains("camera_default")) {
                tDataList.add(s);
            }
        }
        return tDataList;
    }

    private final static int SELECT_LOCAL_PHOTO = 110;
    private Uri uri;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
//            showMsg(WeeklyDetailActivtiy.this, getResources().getString(R.string.open_file_none));
            return ;
        }

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //获取路径名
            dataList.clear();
            dataListName.clear();
            
            String pptPath = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER);
            String pptName = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER_NAME);
            if(pptPath != null){
                dataList.add(pptPath);
                dataListName.add(pptName);
                //上传
                File file = new File(dataList.get(0));
                if (file.length() > 20 * 1024 * 1024) {
                    Toast.makeText(AttachMentActivity.this, R.string.The_file_is_not_greater_than_20_m, Toast.LENGTH_SHORT).show();
                }else {
                    sendFile(dataList.get(0));
                }
            }else {
                showMsg(AttachMentActivity.this, getResources().getString(R.string.open_file_failed));
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_PHOTO:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                            dataListName.add(string);
                        }
                        for(String str:dataList){
                            File file = new File(str);
                            if (file.length() <= 20 * 1024 * 1024) {
                                sendFile(str);
                            }
                        }
                    } else {
                        finish();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String fileName = System.currentTimeMillis()+"";
                    String cameraImagePath = com.ruiping.BankApp.util.FileUtils.saveBitToSD(bitmap, fileName + ".jpg");

                    dataList.add(cameraImagePath);
                    dataListName.add(fileName);
                    for(String str:dataList){
                        File file = new File(str);
                        if (file.length() <= 20 * 1024 * 1024) {
                            sendFile(str);
                        }
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        dataListName.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                            dataListName.add(string);
                        }
                        for(String str:dataList){
                            File file = new File(str);
                            if (file.length() <= 20 * 1024 * 1024) {
                                sendFile(str);
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void sendFile(String filePath) {
        fileUrls = "";
        fileNames = "";
            File f = new File(filePath);
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
                                        if(progressDialog != null){
                                            progressDialog.dismiss();
                                        }
                                        updateWeekly( fileNames,  fileUrls);
                                    }
                                } catch (JSONException e) {
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }
                            }else {
                                if(progressDialog != null){
                                    progressDialog.dismiss();
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

    private void updateWeekly(final String fileNames, final String fileUrls) {
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
                                    lists.add(new AttachMentObj(fileNames, fileUrls));
                                    adapter.notifyDataSetChanged();
                                    BankJobReportSingleData data = getGson().fromJson(s, BankJobReportSingleData.class);
                                    //调用广播，刷新
                                    Intent intent1 = new Intent("update_report_file_success");
                                    intent1.putExtra("bankJobReport", data.getData());
                                    sendBroadcast(intent1);
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                }else {
                                    showMsg(AttachMentActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(progressDialog != null){
                                progressDialog.dismiss();
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
                updateWeeklyDel();
                lists.remove(position);
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void updateWeeklyDel() {
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
                            if(progressDialog != null){
                                progressDialog.dismiss();
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

}
