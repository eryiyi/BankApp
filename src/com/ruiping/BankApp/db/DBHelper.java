package com.ruiping.BankApp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by liuzwei on 2015/3/13.
 */
public class DBHelper {
    private static Context mContext;
    private static DBHelper instance;
    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;

    private EmpDao empDao;

    private DBHelper() {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (mContext == null) {
                mContext = context;
            }
            helper = new DaoMaster.DevOpenHelper(context, "guiren_hm_db_t_001", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            instance.empDao = daoMaster.newSession().getEmpDao();
        }
        return instance;
    }

    /**
     * 插入会员信息
     *
     * @param test
     */
    public void addEmp(Emp test) {
        empDao.insert(test);
    }


    /**
     * 更新数据
     *
     * @param emp
     */
    public void updateEmp(Emp emp) {
        empDao.update(emp);
    }

    /**
     * 删除所有数据--会员信息
     */

    public void deleteAllEmp() {
        empDao.deleteAll();
    }

    /**
     * 插入或是更新数据
     *
     * @return
     */
    public long saveEmp(Emp emp) {
        return empDao.insertOrReplace(emp);
    }

    //查询会员信息
    public Emp getEmpById(String id) {
        Emp emp = empDao.load(id);
        return emp;
    }

}
