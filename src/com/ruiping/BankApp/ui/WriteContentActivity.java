package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 */
public class WriteContentActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private String typeWrite = "0";//1 工作成效  2工作总结心得  3 工作计划
    private TextView right_btn;

    private EditText content;//输入框

    private String typeWrite_content;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_content_activity);
        typeWrite = getIntent().getExtras().getString("typeWrite");
        typeWrite_content = getIntent().getExtras().getString("typeWrite_content");
        this.findViewById(R.id.back).setOnClickListener(this);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("保存");
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("工作成效");
        content = (EditText) this.findViewById(R.id.content);
        if(!StringUtil.isNullOrEmpty(typeWrite_content)){
            content.setText(typeWrite_content);
        }

        switch (Integer.parseInt(typeWrite)){
            case 1:
                title.setText("工作成效");
                content.setHint("工作成效");
                break;
            case 2:
                title.setText("工作总结心得");
                content.setHint("工作总结心得");
                break;
            case 3:
                title.setText("工作计划");
                content.setHint("工作计划");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //点击保存
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(WriteContentActivity.this, "请输入内容！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("xontent", content.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
                break;
        }
    }
}
