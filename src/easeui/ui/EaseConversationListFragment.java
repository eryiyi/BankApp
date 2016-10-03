package easeui.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.ruiping.BankApp.R;
import com.ruiping.BankApp.base.InternetURL;
import com.ruiping.BankApp.data.BankEmpData;
import com.ruiping.BankApp.data.NoteJobTaskObj;
import com.ruiping.BankApp.entiy.BankEmpBean;
import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.entiy.BankNoticesBean;
import com.ruiping.BankApp.huanxin.mine.MyEMConversation;
import com.ruiping.BankApp.huanxin.ui.GroupsActivity;
import com.ruiping.BankApp.ui.MemoListActivity;
import com.ruiping.BankApp.ui.NoticesActivity;
import com.ruiping.BankApp.ui.RenwuListActivity;
import com.ruiping.BankApp.util.Contance;
import com.ruiping.BankApp.util.StringUtil;
import easeui.widget.EaseConversationList;
import org.json.JSONObject;


import java.util.*;

/**
 * conversation list fragment
 *
 */
public class EaseConversationListFragment extends EaseBaseFragment implements OnClickListener{
    private View view;
    private Resources res;

	private final static int MSG_REFRESH = 2;
    protected boolean hidden;
    protected List<MyEMConversation> conversationList = new ArrayList<MyEMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected boolean isConflict;

    private LinearLayout listViewHead;//头部
    private TextView unread_renwu_number;
    private TextView unread_beiwang_number;
    private TextView unread_notice_number;
    private InputMethodManager inputMethodManager;


