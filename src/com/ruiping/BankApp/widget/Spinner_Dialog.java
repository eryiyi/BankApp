package com.ruiping.BankApp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.ruiping.BankApp.R;

/**
 * Created by Administrator on 2017/4/30 0030.
 */
public class Spinner_Dialog extends Dialog {
    Context context;

    public Spinner_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    public Spinner_Dialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.spinner_bg);
    }
}
