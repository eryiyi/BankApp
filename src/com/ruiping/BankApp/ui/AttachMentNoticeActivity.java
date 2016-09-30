package com.ruiping.BankApp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.hyphenate.util.FileUtils;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.ItemAttachNoticeAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.entiy.AttachMentObj;
import com.ruiping.BankApp.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 */
public class AttachMentNoticeActivity extends BaseActivity implements View.OnClickListener{
    private ListView lstv;
    private TextView title;
    private TextView right_btn;
    private ItemAttachNoticeAdapter adapter;
    private List<AttachMentObj> lists = new ArrayList<AttachMentObj>();

    private String attach_file = "";//附件列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attach_ment_activity);
        attach_file = getIntent().getExtras().getString("attach_file");

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

        right_btn.setVisibility(View.GONE);

        adapter = new ItemAttachNoticeAdapter(lists, AttachMentNoticeActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //todo
                if(lists.size() > i){
                    AttachMentObj attachMentObj = lists.get(i);
                    if(attachMentObj != null){
                        String filePath = attachMentObj.getUrlStr();
                        File file = new File(filePath);
                        if (file.exists()) {
                            // open files if it exist
                            FileUtils.openFile(file, AttachMentNoticeActivity.this);
                        } else {
                            // download the file
//                            startActivity(new Intent(AttachMentActivity.this, EaseShowNormalFileActivity.class).putExtra("msgbody", message.getBody()));
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
        }
    }

}
