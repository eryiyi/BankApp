package com.ruiping.BankApp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemSelectFileAdapter;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankJobReportSingleData;
import com.ruiping.BankApp.entiy.BankJobReport;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.*;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.SelectPhotoPopFileWindow;
import com.ruiping.BankApp.widget.SelectPhotoPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/2.
 */
public class AddDailyActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private TextView back;
    private TextView right_btn;
    private EditText content;

    private TextView title;

    private ArrayList<String> dataList = new ArrayList<String>();//选择文件  手机的路径
    private ArrayList<String> dataListName = new ArrayList<String>();//选择文件的文件名  手机的路径

    private Uri uri;
    private final static int SELECT_LOCAL_PHOTO = 110;

    private List<String> fileUrls = new ArrayList<String>();//上传文件返回的保存路径
    private List<String> fileNames = new ArrayList<String>();//上传文件返回的文件名
    private Intent fileChooserIntent ;

    private static final int REQUEST_CODE = 1;   //请求码

    private ListView lstv;
    private ItemSelectFileAdapter adapter;

    private String reportFile = "";

    private BankJobReport bankJobReport;//日报信息

    String tmpDate = "";//要填加的日报日期格式2017-01-01

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_daily_activity);
        tmpDate = getIntent().getExtras().getString("tmpDate");
        initView();
    }

    private void initView() {
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);
        title.setText("发布日报");
        right_btn.setText("保存");
        right_btn.setOnClickListener(this);
        content = (EditText) this.findViewById(R.id.content);
        this.findViewById(R.id.addFile).setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemSelectFileAdapter(dataListName, AddDailyActivity.this);
        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //保存
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(AddDailyActivity.this, "请填写日报内容！");
                    return;
                }
                if(content.getText().toString().length() > 1000){
                    showMsg(AddDailyActivity.this, "日报内容1000字以内！");
                    return;
                }
                progressDialog = new CustomProgressDialog(AddDailyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                if(dataList != null && dataList.size() > 0){
                    sendFile();
                }else {
                    addDaily();
                }
                break;
            case R.id.addFile:
            {

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
        selectPhotoPopWindow = new SelectPhotoPopWindow(AddDailyActivity.this, itemsOnClick);
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
        selectPhotoPopFileWindow = new SelectPhotoPopFileWindow(AddDailyActivity.this, itemsOnClick);
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
        WindowManager.LayoutParams lp = ((Activity) AddDailyActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) AddDailyActivity.this).getWindow().setAttributes(lp);
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
                    fileChooserIntent =  new Intent(AddDailyActivity.this, FileChooserActivity.class);
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
                    Intent intent = new Intent(AddDailyActivity.this, AlbumActivity.class);
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

    public void onActivityResult(int requestCode , int resultCode , Intent data){

        if(resultCode == RESULT_CANCELED){
            showMsg(AddDailyActivity.this, getResources().getString(R.string.open_file_none));
            return ;
        }
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //获取路径名
            String pptPath = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER);
            String pptName = data.getStringExtra(Contance.EXTRA_FILE_CHOOSER_NAME);
            if(pptPath != null){
                dataList.add(pptPath);
//                dataListName.add(pptName);
                dataListName.add(pptPath);
                adapter.notifyDataSetChanged();
            }else {
                showMsg(AddDailyActivity.this, getResources().getString(R.string.open_file_failed));
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
                        adapter.notifyDataSetChanged();
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
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap,fileName + ".jpg");

                    dataList.add(cameraImagePath);
                    dataListName.add(fileName);
                    adapter.notifyDataSetChanged();
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
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    dataListName.remove(position);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void addDaily() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REPORT_SAVE_URL,
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
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent("add_report_daily_success");
                                    intent1.putExtra("bankJobReportCommentBean", bankJobReportCommentBean);
                                    sendBroadcast(intent1);
                                    finish();
                                }else {
                                    showMsg(AddDailyActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddDailyActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddDailyActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("reportTitle", content.getText().toString());
                params.put("reportCont", content.getText().toString());
                params.put("reportType", "1");
                params.put("dateLine",  tmpDate +" "+DateUtil.getHms());
                params.put("isUse", "0");
                params.put("status", "1");
                if(!StringUtil.isNullOrEmpty(reportFile)){
                    params.put("reportFile", reportFile);
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


    public void sendFile() {
        for (int i = 0; i < dataList.size(); i++) {
            File f = new File(dataList.get(i));
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
                                        fileUrls.add(jo.getString("data"));
                                        fileNames.add(jo.getString("fileName"));
                                        if(fileUrls.size() == dataList.size()){
                                            if(fileUrls != null && fileUrls.size() > 0){
                                                for(int i=0;i<fileUrls.size();i++){
                                                    reportFile +=fileNames.get(i)+"|" +fileUrls.get(i)+",";
                                                }
                                                addDaily();
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
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                        }
                    },
                    null);
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                dataList.remove(position);
                dataListName.remove(position);
                adapter.notifyDataSetChanged();
                break;
        }
    }


}
