package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.ShopInfo;
import com.wbx.merchant.utils.ShareUtils;

import butterknife.Bind;

public class InviteActivity extends BaseActivity {
    @Bind(R.id.web_view)
    WebView mWebView;
    private String url;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    private ShopInfo shopInfo;

    public static void actionStart(Context context, String url, ShopInfo shopInfo) {
        Intent intent = new Intent(context, InviteActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("shopInfo", shopInfo);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "androidjs");
    }

    @Override
    public void fillData() {
        url = getIntent().getStringExtra("url");
        shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
        mWebView.loadUrl(url);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            titleNameTv.setText(getIntent().getStringExtra("title"));
        } else {
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (title != null) {
                        titleNameTv.setText(title);
                    }
                }
            });
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void setListener() {

    }

    public class JavaScriptInterface {
        Context context;

        JavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void invite() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShareUtils.getInstance().share(InviteActivity.this, "微百姓独立商铺系统（终身版） 限时抢购中...", "活动期间 开店立减¥300 机会难得", shopInfo.getPhoto(), "https://www.wbx365.com/Wbxwaphome/apply/merchant_detail.html?userid=" + shopInfo.getUser_id());
                }
            });
        }

        @JavascriptInterface
        public void share() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShareUtils.getInstance().shareMiniProgram(InviteActivity.this, shopInfo.getShop_name(), "我在微百姓开了一家店，赶快来看看吧", shopInfo.getPhoto(), "pages/home/store/store?shopID=" + shopInfo.getShop_id() + "&gradeid=" + shopInfo.getGrade_id(), shopInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "http://www.wbx365.com/wap/ele/shop/shop_id/" + shopInfo.getShop_id() + ".html" : "http://www.wbx365.com/wap/shop/goods/shop_id/" + shopInfo.getShop_id() + ".html");
                }
            });
        }
    }

    @Override
    public void finish() {
        if (mWebView == null || TextUtils.isEmpty(mWebView.getUrl())) {
            super.finish();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.finish();
            }
        }
    }
}
