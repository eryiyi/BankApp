package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import com.ruiping.BankApp.widget.SexRadioGroup;

/**
 * Created by zhl on 2016/7/1.
 */
public class MineProfileActivity extends BaseActivity implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener{
    private TextView title;
    private TextView right_btn;

    //---
    private TextView mobile;//手机号
    private TextView nickname;//昵称
    private TextView email;//邮箱
    private TextView password;//邮箱

    private SexRadioGroup profile_personal_sex;//性别
    private RadioButton button_one;
    private RadioButton button_two;

    private String sex_selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_profile_activity);
        initView();

        initData();
    }

    private void initData() {
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class))){
            nickname.setText(getGson().fromJson(getSp().getString(Contance.EMP_NAME, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class))){
            email.setText(getGson().fromJson(getSp().getString(Contance.EMP_EMAIL, ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString(Contance.EMP_MOBILE, ""), String.class));
        }
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        title.setText("个人主页");
        right_btn.setText("确定");
        right_btn.setOnClickListener(this);
        mobile  = (TextView) this.findViewById(R.id.mobile);
        nickname  = (TextView) this.findViewById(R.id.nickname);
        email  = (TextView) this.findViewById(R.id.email);
        password  = (TextView) this.findViewById(R.id.password);
        profile_personal_sex = (SexRadioGroup) this.findViewById(R.id.segment_text);
        profile_personal_sex.setOnClickListener(this);
        button_one = (RadioButton) this.findViewById(R.id.button_one);
        button_two = (RadioButton) this.findViewById(R.id.button_two);
        this.findViewById(R.id.liner_email).setOnClickListener(this);
        this.findViewById(R.id.liner_pwr).setOnClickListener(this);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == profile_personal_sex) {
            if (checkedId == R.id.button_one) {
                sex_selected = "0";
            } else if (checkedId == R.id.button_two) {
                sex_selected = "1";
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //确定按钮
                break;
            case R.id.liner_email:
            {
                //邮箱
                Intent pwrV = new Intent(MineProfileActivity.this, UpdateEmailActivity.class);
                startActivity(pwrV);
            }
                break;
            case R.id.liner_pwr:
            {
                //密码
                Intent pwrV = new Intent(MineProfileActivity.this, UpdatePwrActivity.class);
                startActivity(pwrV);
            }
                break;
        }
    }
}
