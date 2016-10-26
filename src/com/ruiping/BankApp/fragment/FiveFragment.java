package com.ruiping.BankApp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.adapter.ItemIndexAdapter;
import com.ruiping.BankApp.base.BaseFragment;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.IndexCountObjData;
import com.ruiping.BankApp.entiy.IndexCountObj;
import com.ruiping.BankApp.entiy.IndexObj;
import com.ruiping.BankApp.ui.*;
import com.ruiping.BankApp.upload.CommonUtil;
import com.ruiping.BankApp.util.CompressPhotoUtil;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.PictureGridview;
import com.ruiping.BankApp.widget.SelectPhoPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class FiveFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Resources res;
    private TextView title;
    private TextView right_btn;
    private PictureGridview gridView;
    private ItemIndexAdapter adapter;
    private List<IndexObj> lists = new ArrayList<IndexObj>();

    private ImageView head;
    private TextView nickname;
    private TextView groupName;
    private TextView upEmpName;
    private TextView email;
    private TextView mobile;
    private TextView status;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    IndexCountObj indexCountObj;

    private String txpic = "";
    private SelectPhoPopWindow deleteWindow;
    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.five_fragment, null);
        res = getActivity().getResources();
        initView();
        initData();//初始化用户个人信息
        //查询主页面统计数据
        getCountTj();
        return view;
    }

    private void initData() {
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_COVER, ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString(Contance.EMP_COVER, ""), String.class), head, animateFirstListener);
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class))){
            nickname.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.GROUP_NAME, ""), String.class))){
            groupName.setText(getGson().fromJson(getSp().getString(Contance.GROUP_NAME, ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_NAME_UP, ""), String.class))){
            upEmpName.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME_UP, ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class))){
            email.setText(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_IS_USE, ""), String.class))){
            switch (Integer.parseInt(getGson().fromJson(getSp().getString(Contance.EMP_IS_USE, ""), String.class))){
                case 0:
                    status.setText("在职");
                    break;
                case 1:
                    status.setText("禁用");
                    break;
                case 2:
                    status.setText("离职");
                    break;
            }
        }
    }

    private void initView() {
        head = (ImageView) view.findViewById(R.id.head);
        head.setOnClickListener(this);
        nickname = (TextView) view.findViewById(R.id.nickname);
        groupName = (TextView) view.findViewById(R.id.groupName);
        upEmpName = (TextView) view.findViewById(R.id.upEmpName);
        email = (TextView) view.findViewById(R.id.email);
        mobile = (TextView) view.findViewById(R.id.mobile);
        status = (TextView) view.findViewById(R.id.status);

        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.relate_set).setOnClickListener(this);
        view.findViewById(R.id.relate_emial).setOnClickListener(this);
        view.findViewById(R.id.relate_mobile).setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.title);
        right_btn = (TextView) view.findViewById(R.id.right_btn);
        title.setText("个人主页");
        right_btn.setText("编辑");
        right_btn.setOnClickListener(this);
        gridView = (PictureGridview) view.findViewById(R.id.gridView);

        adapter = new ItemIndexAdapter(lists, getActivity());
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //任务
                    {
                        Intent intent = new Intent(getActivity(), RenwuListActivity.class);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                    }
                        break;
                    case 1:
                        //日报
                    {
                        Intent intent = new Intent(getActivity(), DailyListActivity.class);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                    }
                        break;
                    case 2:
                        //备忘录
                    {
                        Intent intent = new Intent(getActivity(), MemoActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 3:
                        //周报
                    {
                        Intent intent = new Intent(getActivity(), WeeklyActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 4:
                    {
                        //月报
                        Intent intent = new Intent(getActivity(), MonthActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 5:
                        //季报
                    {
                        Intent intent = new Intent(getActivity(), QuarterActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 6:
                        //年中
                    {
                        Intent intent = new Intent(getActivity(), YearMiddleActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 7:
                    {
                        //年报
                        Intent intent = new Intent(getActivity(), YearActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 8:
                        //办公会
                    {
                        Intent intent = new Intent(getActivity(), BangonghuiActivity.class);
                        startActivity(intent);
                    }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_btn:
            {
                //编辑
                Intent mineV = new Intent(getActivity(), MineProfileActivity.class);
                startActivity(mineV);
            }
                break;
            case R.id.relate_set:
                //设置
            {
                Intent intent = new Intent(getActivity(), MineSetActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.head:
            {
                ShowPickDialog();
            }
                break;
            case R.id.relate_emial:
            {
                //修改邮箱
                Intent intent  = new Intent(getActivity(), UpdateEmailActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_mobile:
            {
                //修改手机号
                Intent intent  = new Intent(getActivity(), UpdateMobileActivity.class);
                startActivity(intent);
            }
                break;
        }
    }
    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(getActivity(), itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                head.setImageBitmap(photo);
                //保存头像
                sendFile(pics);
            }
        }
    }




    //查询主页面统计数据
    private void getCountTj() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_INDEX_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            IndexCountObjData data = getGson().fromJson(s, IndexCountObjData.class);
                            if (data.getCode() == 200) {
                                indexCountObj = data.getData();
                                initCount();
                            } else {
                                Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    //填充统计的数据
    void initCount(){
        lists.clear();
        lists.add(new IndexObj("任务", R.drawable.md_h_blue, indexCountObj.getKey8()));
        lists.add(new IndexObj("日报", R.drawable.md_h_green, indexCountObj.getKey1()));
        lists.add(new IndexObj("备忘录", R.drawable.md_h_purple, indexCountObj.getKey7()));
        lists.add(new IndexObj("周报", R.drawable.md_h_orange, indexCountObj.getKey2()));
        lists.add(new IndexObj("月报", R.drawable.md_h_orange, indexCountObj.getKey3()));
        lists.add(new IndexObj("季报", R.drawable.md_h_brown, indexCountObj.getKey4()));
        lists.add(new IndexObj("年中", R.drawable.md_h_blue_dark, indexCountObj.getKey5()));
        lists.add(new IndexObj("年报", R.drawable.md_h_red, indexCountObj.getKey6()));
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.IS_MEETING, ""), String.class))){
            if("2".equals(getGson().fromJson(getSp().getString(Contance.IS_MEETING, ""), String.class))){
                //允许
                lists.add(new IndexObj("办公会", R.drawable.md_h_red, indexCountObj.getKey9()));
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void sendFile(final String pic) {
            File f = new File(pic);
            final Map<String, File> files = new HashMap<String, File>();
            files.put("file", f);
            Map<String, String> params = new HashMap<String, String>();
            CommonUtil.addPutUploadFileRequest(
                    getActivity(),
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
                                        String coverStr = jo.getString("data");
                                        sendCover(coverStr);
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


    //修改头像
    private void sendCover(final String coverStr) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_COVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    //调用广播
//                                    Intent intent1 = new Intent("update_cover_success");
//                                    getActivity().sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("empCover", coverStr);
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
            if (action.equals("update_email_success")) {
                //更新邮箱
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class))){
                    email.setText(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class));
                }
            }
            if (action.equals("update_mobile_success")) {
                //更新手机号
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class))){
                    mobile.setText(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
                }

            }
            if(action.equals("add_memo_success")){
                //查询主页面统计信息
                getCountTj();
            }
            if(action.equals("add_year_success")){
                //查询主页面统计信息
                getCountTj();
            }
            if(action.equals("add_month_success")){
                //查询主页面统计信息
                getCountTj();
            }
            if(action.equals("update_weekly_success")){
                //查询主页面统计信息
                getCountTj();
            }
            if(action.equals("add_quarter_success")){
                //查询主页面统计信息
                getCountTj();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_email_success");
        myIntentFilter.addAction("update_mobile_success");
        myIntentFilter.addAction("add_memo_success");
        myIntentFilter.addAction("add_year_success");
        myIntentFilter.addAction("add_month_success");
        myIntentFilter.addAction("update_weekly_success");
        myIntentFilter.addAction("add_quarter_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
