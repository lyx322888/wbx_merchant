package com.wbx.merchant.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.chat.ChatConst;
import com.wbx.merchant.chat.ChatDbManager;
import com.wbx.merchant.chat.ChatMessageBean;
import com.wbx.merchant.chat.HttpListenerChat;
import com.wbx.merchant.chat.IntelligentServiceAdapter;
import com.wbx.merchant.chat.MyHttpChat;
import com.wbx.merchant.utils.KeyBordUtil;
import com.wbx.merchant.widget.refresh.BaseRefreshListener;
import com.wbx.merchant.widget.refresh.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wushenghui on 2018/1/2.
 */

public class IntelligentServiceActivity extends BaseActivity implements BaseRefreshListener {
    @Bind(R.id.content_lv)
    PullToRefreshLayout refreshLayout;
    @Bind(R.id.msg_recycler_view)
    RecyclerView mRecyclerView;
    public List<ChatMessageBean> tblist = new ArrayList<ChatMessageBean>();
    public List<ChatMessageBean> pagelist = new ArrayList<ChatMessageBean>();
    public ChatDbManager mChatDbManager;
    public int currentPage = 0;
    public int number = 10;
    public int position; //加载滚动刷新位置
    private IntelligentServiceAdapter tbAdapter;
    @Bind(R.id.content_edit)
    EditText contentEdit;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, IntelligentServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_intelligent_service;
    }

    @Override
    public void initPresenter() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mChatDbManager = new ChatDbManager();
    }

    @Override
    public void initView() {


    }

    @Override
    public void fillData() {
        loadRecords();
        tbAdapter = new IntelligentServiceAdapter(this, tblist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(tbAdapter);
    }

    private void loadRecords() {
        pagelist = mChatDbManager.loadPages(currentPage, number);
        tblist.addAll(pagelist);
        if (tblist.size() == 0 || tblist.get(0).getType() != IntelligentServiceAdapter.HEAD_VIEW) {
            tblist.add(0, getTbub("头部", IntelligentServiceAdapter.HEAD_VIEW, "", null, null, null,
                    null, null, null, 0f, ChatConst.COMPLETED));
            tblist.add(getTbub("智能客服", IntelligentServiceAdapter.FROM_USER_MSG, "", "Hi,我是微百姓小微！", null, null,
                    null, null, null, 0f, ChatConst.COMPLETED));
        }
        position = pagelist.size();
    }

    @Override
    public void setListener() {
        refreshLayout.setRefreshListener(this);
        findViewById(R.id.send_msg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentEdit.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                tblist.add(getTbub(null != userInfo ? userInfo.getNickname() : "", IntelligentServiceAdapter.TO_USER_MSG, null != userInfo ? userInfo.getFace() : "", content, null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));

                KeyBordUtil.hideSoftKeyboard(contentEdit);
                contentEdit.setText("");
                getQuestion(content);
            }
        });

    }

    public void scrollRecycler() {
        int position = tbAdapter.getItemCount() - 1 < 0 ? 0 : tbAdapter.getItemCount() - 1;
        mRecyclerView.smoothScrollToPosition(position);
    }

    //获得答案
    private void getQuestion(String content) {
        MyHttpChat.doPost(Api.getDefault().getQuestion(content), new HttpListenerChat() {
            @Override
            public void onSuccess(JSONObject result) {
                tblist.add(getTbub("智能客服", IntelligentServiceAdapter.FROM_ANSWER_MSG, "", result.toJSONString(), null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                int position = tbAdapter.getItemCount() - 1 < 0 ? 0 : tbAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(position);
                tbAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onErrorHasInfo(String msg) {
                tblist.add(getTbub("智能客服", IntelligentServiceAdapter.FROM_USER_MSG, "抱歉，小微还没有找到相关答案哦，您可以点击人工客服在线咨询", msg, null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                int position = tbAdapter.getItemCount() - 1 < 0 ? 0 : tbAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(position);
                tbAdapter.notifyDataSetChanged();
            }
        });

    }


    private ChatMessageBean getTbub(String username, int type, String userHeadIcon,
                                    String Content, String imageIconUrl, String imageUrl,
                                    String imageLocal, String userVoicePath, String userVoiceUrl,
                                    Float userVoiceTime, @ChatConst.SendState int sendState) {
        ChatMessageBean tbub = new ChatMessageBean();
        tbub.setUserName(username);
        String time = returnTime();
        tbub.setTime(time);
        tbub.setType(type);
        tbub.setUserContent(Content);
        tbub.setUserHeadIcon(userHeadIcon);
        tbub.setImageIconUrl(imageIconUrl);
        tbub.setImageUrl(imageUrl);
        tbub.setUserVoicePath(userVoicePath);
        tbub.setUserVoiceUrl(userVoiceUrl);
        tbub.setUserVoiceTime(userVoiceTime);
        tbub.setSendState(sendState);
        tbub.setImageLocal(imageLocal);
        mChatDbManager.insert(tbub);

        return tbub;
    }

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    @Override
    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tblist.clear();
                currentPage = 0;
                loadRecords();
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                tbAdapter.notifyDataSetChanged();
            }
        }, 2000);

    }

    @Override
    public void loadMore() {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (currentPage >= mChatDbManager.getPages(10)) {
            return;
        }
        currentPage++;
        loadRecords();
        tbAdapter.notifyDataSetChanged();
    }

    public void getOftenQuestion() {
        MyHttpChat.doPost(Api.getDefault().getOftenQuestion(), new HttpListenerChat() {
            @Override
            public void onSuccess(JSONObject result) {
                tblist.add(getTbub("智能客服", IntelligentServiceAdapter.FROM_ANSWER_MSG, "", result.toJSONString(), null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                int position = tbAdapter.getItemCount() - 1 < 0 ? 0 : tbAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(position);
                tbAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onErrorHasInfo(String msg) {
                tblist.add(getTbub("智能客服", IntelligentServiceAdapter.FROM_USER_MSG, "", msg, null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                int position = tbAdapter.getItemCount() - 1 < 0 ? 0 : tbAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(position);
                tbAdapter.notifyDataSetChanged();
            }
        });
    }
}
