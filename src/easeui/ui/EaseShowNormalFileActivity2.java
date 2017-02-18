package easeui.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.util.FileUtils;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.InternetURL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EaseShowNormalFileActivity2 extends EaseBaseActivity {
	private ProgressBar progressBar;
	private File file;
    String filePath = "";
    String filePath_local = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ease_activity_show_file);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

//		final EMFileMessageBody messageBody = getIntent().getParcelableExtra("msgbody");
        filePath = getIntent().getExtras().getString("filePath");
        filePath_local = getIntent().getExtras().getString("filePath_local");
//        filePath = InternetURL.INTERNAL+filePath;
//        filePath = "http://192.168.0.188:8080/Manage_ssm/upload/20170203/1486111082599.png";
		file = new File(filePath_local);
		//set head map
		final Map<String, String> maps = new HashMap<String, String>();
//		if (!TextUtils.isEmpty(messageBody.getSecret())) {
//			maps.put("share-secret", messageBody.getSecret());
//		}
		
		//download file
		EMClient.getInstance().chatManager().downloadFile(filePath, filePath_local, maps,
                new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                FileUtils.openFile(file, EaseShowNormalFileActivity2.this);
                                finish();
                            }
                        });
                    }
                    
                    @Override
                    public void onProgress(final int progress,String status) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progress);
                            }
                        });
                    }
                    
                    @Override
                    public void onError(int error, final String msg) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(file != null && file.exists()&&file.isFile())
                                    file.delete();
                                String str4 = getResources().getString(R.string.Failed_to_download_file);
                                Toast.makeText(EaseShowNormalFileActivity2.this, str4+msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
		
	}
}
