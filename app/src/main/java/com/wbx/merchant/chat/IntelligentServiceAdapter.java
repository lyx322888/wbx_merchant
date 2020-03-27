package com.wbx.merchant.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.IntelligentServiceActivity;
import com.wbx.merchant.activity.WebActivity;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.ApiConstants;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.UilImageGetter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushenghui on 2018/1/2.
 */

public class IntelligentServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEAD_VIEW = 5;//头部
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_ANSWER_MSG = 3;//接收到问题答案
    public static final int FINAL_ANSWER = 4;//最终答案
    public static final String BASE_URL = ApiConstants.DEBUG ? "http://vrzff.com/Wbxwaphome/other/answer/question_id/" : "http://www.wbx365.com/Wbxwaphome/other/answer/question_id/";
    public static String manualServiceUrl;
    private List<ChatMessageBean> userList = new ArrayList<ChatMessageBean>();
    private Context context;
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    public Handler handler;
    private LayoutInflater mLayoutInflater;
    private ChatDbManager mChatDbManager;
    private final IntelligentServiceActivity mActivity;

    public IntelligentServiceAdapter(Context context, List<ChatMessageBean> userList) {
        mChatDbManager = new ChatDbManager();
        this.context = context;
        mActivity = (IntelligentServiceActivity) context;
        this.userList = userList;
        mLayoutInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
        handler = new Handler();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case HEAD_VIEW:
                //最终答案消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intelligent_head_view, parent, false);
                holder = new IntelligentServiceAdapter.HeadViewHolder(view);
                break;
            case FROM_USER_MSG:
                //普通消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgfrom_list_item, parent, false);
                holder = new IntelligentServiceAdapter.FromUserMsgViewHolder(view);
                break;
            case TO_USER_MSG:
                //发送消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgto_list_item, parent, false);
                holder = new IntelligentServiceAdapter.ToUserMsgViewHolder(view);
                break;
            case FROM_ANSWER_MSG:
                //问题列表消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msgfrom_answer, parent, false);
                holder = new IntelligentServiceAdapter.FromUserAnswerMsgViewHolder(view);
                break;
            case FINAL_ANSWER:
                //最终答案消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_final_answer, parent, false);
                holder = new IntelligentServiceAdapter.FromUserFinalAnswerMsgViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageBean tbub = userList.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case HEAD_VIEW:
                headLayout((HeadViewHolder) holder);
                break;
            case FROM_USER_MSG:
                fromMsgUserLayout((FromUserMsgViewHolder) holder, tbub, position);
                break;
            case TO_USER_MSG:
                toMsgUserLayout((ToUserMsgViewHolder) holder, tbub, position);
                break;
            case FROM_ANSWER_MSG:
                fromAnswerMsgUserLayout((FromUserAnswerMsgViewHolder) holder, tbub, position);
                break;
            case FINAL_ANSWER:
                fromFinalAnswerLayout((FromUserFinalAnswerMsgViewHolder) holder, tbub, position);
                break;
        }
    }

    private void headLayout(HeadViewHolder holder) {
//       holder.firstIm.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               MainActivity.isGoBuy = true;
//               mActivity.finish();
//           }
//       });
        holder.secondIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(manualServiceUrl)) {
                    new MyHttp().doPost(Api.getDefault().getBaiduTimUrl(1), new HttpListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            manualServiceUrl = result.getJSONObject("data").getString("url");
                            manualService();
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
                } else {
                    manualService();
                }
            }
        });
        holder.ThirdIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getOftenQuestion();
            }
        });
    }

    private void manualService() {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("isChat", true);
        intent.putExtra("url", manualServiceUrl);
        context.startActivity(intent);
    }

    private void fromFinalAnswerLayout(FromUserFinalAnswerMsgViewHolder holder, ChatMessageBean tbub, int position) {
        if (position != 0) {
            String showTime = getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        if (TextUtils.isEmpty(tbub.getUserContent())) {
            holder.contentTv.setText("抱歉，小微还没有找到相关答案哦，您可以点击人工客服在线咨询");
        } else {
            holder.contentTv.setText(Html.fromHtml(tbub.getUserContent(), new UilImageGetter(context, holder.contentTv), null));
        }

    }

    private void fromAnswerMsgUserLayout(FromUserAnswerMsgViewHolder holder, final ChatMessageBean tbub, int position) {
        if (position != 0) {
            String showTime = getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

        QuestionAdapter questionAdapter = new QuestionAdapter(JSONArray.parseArray(JSONObject.parseObject(tbub.getUserContent()).getString("data"), QuestionInfo.class), context);
        holder.questionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.questionRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setOnItemClickListener(R.id.title_tv, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                String url = BASE_URL + JSONArray.parseArray(JSONObject.parseObject(tbub.getUserContent()).getString("data"), QuestionInfo.class).get(position).getQuestion_id();
                WebActivity.actionStart(context, url, "问题详情");
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position).getType();
    }


    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        GlideUtils.showSmallPic(context, holder.headicon, userList.get(position).getUserHeadIcon());
        /* time */
        if (position != 0) {
            String showTime = getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(tbub.getUserContent());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class FromUserMsgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private TextView content;

        public FromUserMsgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view
                    .findViewById(R.id.tb_other_user_icon);
            chat_time = (TextView) view.findViewById(R.id.chat_time);
            content = (TextView) view.findViewById(R.id.content);
        }
    }


    class FromUserFinalAnswerMsgViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTv;
        private TextView chat_time;

        public FromUserFinalAnswerMsgViewHolder(View view) {
            super(view);
            contentTv = (TextView) view
                    .findViewById(R.id.content_text_view);
            chat_time = (TextView) view.findViewById(R.id.mychat_time);

        }
    }

    class FromUserAnswerMsgViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView questionRecyclerView;
        private TextView chat_time;

        public FromUserAnswerMsgViewHolder(View view) {
            super(view);
            questionRecyclerView = (RecyclerView) view.findViewById(R.id.question_recycler_view);
            chat_time = (TextView) view.findViewById(R.id.mychat_time);
        }
    }

    class ToUserMsgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private TextView content;
        private ImageView sendFailImg;

        public ToUserMsgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view
                    .findViewById(R.id.tb_my_user_icon);
            chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            content = (TextView) view
                    .findViewById(R.id.mycontent);
//            sendFailImg = (ImageView) view
//                    .findViewById(R.id.mysend_fail_img);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstIm, secondIm, ThirdIm;

        public HeadViewHolder(View view) {
            super(view);
            secondIm = (ImageView) view.findViewById(R.id.second_im);
            ThirdIm = (ImageView) view.findViewById(R.id.third_im);

        }
    }

    private void fromMsgUserLayout(final IntelligentServiceAdapter.FromUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        holder.headicon.setBackgroundResource(R.drawable.robot);
        /* time */
        if (position != 0) {
            String showTime = getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(tbub.getUserContent());
    }


    @SuppressLint("SimpleDateFormat")
    public String getTime(String time, String before) {
        String show_time = null;
        if (before != null) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = df.parse(time);
                java.util.Date date = df.parse(before);
                long l = now.getTime() - date.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min >= 1) {
                    show_time = time.substring(11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show_time = time.substring(11);
        }
        String getDay = getDay(time);
        if (show_time != null && getDay != null) {
            show_time = getDay + " " + show_time;
        }
        return show_time;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDay(String time) {
        String showDay = null;
        String nowTime = returnTime();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date now = df.parse(nowTime);
            java.util.Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            if (day >= 365) {
                showDay = time.substring(0, 10);
            } else if (day >= 1 && day < 365) {
                showDay = time.substring(5, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showDay;
    }

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
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


}
