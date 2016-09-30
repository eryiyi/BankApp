package com.ruiping.BankApp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.hyphenate.EMCallBack;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.ActivityTack;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.huanxin.DemoHelper;
import com.ruiping.BankApp.util.Contance;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_set_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        this.findViewById(R.id.liner_one).setOnClickListener(this);
        this.findViewById(R.id.liner_two).setOnClickListener(this);
        this.findViewById(R.id.liner_three).setOnClickListener(this);
        this.findViewById(R.id.liner_four).setOnClickListener(this);
        this.findViewById(R.id.liner_notice).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_one:
            {
                //清楚缓存
            }
                break;
            case R.id.liner_two:
            {
                //新版本检查
            }
            break;
            case R.id.liner_three:
            {
                //二维码
                Intent intent  = new Intent(MineSetActivity.this, ErweimaActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_four:
            {
                //关于我们
                Intent intent  = new Intent(MineSetActivity.this, AboutOaActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_notice:
            {
                //通知
                Intent intent = new Intent(MineSetActivity.this, NoticesActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    public void quiteAction(View view){
        //退出
        AlertDialog dialog = new AlertDialog.Builder(MineSetActivity.this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getResources().getString(R.string.sure_quite))
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
    void logout() {
        save(Contance.EMP_PWD, "");
        //调用广播，刷新主页
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        finish();
                        ActivityTack.getInstanse().exit(MineSetActivity.this);
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {
            }
            @Override
            public void onError(int code, String message) {
            }
        });
    }

}