    protected EMConversationListener convListener = new EMConversationListener(){
		@Override
		public void onCoversationUpdate() {
			refresh();
		}
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
        view = inflater.inflate(R.layout.ease_fragment_conversation_list, null);
        res = getActivity().getResources();
        registerBoradcastReceiver();
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //初始化ListView头部组件
        listViewHead = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.chatheader, null);
        unread_renwu_number = (TextView) listViewHead.findViewById(R.id.unread_renwu_number);
        unread_beiwang_number = (TextView) listViewHead.findViewById(R.id.unread_beiwang_number);
        unread_notice_number = (TextView) listViewHead.findViewById(R.id.unread_notice_number);
        listViewHead.findViewById(R.id.relate_renwu).setOnClickListener(this);
        listViewHead.findViewById(R.id.relate_beiwanglu).setOnClickListener(this);
        listViewHead.findViewById(R.id.relate_notice).setOnClickListener(this);
        getData();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) view.findViewById(R.id.list);

        // button to clear content in search bar
        errorItemContainer = (FrameLayout) view.findViewById(R.id.fl_error_item);
        conversationListView.addHeaderView(listViewHead);


    }

    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.group_chat);
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupsActivity.class);
                startActivity(intent);
            }
        });
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        if(listItemClickListener != null){
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyEMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        conversationListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
        String hxusernames = getHxUsernames(conversationList);
        if(!StringUtil.isNullOrEmpty(hxusernames)){
            getNickNamesByHxUserNames(hxusernames);
        }

    }

    private String getHxUsernames(List<MyEMConversation> conversationList) {
        StringBuffer strUser = new StringBuffer();
        for (int i = 0; i < conversationList.size(); i++) {
            strUser.append(conversationList.get(i).getEmConversation().getUserName());
            if (i < conversationList.size() - 1) {
                strUser.append(",");
            }
        }
        return strUser.toString();
    }


    //获得好友资料
    List<BankEmpBean> emps = new ArrayList<BankEmpBean>();
    //通过环信username获取用户昵称
    private void getNickNamesByHxUserNames(final String hxUserNames) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_INVITE_CONTACT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankEmpData data = getGson().fromJson(s, BankEmpData.class);
                            if (data.getCode() == 200) {
                                emps = data.getData();
                                if(conversationListView != null){
                                    conversationListView.refresh();
                                }
                                notifyMyAdapter();
                            } else {
                                Toast.makeText(getActivity(), "获得数据失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "获得数据失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "获得数据失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hxUserNames", hxUserNames);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);

    }


    private void notifyMyAdapter() {
        for (MyEMConversation my : conversationList) {
            for (BankEmpBean emp : emps) {
                if (my.getEmConversation().getUserName().equals(emp.getHx_name())) {
                    my.setEmp(emp);
                }
            }
        }
        if (conversationListView.adapter != null)
            conversationListView.adapter.notifyDataSetChanged();
    }




    protected EMConnectionListener connectionListener = new EMConnectionListener() {
        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
               handler.sendEmptyMessage(0);
            }
        }
        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;
    
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                onConnectionDisconnected();
                break;
            case 1:
                onConnectionConnected();
                break;
            
            case MSG_REFRESH:
	            {
	            	conversationList.clear();
	                conversationList.addAll(loadConversationList());
                    String hxusernames = getHxUsernames(conversationList);
                    if(!StringUtil.isNullOrEmpty(hxusernames)){
                        getNickNamesByHxUserNames(hxusernames);
                    }

//	                conversationListView.refresh();
	                break;
	            }
            default:
                break;
            }
        }
    };
    
    /**
     * connected to server
     */
    protected void onConnectionConnected(){
        errorItemContainer.setVisibility(View.GONE);
    }
    
    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected(){
        errorItemContainer.setVisibility(View.VISIBLE);
    }
    

    /**
     * refresh ui
     */
    public void refresh() {
    	if(!handler.hasMessages(MSG_REFRESH)){
    		handler.sendEmptyMessage(MSG_REFRESH);
    	}
    }
    
    /**
     * load conversation list
     * 
     * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +    */
    protected List<MyEMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, MyEMConversation>> sortList = new ArrayList<Pair<Long, MyEMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    MyEMConversation my = new MyEMConversation();
                    my.setEmConversation(conversation);
                    sortList.add(new Pair<Long, MyEMConversation>(my.getEmConversation().getLastMessage().getMsgTime(), my));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MyEMConversation> list = new ArrayList<MyEMConversation>();

        for (Pair<Long, MyEMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, MyEMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, MyEMConversation>>() {
            @Override
            public int compare(final Pair<Long, MyEMConversation> con1, final Pair<Long, MyEMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }


    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isConflict){
            outState.putBoolean("isConflict", true);
        }
    }
    
    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         * @param conversation -- clicked item
         */
        void onListItemClicked(MyEMConversation conversation);
    }
    
    /**
     * set conversation list item click listener
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.relate_renwu) {
            Intent intent = new Intent(getActivity(), RenwuListActivity.class);
            intent.putExtra("type", "3");
            startActivity(intent);
        }
        if (i == R.id.relate_beiwanglu) {
            Intent intent = new Intent(getActivity(), MemoListActivity.class);
            startActivity(intent);
        }
        if (i == R.id.relate_notice) {
            Intent intent = new Intent(getActivity(), NoticesActivity.class);
            startActivity(intent);
        }
    }

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("arrived_msg_andMe");//有与我相关
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("arrived_msg_andMe")){
            }
        }
    }  ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

     //根据会员id获取未完成任务列表，公告列表，备忘录列表
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_MAIN_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    NoteJobTaskObj data = getGson().fromJson(s, NoteJobTaskObj.class);
                                    if(data != null){
                                        if(data.getNoticesList()>0){
                                            unread_notice_number.setVisibility(View.VISIBLE);
                                            unread_notice_number.setText(String.valueOf(data.getNoticesList()));
                                        }
                                        if(data.getNote()>0){
                                            unread_beiwang_number.setVisibility(View.VISIBLE);
                                            unread_beiwang_number.setText(String.valueOf(data.getNote()));
                                        }
                                        if(data.getJobTask()>0){
                                            unread_renwu_number.setVisibility(View.VISIBLE);
                                            unread_renwu_number.setText(String.valueOf(data.getJobTask()));
                                        }
                                    }

                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString(Contance.EMP_ID, ""), String.class));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

}
