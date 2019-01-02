package com.wbx.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wbx.merchant.R;
import com.wbx.merchant.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wushenghui on 2018/1/3.
 */

public class WebActivity extends BaseActivity {
    @Bind(R.id.web_view)
    WebView mWebView;
    private String url;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    private boolean isChat;

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        isChat = getIntent().getBooleanExtra("isChat", false);
        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
//        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webSettings.setUseWideViewPort(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }

    @Override
    public void fillData() {
        url = getIntent().getStringExtra("url");
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
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
//                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            }
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//            }

        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void finish() {
        if (isChat) {
            super.finish();
        } else {
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
//    final class InJavaScriptLocalObj {
//        @JavascriptInterface
//        public void showSource(String html) {
//            refreshHtmlContent(html);
//        }
//    }
//    private void refreshHtmlContent(final String html){
//                //解析html字符串为对象
//                Document document = Jsoup.parse(html);
//                //通过类名获取到一组Elements，获取一组中第一个element并设置其html
//                Elements elements = document.getElementsByClass("copyright");
//                elements.remove();
//                //获取进行处理之后的字符串并重新加载
//                String body = document.toString();
//                mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
//    }
}
