/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruiping.BankApp.huanxin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruiping.BankApp.BankAppApplication;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.adapter.AnimateFirstDisplayListener;
import com.ruiping.BankApp.adapter.OnClickContentItemListener;
import com.ruiping.BankApp.base.BaseActivity;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpData;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.pinyin.PinyinComparator;
import com.ruiping.BankApp.util.StringUtil;
import easeui.widget.EaseSidebar;
import org.json.JSONObject;

import java.util.*;

public class GroupPickContactsActivity extends BaseActivity {
	private boolean[] isCheckedArray;

	private ListView listView;
	/** if this is a new group */
	protected boolean isCreatingNewGroup;
	private PickContactAdapter contactAdapter;
	/** members already in the group */
	private List<String> existMembers;
	private List<BankEmpBean> listEmps = new ArrayList<BankEmpBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_group_pick_contacts);

		String groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// create new group
			isCreatingNewGroup = true;
		} else {
			// get members of the group
			EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
			existMembers = group.getMembers();
		}
		if(existMembers == null)
			existMembers = new ArrayList<String>();
		// get contact list
//		final List<EaseUser> alluserList = new ArrayList<EaseUser>();
//		for (EaseUser user : DemoHelper.getInstance().getContactList().values()) {
//			if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME) & !user.getUsername().equals(Constant.CHAT_ROOM) & !user.getUsername().equals(Constant.CHAT_ROBOT))
//				alluserList.add(user);
//		}
		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(listEmps, GroupPickContactsActivity.this);
		listView.setAdapter(contactAdapter);
		((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
							checkBox.toggle();
			}
		});
		getData();
	}

	void getData(){
		StringRequest request = new StringRequest(
				Request.Method.POST,
				InternetURL.GET_FRIENDS_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						if (StringUtil.isJson(s)) {
							try {
								JSONObject jo = new JSONObject(s);
								String code1 = jo.getString("code");
								if (Integer.parseInt(code1) == 200) {
									BankEmpData data = getGson().fromJson(s, BankEmpData.class);
									listEmps.clear();
									listEmps.addAll(data.getData());
									Collections.sort(listEmps,new PinyinComparator());

									//adapter
									isCheckedArray = new boolean[listEmps.size()];
									contactAdapter.notifyDataSetChanged();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						Toast.makeText(GroupPickContactsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};

		request.setRetryPolicy(new DefaultRetryPolicy(10000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		getRequestQueue().add(request);
	}


	/**
	 * save selected members
	 * 
	 * @param v
	 */
	public void save(View v) {
		setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
		finish();
	}

	/**
	 * get selected members
	 * 
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		int length = isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			BankEmpBean bankEmpBean = listEmps.get(i);
			String username = bankEmpBean.getHx_name();
			if (isCheckedArray[i] && !existMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	/**
	 * adapter
	 */
//	private class PickContactAdapter extends EaseContactAdapter {
//
//		private boolean[] isCheckedArray;
//
//		public PickContactAdapter(Context context, int resource, List<BankEmpBean> users) {
//			super(context, resource, users);
//			isCheckedArray = new boolean[users.size()];
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) {
//			View view = super.getView(position, convertView, parent);
//
////			final String username = getItem(position).getUsername();
//			final BankEmpBean bankEmpBean = listEmps.get(position);
//
//			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
//			ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
//			TextView nameView = (TextView) view.findViewById(R.id.name);
//			nameView.setText(bankEmpBean.getEmpName());
//			imageLoader.displayImage(InternetURL.INTERNAL + bankEmpBean.getEmpCover(), avatarView, BankAppApplication.txOptions, animateFirstListener);
//			if (checkBox != null) {
//			    if(existMembers != null && existMembers.contains(bankEmpBean.getHx_name())){
//                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
//                }else{
//                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
//                }
//
//				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						// check the exist members
//						if (existMembers.contains(bankEmpBean.getHx_name())) {
//								isChecked = true;
//								checkBox.setChecked(true);
//						}
//						isCheckedArray[position] = isChecked;
//
//					}
//				});
//				// keep exist members checked
//				if (existMembers.contains(bankEmpBean.getHx_name())) {
//						checkBox.setChecked(true);
//						isCheckedArray[position] = true;
//				} else {
//					checkBox.setChecked(isCheckedArray[position]);
//				}
//			}
//
//			return view;
//		}
//	}

	public void back(View view){
		finish();
	}

	private class PickContactAdapter extends BaseAdapter {
		private ViewHolder holder;
		private List<BankEmpBean> lists;
		private Context mContect;
		Resources res;

		ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private OnClickContentItemListener onClickContentItemListener;

		public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
			this.onClickContentItemListener = onClickContentItemListener;
		}

		public PickContactAdapter(List<BankEmpBean> lists, Context mContect) {
			this.lists = lists;
			this.mContect = mContect;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			res = mContect.getResources();
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContect).inflate(R.layout.em_row_contact_with_checkbox, null);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final BankEmpBean cell = lists.get(position);
			if (cell != null) {
				holder.name.setText(cell.getEmpName());
				imageLoader.displayImage(InternetURL.INTERNAL+cell.getEmpCover(), holder.avatar, BankAppApplication.txOptions, animateFirstListener);

				if (holder.checkbox != null) {
			    if(existMembers != null && existMembers.contains(cell.getHx_name())){
					holder.checkbox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
                }else{
					holder.checkbox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
                }

					holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// check the exist members
						if (existMembers.contains(cell.getHx_name())) {
								isChecked = true;
							holder.checkbox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;

					}
				});
				// keep exist members checked
//				if (existMembers.contains(cell.getHx_name())) {
//					holder.checkbox.setChecked(true);
//						isCheckedArray[position] = true;
//				} else {
//					holder.checkbox.setChecked(isCheckedArray[position]);
//				}
			}


			}

			return convertView;
		}

		class ViewHolder {
			TextView name;
			ImageView avatar;
			CheckBox checkbox;
		}

	}
}
