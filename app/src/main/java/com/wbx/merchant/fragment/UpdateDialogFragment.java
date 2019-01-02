package com.wbx.merchant.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.R;
import com.wbx.merchant.update.UpdateProgressListener;
import com.wbx.merchant.update.UpdateService;
import com.wbx.merchant.widget.NumberProgressBar;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by Vector
 * on 2017/7/19 0019.
 */

public class UpdateDialogFragment extends DialogFragment {
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static boolean isShow = false;
    public static final String JSONSTRING = "json";

    public static UpdateDialogFragment newInstent(String result) {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(JSONSTRING, result);
        updateDialogFragment.setArguments(bundle);
        return updateDialogFragment;
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateService.LocalBinder binder = (UpdateService.LocalBinder) service;
            UpdateService downloadService = binder.getService();

            //接口回调，下载进度
            downloadService.setUpdateProgressListener(new UpdateProgressListener() {
                @Override
                public void start() {

                }

                @Override
                public void update(int progress) {
                    mNumberProgressBar.setProgress(progress);
                }

                @Override
                public void success() {
                    if (getActivity() != null) {
                        getActivity().unbindService(conn);
                    }
                    dismissAllowingStateLoss();
                }

                @Override
                public void error() {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_app_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListener();
    }

    private void setListener() {
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateOkButton.setVisibility(View.GONE);
                mNumberProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), UpdateService.class);
                intent.putExtra(UpdateService.URL, "http://wbx365.com/Public/app/wbxmerchant.apk");
                getActivity().bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });
    }

    private void initView(View view) {
        //标题
        mTitleTextView = (TextView) view.findViewById(R.id.tv_title);
        //提示内容
        mContentTextView = (TextView) view.findViewById(R.id.tv_update_info);
        //更新按钮
        mUpdateOkButton = (Button) view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = (NumberProgressBar) view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = (ImageView) view.findViewById(R.id.iv_close);

        String string = getArguments().getString(JSONSTRING);
        JSONObject jsonObject = JSONObject.parseObject(string);
        mTitleTextView.setText(jsonObject.getJSONObject("data").getString("title"));
        List<String> stringList = JSONArray.parseArray(jsonObject.getJSONObject("data").getString("message"), String.class);
        String infoStr = "";
        for (String str : stringList) {
            infoStr += str + "\n";
        }
        mContentTextView.setText(infoStr);
    }
}