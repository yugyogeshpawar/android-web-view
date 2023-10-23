package com.blazing.malls;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewSetup {

    public static void configureWebViewSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(webView.getContext()), "Android");
    }
}
