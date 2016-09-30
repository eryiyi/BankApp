package com.ruiping.BankApp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.FileChooserAdapter;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.util.Contance;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhl on 2016/8/29.
 */
public class FileChooserActivity extends BaseActivity {
    private GridView mGridView;
    private View mBackView;
    private View mBtExit;
    private TextView mTvPath ;

    private String mSdcardRootPath ;  //sdcard 根路径
    private String mLastFilePath ;    //当前显示的路径

    private ArrayList<FileChooserAdapter.FileInfo> mFileLists  ;
    private FileChooserAdapter mAdatper ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filechooser_show);

        mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mBackView = findViewById(R.id.imgBackFolder);
        mBackView.setOnClickListener(mClickListener);
        mBtExit = findViewById(R.id.btExit);
        mBtExit.setOnClickListener(mClickListener);

        mTvPath = (TextView)findViewById(R.id.tvPath);

        mGridView = (GridView)findViewById(R.id.gvFileChooser);
        mGridView.setEmptyView(findViewById(R.id.tvEmptyHint));
        mGridView.setOnItemClickListener(mItemClickListener);
        setGridViewAdapter(mSdcardRootPath);

    }
    //配置适配器
    private void setGridViewAdapter(String filePath) {
        updateFileItems(filePath);
        mAdatper = new FileChooserAdapter(this , mFileLists);
        mGridView.setAdapter(mAdatper);
    }
    //根据路径更新数据，并且通知Adatper数据改变
    private void updateFileItems(String filePath) {
        mLastFilePath = filePath ;
        mTvPath.setText(mLastFilePath);

        if(mFileLists == null)
            mFileLists = new ArrayList<FileChooserAdapter.FileInfo>() ;
        if(!mFileLists.isEmpty())
            mFileLists.clear() ;

        File[] files = folderScan(filePath);
        if(files == null)
            return ;

        for (int i = 0; i < files.length; i++) {
            if(files[i].isHidden())  // 不显示隐藏文件
                continue ;

            String fileAbsolutePath = files[i].getAbsolutePath() ;
            String fileName = files[i].getName();
            boolean isDirectory = false ;
            if (files[i].isDirectory()){
                isDirectory = true ;
            }
            FileChooserAdapter.FileInfo fileInfo = new FileChooserAdapter.FileInfo(fileAbsolutePath , fileName , isDirectory) ;
            mFileLists.add(fileInfo);
        }
        //When first enter , the object of mAdatper don't initialized
        if(mAdatper != null)
            mAdatper.notifyDataSetChanged();  //重新刷新
    }
    //获得当前路径的所有文件
    private File[] folderScan(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }

    private View.OnClickListener mClickListener = new  View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgBackFolder:
                    backProcess();
                    break;
                case R.id.btExit :
                    setResult(RESULT_CANCELED);
                    finish();
                    break ;
                default :
                    break ;
            }
        }
    };

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                long id) {
            FileChooserAdapter.FileInfo fileInfo = (FileChooserAdapter.FileInfo)(((FileChooserAdapter)adapterView.getAdapter()).getItem(position));
            if(fileInfo.isDirectory())   //点击项为文件夹, 显示该文件夹下所有文件
                updateFileItems(fileInfo.getFilePath()) ;
            else {
                Intent intent = new Intent();
                intent.putExtra(Contance.EXTRA_FILE_CHOOSER , fileInfo.getFilePath());
                intent.putExtra(Contance.EXTRA_FILE_CHOOSER_NAME , fileInfo.getFileName());
                setResult(RESULT_OK , intent);
                finish();
            }
//			if(fileInfo.isPPTFile()){  //是ppt文件 ， 则将该路径通知给调用者

//			}
//			else {   //其他文件.....
//				toast(getText(R.string.open_file_error_format));
//			}
        }
    };

    public boolean onKeyDown(int keyCode , KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
                == KeyEvent.KEYCODE_BACK){
            backProcess();
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }
    //返回上一层目录的操作
    public void backProcess(){
        //判断当前路径是不是sdcard路径 ， 如果不是，则返回到上一层。
        if (!mLastFilePath.equals(mSdcardRootPath)) {
            File thisFile = new File(mLastFilePath);
            String parentFilePath = thisFile.getParent();
            updateFileItems(parentFilePath);
        }
        else {   //是sdcard路径 ，直接结束
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    private void toast(CharSequence hint){
        Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
    }

}
