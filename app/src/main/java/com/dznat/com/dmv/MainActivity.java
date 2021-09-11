package com.dznat.com.dmv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private FrameLayout fullVideo;
    private View customView = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }

        webView=findViewById(R.id.webView);
        fullVideo=findViewById(R.id.full_video);
        progressBar=findViewById(R.id.progress);

        webView.loadUrl("http://mv.dznat.com:8880/");

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        setWebView();
    }
    // 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            // 返回键退回
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null){
            webView.destroy();
        }
    }

    private void setWebView(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class MyWebChromeClient extends WebChromeClient{

        @Override
        public void onHideCustomView() {

            if (customView == null){
                return;
            }
            fullVideo.removeView(customView);
            fullVideo.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏

        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            customView = view;
            fullVideo.setVisibility(View.VISIBLE);
            fullVideo.addView(customView);
            fullVideo.bringToFront();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        }

    }


    private class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }


}
