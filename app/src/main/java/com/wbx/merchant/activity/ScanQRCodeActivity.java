package com.wbx.merchant.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.FormatUtil;

import butterknife.Bind;
import butterknife.OnClick;

//核销明细
public class ScanQRCodeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_hxmx)
    RoundTextView tvHxmx;

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_q_r_code;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        CaptureFragment captureFragment = new CaptureFragment();
        //定制化扫描框UI
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_qrcode_scan);
        //分析结果回调
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (result.contains("wbx365") && result.contains("use_code")) {
                        String use_code = FormatUtil.getValueByName(result, "use_code");
                    }
                }
            }

            @Override
            public void onAnalyzeFailed() {

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_scan, captureFragment).commit();
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.iv_back, R.id.tv_hxmx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_hxmx:
                //核销明细
                break;
        }
    }
}