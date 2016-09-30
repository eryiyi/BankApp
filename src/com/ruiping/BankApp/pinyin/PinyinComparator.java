package com.ruiping.BankApp.pinyin;

import com.ruiping.BankApp.entiy.BankEmpBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		BankEmpBean o11 = (BankEmpBean) o1;
		BankEmpBean o22 = (BankEmpBean) o2;
		 String str1 = PingYinUtil.getPingYin(o11.getEmpName());
	     String str2 = PingYinUtil.getPingYin(o22.getEmpName());
	     return str1.compareTo(str2);
	}

}
