package com.ruiping.BankApp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class ErweimaActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.erweima_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("二维码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
