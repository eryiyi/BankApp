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
 * 任务更新标题
 */
public class TaskWriteContentActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private TextView right_btn;

    private EditText content;//输入框

    private String contentStr;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_content_activity);
        contentStr = getIntent().getExtras().getString("content");
        this.findViewById(R.id.back).setOnClickListener(this);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("保存");
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        content = (EditText) this.findViewById(R.id.content);
        if(!StringUtil.isNullOrEmpty(contentStr)){
            content.setText(contentStr);
        }
        title.setText("任务标题");
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
                    showMsg(TaskWriteContentActivity.this, "请输入内容！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("xontent", content.getText().toString());
                setResult(10001, intent);
                finish();
            }
                break;
        }
    }
}
