package com.bconnect.b_connect;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        WebView mWebView=(WebView)findViewById(R.id.Logain_webView);

        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    Log.e("SR", "mWebview.urlS : " + view.getUrl().toString());
                } else {

                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e("SR", "mWebview.urlS : " + view.getUrl().toString());

            }


        });
        mWebView.loadUrl("http://www.bconnect-egypt.info/");


//        webview.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                Log.e("SR", "mWebview :Loading url" );
//
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, final String urlFinal) {
//
//
//
//                Log.e("SR", "mWebview.urlS :" + urlFinal.toString() +" OR "+ view.getUrl().toString());
//
//
//            }
//        });
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //your handling...
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}
