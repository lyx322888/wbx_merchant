package com.wbx.merchant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.utils.LogUtils;

import java.util.Map;

/**
 * Created by wushenghui on 2017/8/1.
 * 消息列表
 */

public class MessageListActivity extends BaseActivity {
    private EaseConversationListFragment contactListFragment;
    private MyReceiver myReceiver;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        contactListFragment = new EaseConversationListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_container, contactListFragment).commit();
        contactListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                ChatActivity.actionStart(mContext, conversation.conversationId());
            }
        });

    }

    @Override
    public void fillData() {
        getChatRecord();
    }

    //获取聊天记录
    private void getChatRecord() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        LogUtils.logd(conversations.toString());
    }

    @Override
    public void setListener() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(AppConfig.REFRESH_UI);
        registerReceiver(myReceiver, intentFilter);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            contactListFragment.refresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
