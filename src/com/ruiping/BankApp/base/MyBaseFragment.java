package com.ruiping.BankApp.base;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.widget.CustomProgressDialog;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class MyBaseFragment extends Fragment {
    public CustomProgressDialog progressDialog;

    /**
     * 获取当前Application
     *
     * @return
     */
    public BankAppApplication getMyApp() {
        return (BankAppApplication) getActivity().getApplication();
    }

    //存储sharepreference
    public void save(String key, Object value) {
        getMyApp().getSp().edit()
                .putString(key, getMyApp().getGson().toJson(value))
                .commit();
    }

    /**
     * 获取Volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return getMyApp().getRequestQueue();
    }

    /**
     * 获取Gson
     *
     * @return
     */
    public Gson getGson() {
        return getMyApp().getGson();
    }

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    public SharedPreferences getSp() {
        return getMyApp().getSp();
    }


}
