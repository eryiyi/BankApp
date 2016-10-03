package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.ruiping.BankApp.data.BankNoteBeanSingleData;
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.DateUtil;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.CustomProgressDialog;
import com.ruiping.BankApp.widget.DateTimePickDialogUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/9/5.
 */
public class AddMemoActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private EditText content;
    private TextView startDateTime;

    //年月日 时分苗
    String dateline="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_memo_activity);
        initView();
        startDateTime.setText(DateUtil.getNoteDateline());
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);

        title.setText("添加备忘");
        right_btn.setText("保存");
        right_btn.setOnClickListener(this);

        this.findViewById(R.id.liner_dateline).setOnClickListener(this);

        content = (EditText) this.findViewById(R.id.content);
        startDateTime = (TextView) this.findViewById(R.id.dateline);
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
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(AddMemoActivity.this, "请输入备忘内容！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(startDateTime.getText().toString())){
                    showMsg(AddMemoActivity.this, "请选择日期！");
                    return;
                }
                dateline = startDateTime.getText().toString();
                if(!StringUtil.isNullOrEmpty(dateline)){
                    dateline = dateline.replace("年", "-");
                    dateline = dateline.replace("月", "-");
                    dateline = dateline.replace("日", "");
                }
                dateline += ":00";
                progressDialog = new CustomProgressDialog(AddMemoActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                addData();
            }
            break;
            case R.id.liner_dateline:
            {
                //点击时间
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        AddMemoActivity.this, DateUtil.getNoteDateline());
                dateTimePicKDialog.dateTimePicKDialog(startDateTime);
            }
            break;
        }
    }

    private void addData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MOME_NOTE_SAVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    BankNoteBeanSingleData data = getGson().fromJson(s, BankNoteBeanSingleData.class);
                                    BankNoteBean bankNoteBean = data.getData();
                                    if(bankNoteBean != null){
                                        //发送广播  增加一个备忘录
                                        Intent intent1 = new Intent("add_memo_success");
                                        sendBroadcast(intent1);

                                        Intent intent = new Intent(AddMemoActivity.this, MemoDetailActivity.class);
                                        intent.putExtra("bankNoteBeanId", bankNoteBean.getNoteId());
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(AddMemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddMemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddMemoActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                params.put("noteContent", content.getText().toString());
                params.put("noteDate", dateline);
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
