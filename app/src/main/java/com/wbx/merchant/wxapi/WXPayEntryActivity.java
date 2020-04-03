package com.wbx.merchant.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wbx.merchant.MainActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.activity.AuditingActivity;
import com.wbx.merchant.activity.GoodsOrderActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.baseapp.AppManager;
import com.wbx.merchant.utils.ToastUitl;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "com.wbx.merchant.wxapi.WXPayEntryActivity";
    private IWXAPI api;

    TextView errorStateTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        errorStateTv = findViewById(R.id.error_state_tv);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUitl.showShort("支付成功！");
                if (AppConfig.RESULT_PAY_TYPE) {//续费
                    finish();
                    AppManager.getAppManager().finishAllActivity();
                    startActivity(new Intent(WXPayEntryActivity.this, MainActivity.class));
                } else if (AppConfig.isBuy) {
                    AppConfig.isBuy = false;
                    finish();
                    startActivity(new Intent(WXPayEntryActivity.this, GoodsOrderActivity.class));
                } else {//入驻
                    finish();
                    AppManager.getAppManager().finishAllActivity();
                    startActivity(new Intent(WXPayEntryActivity.this, MainActivity.class));
                }
            } else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        errorStateTv.setText("支付失败！");
                    }
                });
            }
        }
    }

    public void close(View view) {
        finish();
    }
}