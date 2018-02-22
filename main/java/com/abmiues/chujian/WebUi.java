package com.abmiues.chujian;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class WebUi extends FragmentActivity {
    private FrameLayout mFrameLayout;
    private WebView mWebView;
    private MyWebChromeClient mMyWebChromeClient;
    //file:///android_asset/test.html
    //file:///android_asset/index.html
    private String URL = "file:///android_asset/index.html";
    //http://www.w3cschool.cc/try/demo_source/mov_bbb.mp4
    private String videoUrl="";
    private String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        videoUrl= getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        if(videoUrl.equals(""))
        {
            Toast.makeText(getApplicationContext(),"请设置视频地址",Toast.LENGTH_LONG).show();
            return ;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_ui);
        mFrameLayout = (FrameLayout) findViewById(R.id.activity_web_ui);
        mWebView = (WebView) findViewById(R.id.webview);
        initWebView();
        mWebView.loadUrl(URL);
    }
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(mMyWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.loadUrl("javascript:setSrc(\""+videoUrl+"\",\""+title+"\")");
            }
        });
        ButtonClick click=new ButtonClick();
        mWebView.addJavascriptInterface(click,click.toString());
    }

    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            mWebView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            mWebView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            //return super.getDefaultVideoPoster();
        return null;
        }
    }


    class ButtonClick
    {
        //这是 button.click() 的触发事件
        //H5调用方法：javascript:button.click0()
        @JavascriptInterface
        public void click(){
           WebUi.this.finish();
            // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface  //必须添加，这样才可以标志这个类的名称是 androidClick
        public String toString(){
            return "androidClick";
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

}

