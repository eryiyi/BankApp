package com.ruiping.BankApp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/3.
 */
public class MemoDetailActivity extends BaseActivity implements View.OnClickListener{
    private TextView back;
    private TextView title;
    private TextView right_btn;

    private BankNoteBean bankNoteBean;//传递过来的备忘录
    private TextView content;
    private TextView dateline;

    boolean flag = false;//true是自己 false是他人的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_detail_activity);
        bankNoteBean = (BankNoteBean) getIntent().getExtras().get("bankNoteBean");
        if(bankNoteBean != null){
            if(bankNoteBean.getEmpId().equals(getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class))){
                flag = true;
            }
        }
        initView();
        initData();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        back = (TextView) this.findViewById(R.id.back);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        back.setOnClickListener(this);

        title.setText("备忘详情");
        right_btn.setVisibility(View.GONE);

        this.findViewById(R.id.liner_dateline).setOnClickListener(this);
        content = (TextView) this.findViewById(R.id.content);
        dateline = (TextView) this.findViewById(R.id.dateline);
        if(flag){
            //是自己
            right_btn.setVisibility(View.VISIBLE);
            right_btn.setText("删除");
            right_btn.setOnClickListener(this);
        }else{
            //他人
            right_btn.setVisibility(View.GONE);
        }
    }

    void initData(){
        content.setText(bankNoteBean.getNoteContent()==null?"":bankNoteBean.getNoteContent());
        dateline.setText(bankNoteBean.getNoteDate());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //删除
                showWindows();
            }
                break;
        }
    }

    // 弹窗删除
    private void showWindows() {
        final Dialog picAddDialog = new Dialog(MemoDetailActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        final TextView msg_content = (TextView) picAddInflate.findViewById(R.id.content);
        msg_content.setText("确定删除？");
        //提交
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteById();
                picAddDialog.dismiss();
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    //删除
    private void deleteById() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MOME_NOTE_DELETE_BY_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    Toast.makeText(MemoDetailActivity.this, R.string.caozuo_success, Toast.LENGTH_SHORT).show();
                                    //调用广播，刷新主页
                                    Intent intent1 = new Intent("delete_note_success");
                                    intent1.putExtra("note_id", bankNoteBean.getNoteId());
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(MemoDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MemoDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MemoDetailActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("noteId", bankNoteBean.getNoteId());
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
